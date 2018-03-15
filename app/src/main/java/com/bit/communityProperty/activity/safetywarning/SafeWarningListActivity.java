package com.bit.communityProperty.activity.safetywarning;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.elevator.adapter.FragmentAdapter;
import com.bit.communityProperty.activity.safetywarning.fragment.FinishedListFragment;
import com.bit.communityProperty.activity.safetywarning.fragment.WaitDealListFragment;
import com.bit.communityProperty.activity.safetywarning.fragment.WaitSolveListFragment;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.receiver.JPushBean;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class SafeWarningListActivity extends BaseActivity {


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
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] tabTitles = new String[]{"待受理", "待排查", "已解决"};

    private JPushBean jPushBean;

    private WaitDealListFragment waitDealListFragment;
    @Override
    public int getLayoutId() {
        return R.layout.activity_safe_warning_list;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView() {
        actionBarTitle.setText("安防警报");
        final List<Fragment> list = new ArrayList();
        waitDealListFragment = new WaitDealListFragment();
        list.add(waitDealListFragment);
        list.add(new WaitSolveListFragment());
        list.add(new FinishedListFragment());
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager(), list, tabTitles);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        slidingTabs.setupWithViewPager(viewpager);
        slidingTabs.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(slidingTabs, 20, 20);
            }
        });
        jPushBean = (JPushBean) getIntent().getSerializableExtra("jpushbean");
        if (jPushBean != null) {
            DialogUtil.showConfirmDialog(this, "安防警报", "确定前往排查？", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    RxBus.get().post(jPushBean.getData());
                    waitDealListFragment.receiveAlarm(jPushBean.getData().getPolice_id());
                    DialogUtil.dissmiss();
                }
            });
        }
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object s) throws Exception {
                if (s instanceof String) {
                    if (s.equals("change1")) {
                        viewpager.setCurrentItem(0);
                    }else if (s.equals("change2")){
                        viewpager.setCurrentItem(1);
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
