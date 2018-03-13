package com.bit.communityProperty.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bit.communityProperty.Bluetooth.util.BluetoothNetUtils;
import com.bit.communityProperty.Bluetooth.yunduijiang.YunDuiJIangUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.HouseholdManagementActivity;
import com.bit.communityProperty.activity.newsdetail.NewsDetailActivity;
import com.bit.communityProperty.activity.safetywarning.SafeWarningListActivity;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.AppVersionInfo;
import com.bit.communityProperty.bean.CardListBean;
import com.bit.communityProperty.bean.StoreDoorMILiBeanList;
import com.bit.communityProperty.bean.StoreElevatorListBeans;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.fragment.main.MainMineFragment;
import com.bit.communityProperty.fragment.main.MainNewsFragment;
import com.bit.communityProperty.fragment.main.MainWorkFragment;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.JPushBean;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.AppUtil;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.DownloadUtils;
import com.bit.communityProperty.utils.LiteOrmUtil;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.PermissionUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.UploadInfo;
import com.bit.communityProperty.view.TabItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity {


    private static final int REQUEST_CODE_STORAGE = 5;
    private static final int REQUEST_CODE_LOCATION = 6;
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.main_tab_contents)
    FrameLayout mainTabContents;
    @BindView(R.id.tab_bar_content)
    FrameLayout tabBarContent;
    @BindView(R.id.main_tabhost)
    FragmentTabHost mainTabhost;
    private List<TabItem> mTabItemList;
    private YunDuiJIangUtils yunDuiJIangUtils;

    private AppVersionInfo versionInfo;

    private String[] locPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入权限
            Manifest.permission.CAMERA,//相机
            Manifest.permission.CALL_PHONE,//电话
            Manifest.permission.RECORD_AUDIO//录音
    };

    private String downloadUrl;

    private String ROLE_TYPE;
    private BluetoothNetUtils bluetoothNetUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewData() {
        initTabData();
        initTabHost();
//        initOssToken();
        MyApplication.getInstance().getBlueToothApp().openBluetooth();
        //从通知点击启动
        Bundle bundle = getIntent().getBundleExtra(AppConfig.EXTRA_BUNDLE);
        if (bundle != null) {
            JPushBean jPushBean = (JPushBean) bundle.getSerializable("jpushbean");
            if (jPushBean != null) {
                switch (jPushBean.getAction()) {
                    case "100301"://一键报警
                        startActivity(new Intent(this, SafeWarningListActivity.class).putExtra("jpushbean", jPushBean));
                        break;
                    case "100101"://社区公告
                        startActivity(new Intent(this, NewsDetailActivity.class).putExtra("id", jPushBean.getData().getNotice_id()));
                        break;
                    case "100401"://房屋认证
                        startActivity(new Intent(this, HouseholdManagementActivity.class).putExtra("id", jPushBean.getData().getCommunityId()));
                        break;
                }
            }
        }
        yunDuiJIangUtils = new YunDuiJIangUtils(this);
        yunDuiJIangUtils.login("13500000000", "123456");
        //yunDuiJIangUtils.login("13774567405", "123456");
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {
            initLoc();
        }
        getVersion();

        getCardList();//获取虚拟卡
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof String){
                    if (o.equals("update_card_list")){
                        getCardList();
                    }
                }
            }
        });

