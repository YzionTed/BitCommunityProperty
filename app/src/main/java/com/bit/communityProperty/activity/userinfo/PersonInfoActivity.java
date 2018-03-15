package com.bit.communityProperty.activity.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.userinfo.bean.UserData;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.CircleImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 用户信息
 */
public class PersonInfoActivity extends BaseActivity {


    PromptDialog userinfo;
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
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;
    @BindView(R.id.tv_en_name)
    TextView tvEnName;
    @BindView(R.id.layout_name)
    LinearLayout layoutName;
    @BindView(R.id.tv_ch_name)
    TextView tvChName;
    @BindView(R.id.ll_ch_name)
    LinearLayout llChName;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.ll_country)
    LinearLayout llCountry;
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    public void initViewData() {
        userinfo = new PromptDialog(PersonInfoActivity.this);
        initView();
        getcurrUser();
    }

    private void initView() {
        actionBarTitle.setText("用户信息");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
    }

    private void getcurrUser() {
        userinfo.showLoading("获取数据中", false);
        RetrofitManage.getInstance().subscribe(Api.getInstance().GetCurrUser(), new Observer<BaseEntity<UserData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<UserData> userDataBaseEntity) {
                if (userDataBaseEntity.isSuccess() == true) {
                    UserData userData = userDataBaseEntity.getData();
                    if (userData != null) {
                        tvEnName.setText(userData.getName());
                        tvPhone.setText(userData.getPhone());
                        tvCountry.setText(userData.getAge());
                        if (userData.getSex() == 1) {
                            tvChName.setText("男");
                        } else if (userData.getSex() == 2) {
                            tvChName.setText("女");
                        }
                    }
                }
                LogManager.printErrorLog("backinfo", "用户信息返回数据：" + GsonUtils.getInstance().toJson(userDataBaseEntity));
            }

            @Override
            public void onError(Throwable e) {
                LogManager.printErrorLog("backinfo", "用户信息返回错误：" + e);
            }

            @Override
            public void onComplete() {
                userinfo.dismiss();
            }

        });

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
