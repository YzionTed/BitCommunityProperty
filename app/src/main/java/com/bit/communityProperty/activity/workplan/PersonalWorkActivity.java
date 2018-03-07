package com.bit.communityProperty.activity.workplan;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.workplan.bean.PersonalWorkListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ViewHolder;
import com.classic.common.MultipleStatusView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PersonalWorkActivity extends BaseActivity {


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
    @BindView(R.id.lv_work)
    ListView lvWork;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private CommonAdapter commonAdapter;
    private int id = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_work;
    }

    @Override
    public void initViewData() {
        multipleStatusView.showLoading();
        actionBarTitle.setText((String) SPUtil.get(this, AppConfig.name, "个人排班"));
        commonAdapter = new CommonAdapter<PersonalWorkListBean>(this, R.layout.item_personal_work) {
            @Override
            public void convert(ViewHolder holder, PersonalWorkListBean o, int position, View convertView) {
                holder.setText(R.id.tv_date, TimeUtils.stampToDate(o.getWorkDate()));
                String work;
                if (o.getRemark() != null && !o.getRemark().equals("")) {
                    work = o.getClassName() + "(" + o.getRemark() + ")";
                } else {
                    work = o.getClassName();
                }
                holder.setText(R.id.tv_work, work);
                switch (o.getClassName()) {
                    case "白班":
                        holder.setTextColor(R.id.tv_work, ContextCompat.getColor(mContext, R.color.yellow));
                        break;
                    case "早班":
                        holder.setTextColor(R.id.tv_work, ContextCompat.getColor(mContext, R.color.blues));
                        break;
                    case "晚班":
                        holder.setTextColor(R.id.tv_work, ContextCompat.getColor(mContext, R.color.text_status_red));
                        break;
                    default:
                        holder.setTextColor(R.id.tv_work, ContextCompat.getColor(mContext, R.color.tv_black_33));
                        break;
                }
            }
        };
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleStatusView.showLoading();
                getList();
            }
        });
        lvWork.setAdapter(commonAdapter);
        getList();
    }

    private void getList() {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        map.put("userId", SPUtil.get(this, AppConfig.id, ""));
        RetrofitManage.getInstance().subscribe(Api.getInstance().getWorkList(map), new Observer<BaseEntity<List<PersonalWorkListBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<PersonalWorkListBean>> listBaseEntity) {
                if (listBaseEntity.isSuccess()){
                    if (listBaseEntity.getData()!=null&&listBaseEntity.getData().size()>0){
                        multipleStatusView.showContent();
                        commonAdapter.setDatas(listBaseEntity.getData());
                    }else{
                        multipleStatusView.showEmpty();
                    }
                }else{
                    multipleStatusView.showError();
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

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

}
