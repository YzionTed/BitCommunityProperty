package com.bit.communityProperty.activity.safetywarning;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.safetywarning.bean.AlarmListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SafeWarningDetailActivity extends BaseActivity {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_right_action_bar)
    TextView btnRightActionBar;
    @BindView(R.id.iv_right_action_bar)
    ImageView ivRightActionBar;
    @BindView(R.id.pb_loaing_action_bar)
    ProgressBar pbLoaingActionBar;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.tv_bj_name)
    TextView tvBjName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_baoan)
    TextView tvBaoan;
    @BindView(R.id.tv_accept_time)
    TextView tvAcceptTime;
    @BindView(R.id.tv_remove_time)
    TextView tvRemoveTime;
    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.ll_baoan)
    LinearLayout llBaoan;
    @BindView(R.id.ll_accept_time)
    LinearLayout llAcceptTime;
    @BindView(R.id.ll_remove_time)
    LinearLayout llRemoveTime;
    @BindView(R.id.ll_report)
    LinearLayout llReport;

    private AlarmListBean.RecordsBean recordsBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_safe_warning_detail;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("警报详情");
        recordsBean = (AlarmListBean.RecordsBean) getIntent().getSerializableExtra("bean");
        if (recordsBean != null) {
            tvBjName.setText(recordsBean.getCallerName());
            tvTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getCallTime()));
            tvAddress.setText(recordsBean.getCommunityName()+recordsBean.getBuildingName()+recordsBean.getRoomName());
            tvContact.setText(recordsBean.getCallerPhoneNum());
            switch (recordsBean.getReceiveStatus()) {
                case 1:
                    tvStatus.setText("待受理");
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_status_red));
                    btnGo.setVisibility(View.VISIBLE);
                    btnGo.setText("前往排查");
                    break;
                case 2:
                    tvStatus.setText("待排查");
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_status_red));
                    tvBaoan.setText(recordsBean.getReceiverName());
                    tvAcceptTime.setText(recordsBean.getReceiveTime());
                    llBaoan.setVisibility(View.VISIBLE);
                    llAcceptTime.setVisibility(View.VISIBLE);
                    if (SPUtil.get(this, AppConfig.id,"").equals(recordsBean.getReceiverId())){
                        btnGo.setVisibility(View.VISIBLE);
                        btnGo.setText("警报排除");
                    }else{
                        btnGo.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    tvStatus.setText("已解除");
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.tv_black_33));
                    tvBaoan.setText(recordsBean.getReceiverName());
                    tvAcceptTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getReceiveTime()));
                    tvRemoveTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getTroubleShootingTime()));
                    tvReport.setText(recordsBean.getTroubleShootingReport());
                    llBaoan.setVisibility(View.VISIBLE);
                    llAcceptTime.setVisibility(View.VISIBLE);
                    llRemoveTime.setVisibility(View.VISIBLE);
                    llReport.setVisibility(View.VISIBLE);
                    btnGo.setVisibility(View.GONE);
                    break;
            }
        }

        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof String){
                    if (o.equals("finish")){
                        RxBus.get().post("update");
                        finish();
                    }
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.btn_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_go:
                if (recordsBean.getReceiveStatus()==1){
                    receiveAlarm();
                }else{
                    startActivity(new Intent(this, SafeWarningReportActivity.class).putExtra("id",recordsBean.getId()));
                }
                break;
        }
    }

    private void receiveAlarm(){
        Map<String, Object> map = new HashMap<>();
        map.put("Id", recordsBean.getId());
        map.put("receiverId",SPUtil.get(this, AppConfig.id,""));
        RetrofitManage.getInstance().subscribe(Api.getInstance().receiveAlarm(map), new Observer<BaseEntity<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<Object> baseEntity) {
                if (baseEntity.isSuccess()){
                    RxBus.get().post("update");
                    ToastUtil.showShort("接单成功,请前往排查");
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
