package com.bit.communityProperty.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.newsdetail.NewsDetailActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.fragment.main.adapter.MainNewsAdapter;
import com.bit.communityProperty.fragment.main.bean.MainNewsBean;
import com.bit.communityProperty.fragment.main.bean.WeatherInfoBean;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.TimeUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.CommonHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by DELL60 on 2018/2/2.
 */

public class MainNewsFragment extends BaseFragment {


    @BindView(R.id.rlv_news)
    LRecyclerView rlvNews;
    Unbinder unbinder;

    private TextView tvCountDown;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private MainNewsAdapter newsAdapter;
    private CountDownTimer timer;

    private TextView tvTemperature;
    private TextView tvWeather;
    private TextView tvQuality;
    private TextView tvDate;
    private TextView tvWeek;
    private LinearLayout llEmpty;

    private List<MainNewsBean.RecordsBean> noticeList;
    private MainNewsBean mainNewsBean;
    private int page = 1;
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_news;
    }

    @Override
    protected void initViewAndData() {
        initListView();
////        setCountDown(24 * 60 * 1000);
        getWeatherInfo();
        getNoticeList();
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof String) {
                    if (o != null && o.equals("location")) {
                        getWeatherInfo();
                    }
                }
            }
        });
    }

    private void getWeatherInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("city", SPUtil.get(mActivity, AppConfig.CITY, "包头市"));
        RetrofitManage.getInstance().subscribe(Api.getInstance().getWeatherInfo(map), new Observer<BaseEntity<WeatherInfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<WeatherInfoBean> weatherInfoBeanBaseEntity) {
                if (weatherInfoBeanBaseEntity.isSuccess()) {
                    WeatherInfoBean weatherInfoBean = weatherInfoBeanBaseEntity.getData();
                    tvTemperature.setText(weatherInfoBean.getLow() + "/" + weatherInfoBean.getHigh() + "℃");
                    tvWeather.setText(weatherInfoBean.getType());
                    tvQuality.setText("空气质量指数：" + weatherInfoBean.getAqi() + " " + weatherInfoBean.getQuality());
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

    private void getNoticeList() {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", AppConfig.COMMUNITYID);
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        map.put("page", page);
        map.put("size", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getNoticeList(map), new Observer<BaseEntity<MainNewsBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<MainNewsBean> listBaseEntity) {
                rlvNews.refreshComplete(AppConfig.pageSize);
                if (listBaseEntity.isSuccess()) {
                    mainNewsBean = listBaseEntity.getData();
                    noticeList = listBaseEntity.getData().getRecords();
                    if (isRefresh) {
                        if (mainNewsBean.getCurrentPage() < mainNewsBean.getTotalPage()) {
                            rlvNews.setLoadMoreEnabled(true);
                        } else {
                            rlvNews.setLoadMoreEnabled(false);
                        }
                        if (noticeList != null && noticeList.size() > 0) {
                            llEmpty.setVisibility(View.GONE);
                            newsAdapter.setDataList(noticeList);
                        } else {
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        newsAdapter.addAll(noticeList);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                rlvNews.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onComplete() {
                rlvNews.refreshComplete(AppConfig.pageSize);
            }
        });
    }

    private void setCountDown(long time) {
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(TimeUtils.timeParse(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    private void initListView() {
        newsAdapter = new MainNewsAdapter(mActivity);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(newsAdapter);
        CommonHeader commonHeader = new CommonHeader(mActivity, R.layout.include_main_news_head);
        mLRecyclerViewAdapter.addHeaderView(commonHeader);
        llEmpty = commonHeader.findViewById(R.id.ll_empty);
        tvTemperature = commonHeader.findViewById(R.id.tv_temperature);
        tvWeather = commonHeader.findViewById(R.id.tv_weather);
        tvQuality = commonHeader.findViewById(R.id.tv_quality);
        tvDate = commonHeader.findViewById(R.id.tv_date);
        tvWeek = commonHeader.findViewById(R.id.tv_week);
        tvDate.setText(TimeUtils.getCurrentDate());
        tvWeek.setText(TimeUtils.getCurrentWeek());
        tvCountDown = commonHeader.findViewById(R.id.tv_count_down);
        DividerDecoration divider = new DividerDecoration.Builder(mContext)
                .setHeight(R.dimen.common_dp_10)
                .setColorResource(R.color.appbg)
                .build();
        rlvNews.addItemDecoration(divider);
        rlvNews.setAdapter(mLRecyclerViewAdapter);
        rlvNews.setLayoutManager(new LinearLayoutManager(mActivity));
        rlvNews.setPullRefreshEnabled(true);
        rlvNews.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getNoticeList();
                getWeatherInfo();
            }
        });
        rlvNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mainNewsBean.getCurrentPage() < mainNewsBean.getTotalPage()) {
                    isRefresh = false;
                    getNoticeList();
                } else {
                    rlvNews.setNoMore(true);
                }
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("id", noticeList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind(); //不解绑，防止空指针
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
