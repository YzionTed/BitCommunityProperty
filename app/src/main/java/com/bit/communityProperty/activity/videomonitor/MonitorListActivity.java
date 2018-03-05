package com.bit.communityProperty.activity.videomonitor;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.videomonitor.adapter.MonitorListAdapter;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;


import butterknife.BindView;
import butterknife.OnClick;
import sdk.NETDEV_CLOUD_DEV_INFO_S;
import sdk.NetDEVSDK;

public class MonitorListActivity extends BaseActivity {

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
    @BindView(R.id.rlv)
    LRecyclerView rlv;

    private MonitorListAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private String Tag = "MonitorListActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_monitor_list;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView() {
        actionBarTitle.setText("视频监控");
        initMonitor();
        initDate();
    }

    public String strCloudServerUrl = "http://ezcloud.uniview.com";
    public String strCloudUserName = "Ib77w2";
    public String strCloudPassword = "bit@123456";

    /**
     * 初始化摄像头数据
     */
    private void initMonitor() {

        final NetDEVSDK oNetSDKDemo = new NetDEVSDK();
        oNetSDKDemo.NETDEV_Init();
        //登录宇视云端账户登录
        NetDEVSDK.glpcloudID = NetDEVSDK.NETDEV_LoginCloud(strCloudServerUrl, strCloudUserName, strCloudPassword);
        if (0 != NetDEVSDK.glpcloudID) {
            Log.e(Tag, "宇视云端账户登录成功");
            //搜索设备
            int dwFileHandle = NetDEVSDK.NETDEV_FindCloudDevList(NetDEVSDK.glpcloudID);
            if (dwFileHandle == 0) {
                Log.e(Tag, "搜索云端设备失败");
            }else {
                Log.e(Tag, "搜索云端设备成功");
                String[] szDevList = new String[10];
                int dwCount = 0;
                NETDEV_CLOUD_DEV_INFO_S stclouddeviceinfo = new NETDEV_CLOUD_DEV_INFO_S();
                for(int i = 0;i < 10; i++)
                {
                    String strMeg = "";
                    String strOut = "";
                    int iRet = NetDEVSDK.NETDEV_FindNextCloudDevInfo(dwFileHandle, stclouddeviceinfo);
                    if(0 == iRet)
                    {
                        break;
                    }
                    else
                    {
                        strMeg = "IP:" + stclouddeviceinfo.szIPAddr + "\n";
                        strOut += strMeg;
                        strMeg = "User Name:" + stclouddeviceinfo.szDevUserName + "\n";
                        strOut += strMeg;
                        strMeg = "Serial Num:" + stclouddeviceinfo.szDevSerialNum + "\n";
                        strOut += strMeg;
                        strMeg = "Dev Name:" + stclouddeviceinfo.szDevName + "\n";
                        strOut += strMeg;
                        strMeg = "Dev Model:" + stclouddeviceinfo.szDevModel + "\n";
                        strOut += strMeg;
                        strMeg = "Dev Port:" + String.valueOf(stclouddeviceinfo.dwDevPort) + "";
                        strOut += strMeg;
                        szDevList[dwCount++] = strOut;
                    }
                    Log.e(Tag, "搜索的设备"+strOut);
                }

            }

        } else {
            Log.e(Tag, "宇视云端账户登录失败");
        }
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        adapter = new MonitorListAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rlv.setAdapter(mLRecyclerViewAdapter);
        rlv.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(mContext)
                .setHeight(R.dimen.common_dp_10)
                .setColorResource(R.color.appbg)
                .build();
        rlv.addItemDecoration(divider);
        rlv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        rlv.setLoadMoreEnabled(false);
        rlv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(MonitorListActivity.this, RealMonitorActivity.class));
            }
        });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
