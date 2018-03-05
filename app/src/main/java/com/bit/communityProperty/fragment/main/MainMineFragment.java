package com.bit.communityProperty.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.LogonActivity;
import com.bit.communityProperty.activity.company.CompanyInfo;
import com.bit.communityProperty.activity.houseinfo.HouseInfoActivity;
import com.bit.communityProperty.activity.mail_list.Mail_list;
import com.bit.communityProperty.activity.userinfo.AboutUsActivity;
import com.bit.communityProperty.activity.userinfo.FeedbackActivity;
import com.bit.communityProperty.activity.userinfo.PersonInfoActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.bean.MessageEvent;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.StringUtils;
import com.bit.communityProperty.utils.ToastUtil;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by DELL60 on 2018/2/2.
 */

public class MainMineFragment extends BaseFragment {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;
    @BindView(R.id.ll_feedback)
    LinearLayout llFeedback;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.outlogin)
    TextView outlogin;
    Unbinder unbinder;
    @BindView(R.id.personinfo)
    LinearLayout personinfo;
    @BindView(R.id.houseinfo)
    LinearLayout houseinfo;
    @BindView(R.id.companyinfo)
    TextView companyinfo;
    @BindView(R.id.maillist)
    LinearLayout maillist;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.nickName)
    TextView nickName;
    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;
    PromptDialog outLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_mine;
    }

    @Override
    protected void initViewAndData() {
        mBasIn = new BounceTopEnter();
        mBasOut = new SlideBottomExit();
        if (StringUtils.isBlank((String) SPUtil.get(getActivity(), AppConfig.token, ""))) {
            login.setVisibility(View.VISIBLE);
            outlogin.setVisibility(View.GONE);
        } else {
            login.setVisibility(View.GONE);
            outlogin.setVisibility(View.VISIBLE);
            username.setText((String) SPUtil.get(getActivity(), AppConfig.name, ""));
            nickName.setText((String) SPUtil.get(getActivity(), AppConfig.nickName, ""));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoginSuccess(MessageEvent messageEvent) {
        if (messageEvent.isLoginSuccess() == true) {
            login.setVisibility(View.GONE);
            outlogin.setVisibility(View.VISIBLE);
            username.setText((String) SPUtil.get(getActivity(), AppConfig.name, ""));
            nickName.setText((String) SPUtil.get(getActivity(), AppConfig.nickName, ""));
        } else if (messageEvent.isLoginSuccess() == false) {
            login.setVisibility(View.VISIBLE);
            outlogin.setVisibility(View.GONE);
            SPUtil.clear(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        outLogin = new PromptDialog(getActivity());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    Intent intent = null;

    @OnClick({R.id.maillist, R.id.ll_update, R.id.houseinfo, R.id.personinfo, R.id.ll_feedback, R.id.login, R.id.outlogin, R.id.ll_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                intent = new Intent(getActivity(), LogonActivity.class);
                startActivity(intent);
                break;
            case R.id.outlogin:
                MaterialDialogDefault();
                break;
            case R.id.ll_about:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_update:
                intent = new Intent(getActivity(), CompanyInfo.class);
                startActivity(intent);
                break;
            case R.id.ll_feedback:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.personinfo:
                if(StringUtils.isBlank((String) SPUtil.get(getActivity(),AppConfig.token,""))){
                     ToastUtil.showSingletonText(getActivity(),"请先登录账号",3000);
                }else{
                    intent = new Intent(getActivity(), PersonInfoActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.houseinfo:
                intent = new Intent(getActivity(), HouseInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.maillist:
                intent = new Intent(getActivity(), Mail_list.class);
                startActivity(intent);
                break;
        }
    }

    private void MaterialDialogDefault() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("确定退出当前登录账号吗？")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(20)//
                .title("退出账号")
                .titleTextColor(Color.parseColor("#333333"))
                .contentTextColor(Color.parseColor("#666666"))
                .contentTextSize(15)
                .btnTextColor(Color.parseColor("#666666"), Color.parseColor("#5d8efe"))
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        //
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {

                        outlogin();
                        dialog.dismiss();
                    }
                });
    }

    private void outlogin() {
        outLogin.showLoading("退出登录中");
       RetrofitManage.getInstance().subscribe(Api.getInstance().signOut(), new Observer<BaseEntity<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(BaseEntity<String> s) {
                if (s.isSuccess() == true) {
                    outLogin.showSuccess("退出登录成功");
                    MessageEvent event = new MessageEvent();
                    event.setLoginSuccess(false);
                    EventBus.getDefault().post(event);
                    SPUtil.clear(getActivity());
                } else {
                    ToastUtil.showSingletonText(getActivity(), s.getErrorMsg(), 3000);
                }
                LogManager.printErrorLog("backinfo", "退出登录返回数据：" + GsonUtils.getInstance().toJson(s));

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                outLogin.dismiss();
            }
        });

    }
}
