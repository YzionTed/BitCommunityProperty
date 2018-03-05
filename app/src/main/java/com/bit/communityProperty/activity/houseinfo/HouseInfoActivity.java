package com.bit.communityProperty.activity.houseinfo;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.houseinfo.bean.HouseInfoBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HouseInfoActivity extends BaseActivity {


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
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_mianji)
    TextView tvMianji;
    @BindView(R.id.tv_zhufanshu)
    TextView tvZhufanshu;
    @BindView(R.id.tv_zhuhushu)
    TextView tvZhuhushu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_house_info;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView() {
        actionBarTitle.setText("小区信息");
        GetResidential_quarters_Detail();
    }

    private void GetResidential_quarters_Detail() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().Detail("5a82adf3b06c97e0cd6c0f3d"), new Observer<BaseEntity<HouseInfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<HouseInfoBean> stringBaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(stringBaseEntity));
                if (stringBaseEntity.isSuccess()){
                    HouseInfoBean infoBean = stringBaseEntity.getData();
                    tvName.setText(infoBean.getName());
                    tvAddress.setText(infoBean.getAddress());
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