//        createAccountId();//测试云信
        upBlueToothDate();
    }

    /**
     * 更新蓝牙数据
     */
    public void upBlueToothDate(){
        miLiUpDate();
        upElevatorDate();
    }
    /**
     * 米粒的数据更新缓存
     */
    private void miLiUpDate() {
        if (bluetoothNetUtils == null) {
            bluetoothNetUtils = new BluetoothNetUtils();
        }
        StoreDoorMILiBeanList doorMILiBeans = bluetoothNetUtils.getBletoothDoorDate();
        if (doorMILiBeans != null) {
            if (doorMILiBeans.isTimeOutNow()) {
                bluetoothNetUtils.getMiLiNetDate(null, 2, null);
            }
        } else {
            bluetoothNetUtils.getMiLiNetDate(null, 2, null);
        }
    }

    /**
     * 电梯蓝牙的数据更新缓存
     */
    private void upElevatorDate() {
        if (bluetoothNetUtils == null) {
            bluetoothNetUtils = new BluetoothNetUtils();
        }
        StoreElevatorListBeans bletoothElevateDate = bluetoothNetUtils.getBletoothElevateDate();
        if (bletoothElevateDate != null) {
            if (bletoothElevateDate.isTimeOutNow()) {
                bluetoothNetUtils.getBluetoothElevatorDate(2, null);
            }
        } else {
            bluetoothNetUtils.getBluetoothElevatorDate( 2, null);
        }
    }

    private void getCardList(){
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", AppConfig.COMMUNITYID);
        map.put("userId", SPUtil.get(this, AppConfig.id, ""));
        RetrofitManage.getInstance().subscribe(Api.getInstance().getCardList(map), new Observer<BaseEntity<CardListBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<CardListBean> objectBaseEntity) {
                if (objectBaseEntity.isSuccess()){
//                    LiteOrmUtil.getInstance().getOrm().delete(CardListBean.class);
                    if (objectBaseEntity.getData()!=null){
                        LiteOrmUtil.getInstance().getOrm().save(objectBaseEntity.getData().getRecords());
                    }
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

//    private void createAccountId() {
//        RequestParams requestParams = new RequestParams();
//        requestParams.addHeader("AppKey", "c7d64ed61462dfac25c0089ab171eaa4");
//        requestParams.addHeader("Nonce", "123456");
//        String curTime = String.valueOf((new Date()).getTime() / 1000L);
//        requestParams.addHeader("CurTime", curTime);
//        requestParams.addHeader("CheckSum", CheckSumBuilder.getCheckSum("744182fbc16c", "123456", curTime));
//        requestParams.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        requestParams.addBodyParameter("accid", (String) SPUtil.get(mContext, AppConfig.phone, ""));
////        requestParams.addBodyParameter("accid", "15900020005");
//
////        String url = "https://api.netease.im/nimserver/user/create.action";//{"desc":"already register","code":414}
//        String url = "https://api.netease.im/nimserver/user/refreshToken.action";
////        ApiRequester.sendRequest(url0, requestParams, mResponseCallBack);
//        ApiRequester.sendRequest(url, requestParams, mResponseCallBack);
//    }
//
//
//    //{"code":200,"info":{"token":"338fdb41436631cd5ced4d73950154d1","accid":"15900010001"}}
//    ResponseCallBack mResponseCallBack = new ResponseCallBack<IMToken>(false) {
//
//        @Override
//        public void onSuccess(IMToken data) {
////            Toast.makeText(mContext, "onSuccess", Toast.LENGTH_SHORT).show();
//            NimUIKit.login(new LoginInfo(data.getInfo().getAccid(), data.getInfo().getToken()), new RequestCallback<LoginInfo>() {
//                @Override
//                public void onSuccess(LoginInfo param) {
////                    Toast.makeText(mContext, "login im onSuccess", Toast.LENGTH_SHORT).show();
//                    LogUtil.d(TAG, "login im onSuccess");
//                }
//
//                @Override
//                public void onFailed(int code) {
//                    LogUtil.d(TAG, "onFailed:" + code);
//                }
//
//                @Override
//                public void onException(Throwable exception) {
//                    LogUtil.d(TAG, "onException:" + exception.getMessage());
//                }
//            });

//            NIMClient.getService(AuthService.class).login(new LoginInfo(data.getInfo().getAccid(), data.getInfo().getToken()))
//                    .setCallback(new RequestCallback<LoginInfo>() {
//                        @Override
//                        public void onSuccess(LoginInfo o) {
//                            showToast("login onSuccess");
//                        }
//
//                        @Override
//                        public void onFailed(int i) {
//                            showToast("login onFailed");
//                        }
//
//                        @Override
//                        public void onException(Throwable throwable) {
//                            showToast(throwable.getMessage());
//                        }
//                    });
//        }
//
//        @Override
//        public void onFailure(ServiceException e) {
//            LogUtil.d(TAG, e.getMsg());
//        }
//    };

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void initOssToken() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().ossToken(), new Observer<BaseEntity<UploadInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<UploadInfo> uploadInfoBaseEntity) {
                if (uploadInfoBaseEntity.isSuccess()) {
                    UploadInfo uploadInfo = uploadInfoBaseEntity.getData();
                    if (uploadInfo != null) {
                        OssManager.getInstance().init(mContext, uploadInfo);
                    }
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

    //检测版本更新
    private void getVersion() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().getVersion("5a961fc80cf2c1914073ded2", AppUtil.getVersionName(this)), new
                Observer<BaseEntity<AppVersionInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseEntity<AppVersionInfo> appVersionInfoBaseEntity) {
                        if (appVersionInfoBaseEntity.isSuccess()) {
                            versionInfo = appVersionInfoBaseEntity.getData();
                            if (versionInfo != null) {
                                downloadUrl = OssManager.getInstance().getUrl(versionInfo.getUrl());
                                if (versionInfo.isForceUpgrade()) {
                                    DialogUtil.showConfirmDialog(mContext, "版本更新", "发现新版本" + versionInfo.getSequence() + ",请下载更新.", false, new View
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (Build.VERSION.SDK_INT >= 23) {
                                                requestPermission();
                                            } else {
                                                downloadApk();
                                            }
                                            DialogUtil.dissmiss();
                                        }
                                    });
                                } else {
                                    DialogUtil.showConfirmDialog(mContext, "版本更新", "发现新版本" + versionInfo.getSequence() + ",是否更新？", true, new View
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (Build.VERSION.SDK_INT >= 23) {
                                                requestPermission();
                                            } else {
                                                downloadApk();
                                            }
                                            DialogUtil.dissmiss();
                                        }
                                    });
                                }
                            }
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


    private void initTabData() {
        mTabItemList = new ArrayList();
        String[] mTabTitle = new String[]{"消息", "工作", "我的"};
        mTabItemList.add(new TabItem(this, R.mipmap.icon_home_normal, R.mipmap.icon_home_selected, mTabTitle[0], MainNewsFragment.class));
        mTabItemList.add(new TabItem(this, R.mipmap.icon_door_normal, R.mipmap.icon_door_selected, mTabTitle[1], MainWorkFragment.class));
        mTabItemList.add(new TabItem(this, R.mipmap.icon_user_normal, R.mipmap.icon_user_selected, mTabTitle[2], MainMineFragment.class));
    }

    private void initTabHost() {
        if (getIntent().getStringExtra(AppConfig.ROLE_TYPE)==null){
            ROLE_TYPE = (String) SPUtil.get(mContext, AppConfig.ROLE_TYPE, AppConfig.ROLE_MANAGER);
        }else{
            ROLE_TYPE = getIntent().getStringExtra(AppConfig.ROLE_TYPE);
        }
        mainTabhost.setup(this, getSupportFragmentManager(), R.id.main_tab_contents);
        mainTabhost.getTabWidget().setDividerDrawable(null);
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.ROLE_TYPE,ROLE_TYPE);
        for (int i = 0; i < mTabItemList.size(); i++) {
            TabItem tabItem = mTabItemList.get(i);
            //实例化一个TabSpec,设置tab的名称和视图
            TabHost.TabSpec tabSpec = mainTabhost.newTabSpec(tabItem.getTitleString()).setIndicator(tabItem.getView(i));
            mainTabhost.addTab(tabSpec, tabItem.getFragmentClass(), bundle);

            //默认选中第一个tab
            if (i == 1) {
                tabItem.setChecked(true);
            }
        }
        mainTabhost.setCurrentTab(1);

        mainTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //重置Tab样式
                for (int i = 0; i < mTabItemList.size(); i++) {
                    TabItem tabitem = mTabItemList.get(i);
                    if (tabId.equals(tabitem.getTitleString())) {
                        tabitem.setChecked(true);
                    } else {
                        tabitem.setChecked(false);
                    }
                }
                upBlueToothDate();
            }
        });
    }

    private boolean mIsExit;
    Toast toast = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                toast.cancel();
              //  MyApplication.getInstance().getBlueToothApp().closeBluetooth();
                MyApplication.getInstance().exitApp();
            } else {
                if (toast == null) {
                    toast = Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT);
                }
                toast.show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != toast)
            toast.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        yunDuiJIangUtils.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        yunDuiJIangUtils.onPause();
    }

    private void downloadApk() {
        if (versionInfo == null || TextUtils.isEmpty(downloadUrl)) {
            ToastUtil.showShort("获取下载链接失败");
            return;
        }
        DownloadUtils downloadUtils = new DownloadUtils(mContext);
        //http://183.240.119.164/imtt.dd.qq.com/16891/0AA5EAE67C378051755E7646A493F822.apk?mkey=5a9606732937e28d&f=b24&c=0&fsname=com.snda
        // .wifilocating_4.2.58_3188.apk&csr=1bbd&p=.apk
        downloadUtils.download(downloadUrl,
                AppUtil.getAppName(mContext) + versionInfo.getSequence() + ".apk");
    }

    // 自定义申请一个权限
    private void requestPermission() {
        PermissionUtils.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        downloadApk();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.requestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE);
                    }
                });
    }

    private void requestPermissions() {
        PermissionUtils.checkMorePermissions(mContext, locPermissions,
                new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        initLoc();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                    }
                });
    }

    /**
     * 解释权限的dialog
     */
    private void showExplainDialog(String[] permission, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(mContext)
                .setTitle("申请权限")
                .setMessage("APP需要相关的权限才能正常运行")
                .setPositiveButton("确定", onClickListener)
                .show();
    }

    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    downloadApk();
                } else {
                    Toast.makeText(mContext, "获取读写SD卡权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_LOCATION:
                PermissionUtils.onRequestMorePermissionsResult(mContext, permissions, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        initLoc();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        showToAppSettingDialog();
                    }
                });
                break;
        }
    }

    private void initLoc() {
        //声明AMapLocationClient类对象
        AMapLocationClient mLocationClient = null;
        //声明定位回调监听器
        mLocationClient = new AMapLocationClient(MyApplication.getInstance());
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)
        // 接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        //异步获取定位结果
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        if (amapLocation.getCity() != null) {
                            LogManager.i(amapLocation.getCity());
                            SPUtil.put(mContext, AppConfig.CITY, amapLocation.getCity());
                            RxBus.get().post("location");
                        }
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }
}
