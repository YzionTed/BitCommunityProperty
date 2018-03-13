package com.bit.communityProperty.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.OnlineData;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.TitleBarView;
import com.netease.nim.uikit.api.NimUIKit;
import com.zhy.adapter.abslistview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OnlineActivity extends BaseActivity {

    @BindView(R.id.titlebarview)
    TitleBarView titleBarView;
    @BindView(R.id.lv_online)
    ListView lvOnline;

    private List<OnlineData> onlineDataList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_online;
    }

    @Override
    public void initViewData() {

        titleBarView.setTvTitleText("在线客服");
        titleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestOnlineData();

        lvOnline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (NimUIKit.getAccount() != null) {
                    NimUIKit.startP2PSession(OnlineActivity.this, onlineDataList.get(i).getId());
                }
            }
        });

    }

    public void requestOnlineData() {
        String url = "/v1/user/property/" + AppConfig.COMMUNITYID + "/user-list";
        Map<String, Object> map = new HashMap<>();
        map.put("postCode", "SUPPORTSTAFF");
        RetrofitManage.getInstance().subscribe(Api.getInstance().online(url, map), new Observer<BaseEntity<ArrayList<OnlineData>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ArrayList<OnlineData>> stringBaseEntity) {
                if (stringBaseEntity.isSuccess() && stringBaseEntity.getData() != null) {
                    onlineDataList = (ArrayList<OnlineData>) stringBaseEntity.getData();

                    if (onlineDataList != null && onlineDataList.size() > 0){
                        lvOnline.setAdapter(new CommonAdapter<OnlineData>(OnlineActivity.this, R.layout.item_online, onlineDataList) {
                            @Override
                            protected void convert(com.zhy.adapter.abslistview.ViewHolder viewHolder, OnlineData item, int position) {
                                if (position == onlineDataList.size() - 1){
                                    viewHolder.getView(R.id.divider).setVisibility(View.GONE);
                                }
                                viewHolder.setText(R.id.tv_name, item.getCommunityName() + item.getPropertyName());


                            }
                        });
                    }
                }
                LogManager.printErrorLog("online", GsonUtils.getInstance().toJson(stringBaseEntity));

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
