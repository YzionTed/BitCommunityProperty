package com.bit.communityProperty.activity.releasePass;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.Zxing.CaptureActivity;
import com.bit.communityProperty.activity.releasePass.bean.ReleasePassDetailsBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.view.TitleBarView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 放行条详情页面
 * Created by kezhangzhao on 2018/2/26.
 */

public class ReleasePassDetailsActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private ReleasePassDetailsBean mReleasePassDetailsBean;//放行条详情bean
    private TextView tvStatus;//放行条状态
    private TextView tvName;//名字
    private TextView tvPhone;//电话
    private TextView tvTime;//时间
    private TextView tvRemark;//备注
    private Button btConfirm;//确认按钮
    private String passId;//放行条id

    @Override
    public int getLayoutId() {
        return R.layout.activity_release_pass_details;
    }

    @Override
    public void initViewData() {
        passId = getIntent().getStringExtra("PASS_Id");
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("扫码放行");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvStatus = findViewById(R.id.tv_status);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvTime = findViewById(R.id.tv_time);
        tvRemark = findViewById(R.id.tv_remark);
        btConfirm = findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new MyOnClickListener());

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (passId!=null)
        getReleasePassInfo(passId);
    }

    /**
     * 获取放行条详情
     *
     * @param releaseId 放行条id=5a925f666951ff48ffd1c5cd
     */
    private void getReleasePassInfo(String releaseId) {
        String url = "/v1/property/rpass/" + releaseId + "/detail";
        RetrofitManage.getInstance().subscribe(Api.getInstance().getReleasePassInfo(url), new Observer<BaseEntity<ReleasePassDetailsBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ReleasePassDetailsBean> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess()) {
                    mReleasePassDetailsBean = BaseEntity.getData();
                    setDetailsMsg(mReleasePassDetailsBean);
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


    /**
     * 审批放行条
     * @param id 放行条id
     * @param auditStatus 审批状态码 1=审核通过；0=未审核；-1=驳回；
     */
    private void approvalPass(String id,String auditStatus){
        final Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("auditStatus",auditStatus);
        RetrofitManage.getInstance().subscribe(Api.getInstance().approvalPass(map), new Observer<BaseEntity<ReleasePassDetailsBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ReleasePassDetailsBean> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess()) {
                    Toast.makeText(mContext,"成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,BaseEntity.getErrorMsg(),Toast.LENGTH_SHORT).show();
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

    /**
     * 设置详情信息
     *
     * @param bean
     */
    private void setDetailsMsg(ReleasePassDetailsBean bean) {
        if (bean.getReleaseStatus() == 0) {
            tvStatus.setText("已失效");
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_fd4f48));
        } else {
            tvStatus.setText("可通行");
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.title_background));
        }
        tvName.setText(bean.getUserName());
        tvPhone.setText(bean.getPhone());
        tvTime.setText(TimeUtils.stampToMonthDayWithHm(bean.getBeginAt()) + "～" + TimeUtils.stampToMonthDayWithHm(bean.getEndAt()));
        if (bean.getRemark() != null) {
            tvRemark.setText(bean.getRemark().toString());
        } else {
            tvRemark.setText("");
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_confirm://确认
                    approvalPass(mReleasePassDetailsBean.getId(),"1");
                    break;
                default:
                    break;
            }
        }
    }
}
