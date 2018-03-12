package com.bit.communityProperty.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.Zxing.CaptureActivity;
import com.bit.communityProperty.activity.access.DoorControlActivity;
import com.bit.communityProperty.activity.cleanclock.CleanClockListActivity;
import com.bit.communityProperty.activity.deviceManagement.DeviceManagementActivity;
import com.bit.communityProperty.activity.elevatorcontrol.ElevatorControlActivity;
import com.bit.communityProperty.activity.faultDeclare.FaultDeclareActivity;
import com.bit.communityProperty.activity.faultManager.FaultManagementActivity;
import com.bit.communityProperty.activity.household.HouseholdManagementActivity;
import com.bit.communityProperty.activity.propertyFee.PropertyFeeActivity;
import com.bit.communityProperty.activity.propertyFee.fragment.PropertyFeeFragment;
import com.bit.communityProperty.activity.repairwork.RepairWorkListActivity;
import com.bit.communityProperty.activity.safetywarning.SafeWarningListActivity;
import com.bit.communityProperty.activity.securityclock.SecurityClockListActivity;
import com.bit.communityProperty.activity.videomonitor.MonitorListActivity;
import com.bit.communityProperty.activity.workplan.PersonalWorkActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.bean.LoginData;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.fragment.main.bean.BannerBean;
import com.bit.communityProperty.fragment.main.bean.MainWorkBean;
import com.bit.communityProperty.fragment.main.bean.OwnerApplyNumBean;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.GlideUtils;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.widget.NoScrollGridView;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.NimUIKit;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
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

public class MainWorkFragment extends BaseFragment {


    @BindView(R.id.banner)
    MZBannerView banner;
    Unbinder unbinder;
    @BindView(R.id.ns_grid)
    NoScrollGridView nsGrid;
    @BindView(R.id.goHouseholdManagement)
    LinearLayout goHouseholdManagement;
    @BindView(R.id.goParkingManagement)
    LinearLayout goParkingManagement;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    @BindView(R.id.tv_owner_apply_num)
    TextView tv0wnerApplyNum;//业主申请数量显示
    private CommonAdapter mAdapter;

    /**
     * 管理人员
     * "小区门禁", "智能梯控", "工作排班","设备管理", "住户管理", "安防警报"
     */
    private String[] managerTitles = new String[]{AppConfig.Community_Access, AppConfig.Intelligent_Elevator, AppConfig.Work_Schedule,
            AppConfig.Device_Management, AppConfig.Household_Management, AppConfig.Security_Alarm,
            AppConfig.Fault_Management};
    private int[] managerImgs = new int[]{R.mipmap.ic_work_xqmj, R.mipmap.ic_work_zntk, R.mipmap.ic_work_gzpb,
            R.mipmap.ic_work_sbgl, R.mipmap.ic_work_zhgl, R.mipmap.ic_work_afjb, R.mipmap.ic_work_gzgl};

    /**
     * 保安门卫
     * "小区门禁", "智能梯控", "工作排班", "视频监控", "安防警报", "扫码放行", "巡逻打卡", "故障申报"
     */
    private String[] securityTitles = new String[]{AppConfig.Community_Access, AppConfig.Intelligent_Elevator, AppConfig.Work_Schedule
            , AppConfig.Video_Surveillance, AppConfig.Security_Alarm, AppConfig.Scanning_Release, AppConfig.Patrol_Punch, AppConfig.Fault_Reporting};
    private int[] securityImgs = new int[]{R.mipmap.ic_work_xqmj, R.mipmap.ic_work_zntk, R.mipmap.ic_work_gzpb, R.mipmap.ic_work_spjk,
            R.mipmap.ic_work_afjb, R.mipmap.ic_work_smfx, R.mipmap.ic_work_xldk, R.mipmap.ic_work_gzsb};

    /**
     * 保洁人员
     * "小区门禁", "智能梯控", "工作排班", "保洁打卡"
     */
    private String[] cleanerTitles = new String[]{AppConfig.Community_Access, AppConfig.Intelligent_Elevator,
            AppConfig.Work_Schedule, AppConfig.Punch_Cleaning};
    private int[] cleanerImgs = new int[]{R.mipmap.ic_work_xqmj, R.mipmap.ic_work_zntk, R.mipmap.ic_work_gzpb, R.mipmap.ic_work_bjdk};

    /**
     * 维修人员
     * "小区门禁", "智能梯控", "工作排班", "维修工单"
     */
    private String[] repairmanTitles = new String[]{AppConfig.Community_Access, AppConfig.Intelligent_Elevator,
            AppConfig.Work_Schedule, AppConfig.Repair_Orders};
    private int[] repairmanImgs = new int[]{R.mipmap.ic_work_xqmj, R.mipmap.ic_work_zntk, R.mipmap.ic_work_gzpb, R.mipmap.ic_work_wxgd};

    private String ROLE_TYPE;

    public static MainWorkFragment newInstance(String type) {
        MainWorkFragment fragment = new MainWorkFragment();
        Bundle args = new Bundle();
        args.putString(AppConfig.ROLE_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_work;
    }

    @Override
    protected void initViewAndData() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            ROLE_TYPE = bundle.getString(AppConfig.ROLE_TYPE);
        }else{
            ROLE_TYPE = (String) SPUtil.get(mContext, AppConfig.ROLE_TYPE, AppConfig.ROLE_MANAGER);
        }

        initBanner();
        initGridView();
        goHouseholdManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HouseholdManagementActivity.class));//暂时在这调试住户管理
            }
        });
        goParkingManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, ParkingManagementActivity.class));
            }
        });
        tvLocal.setText((String) SPUtil.get(mContext, AppConfig.CITY, "包头市"));
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof String) {
                    if (o != null && o.equals("location")) {
                        if ((String) SPUtil.get(mContext, AppConfig.CITY, "包头市") != null) {
                            tvLocal.setText((String) SPUtil.get(mContext, AppConfig.CITY, "包头市"));
                        }
                    }
                }else if (o instanceof LoginData){
                    ROLE_TYPE = ((LoginData) o).getRoles().get(0);
                    initTabData();
                }
            }
        });
    }

    private void initBanner() {
        Map<String, Object> map = new HashMap<>();
        RetrofitManage.getInstance().subscribe(Api.getInstance().getBanner(map), new Observer<BaseEntity<List<BannerBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<BannerBean>> bannerBeanBaseEntity) {
                if (bannerBeanBaseEntity.isSuccess()){
                    List<BannerBean> list = bannerBeanBaseEntity.getData();
                    banner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
                        @Override
                        public BannerViewHolder createViewHolder() {
                            return new BannerViewHolder();
                        }
                    });
                    banner.start();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

//        List<Integer> s = new ArrayList<>();
//        s.add(R.mipmap.banner_1);
//        s.add(R.mipmap.banner_2);
//        s.add(R.mipmap.banner_3);
        banner.setIndicatorVisible(false);
//        banner.setPages(s, new MZHolderCreator<BannerViewHolder>() {
//            @Override
//            public BannerViewHolder createViewHolder() {
//                return new BannerViewHolder();
//            }
//        });
//        banner.start();
    }

    private void initGridView() {
        mAdapter = new CommonAdapter<MainWorkBean>(mActivity, R.layout.item_main_work) {
            @Override
            public void convert(ViewHolder holder, final MainWorkBean bean, final int position, View convertView) {
                holder.setText(R.id.tv_title, bean.getTitle());
                holder.setImageResource(R.id.iv_img, bean.getImgRes());
                holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (bean.getTitle()) {
                            case AppConfig.Community_Access://小区门禁
                                startActivity(new Intent(mContext, DoorControlActivity.class));
                                break;
                            case AppConfig.Intelligent_Elevator://智能梯控
                                startActivity(new Intent(mContext, ElevatorControlActivity.class));
                                break;
                            case AppConfig.Work_Schedule://工作排班
                                startActivity(new Intent(mContext, PersonalWorkActivity.class));
                                break;
                            case AppConfig.Device_Management://设备管理
//                                startActivity(new Intent(mContext, FaultRepairActivity.class));//故障维修主页面
                                startActivity(new Intent(mContext, DeviceManagementActivity.class));//设备管理主页面
                                break;
                            case AppConfig.Household_Management://住户管理
                                startActivity(new Intent(mContext, HouseholdManagementActivity.class));//暂时在这调试住户管理
                                break;
                            case AppConfig.Fault_Management://故障管理
                                startActivity(new Intent(mContext, FaultManagementActivity.class));
                                break;
                            case AppConfig.Property_Management://物业费管理
//                                startActivity(new Intent(mContext, ParkingManagementActivity.class));//停车管理
                                startActivity(new Intent(mContext, PropertyFeeActivity.class));//物业费管理
                                break;
                            case AppConfig.Security_Alarm://安防警报
                                startActivity(new Intent(mContext, SafeWarningListActivity.class));
                                break;
                            case AppConfig.Video_Surveillance://视频监控
                                startActivity(new Intent(mContext, MonitorListActivity.class));
//                                startActivity(new Intent(mContext, IntelligentElevatorActivity.class));
                                break;
                            case AppConfig.Statistics://数据统计
                                ToastUtil.showShort("该功能暂未开放");
//                                startActivity(new Intent(mContext, PoliceClockActivity.class));
//                                startActivity(new Intent(mContext, HouseholdManagementActivity.class));//暂时在这调试住户管理
                                break;
                            case AppConfig.Online_Consultation://在线咨询
                                if (NimUIKit.getAccount() != null) {
//                                NimUIKit.startP2PSession(getContext(), (String) SPUtil.get(mContext, AppConfig.phone, ""));
                                    NimUIKit.startP2PSession(getContext(), "15900020005");
                                }
                                break;
                            case AppConfig.Patrol_Punch://巡逻打卡
                                startActivity(new Intent(mContext, SecurityClockListActivity.class));
                                break;
                            case AppConfig.Fault_Reporting://故障申报
                                startActivity(new Intent(mContext, FaultDeclareActivity.class));
                                break;
                            case AppConfig.Scanning_Release://条形扫码
                                startActivity(new Intent(mContext, CaptureActivity.class));
                                break;
                            case AppConfig.Punch_Cleaning://保洁打卡
                                startActivity(new Intent(mContext, CleanClockListActivity.class));
                                break;
                            case AppConfig.Repair_Orders://维修工单
                                startActivity(new Intent(mContext, RepairWorkListActivity.class));
                                break;
                        }
                    }
                });
            }
        };
        nsGrid.setAdapter(mAdapter);
        initTabData();
    }

    private void initTabData() {
        List<MainWorkBean> mainWorkBeanList = new ArrayList<>();
        switch (ROLE_TYPE) {
            case AppConfig.ROLE_MANAGER:
                for (int i = 0; i < managerTitles.length; i++) {
                    MainWorkBean bean = new MainWorkBean();
                    bean.setTitle(managerTitles[i]);
                    bean.setImgRes(managerImgs[i]);
                    mainWorkBeanList.add(bean);
                }
                break;
            case AppConfig.ROLE_SECURITY:
                for (int i = 0; i < securityTitles.length; i++) {
                    MainWorkBean bean = new MainWorkBean();
                    bean.setTitle(securityTitles[i]);
                    bean.setImgRes(securityImgs[i]);
                    mainWorkBeanList.add(bean);
                }
                break;
            case AppConfig.ROLE_CLEANER:
                for (int i = 0; i < cleanerTitles.length; i++) {
                    MainWorkBean bean = new MainWorkBean();
                    bean.setTitle(cleanerTitles[i]);
                    bean.setImgRes(cleanerImgs[i]);
                    mainWorkBeanList.add(bean);
                }
                break;
            case AppConfig.ROLE_REPAINMAN:
                for (int i = 0; i < repairmanTitles.length; i++) {
                    MainWorkBean bean = new MainWorkBean();
                    bean.setTitle(repairmanTitles[i]);
                    bean.setImgRes(repairmanImgs[i]);
                    mainWorkBeanList.add(bean);
                }
                break;
            default:
                for (int i = 0; i < managerTitles.length; i++) {
                    MainWorkBean bean = new MainWorkBean();
                    bean.setTitle(managerTitles[i]);
                    bean.setImgRes(managerImgs[i]);
                    mainWorkBeanList.add(bean);
                }
                break;
        }
        mAdapter.setDatas(mainWorkBeanList);
    }

    /**
     * 后台运行
     * 处理一些数据
     */
    private void doInBackstage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOwnerApplyNum("5a82adf3b06c97e0cd6c0f3d");
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getOwnerApplyNum("5a82adf3b06c97e0cd6c0f3d");
    }

    /**
     * 获取业主申请数量
     *
     * @param communityId 社区id 5a82adf3b06c97e0cd6c0f3d
     */
    private void getOwnerApplyNum(String communityId) {
        String url = "/v1/user/" + communityId + "/count-unreviewed-proprietors";
        RetrofitManage.getInstance().subscribe(Api.getInstance().getOwnerApplyNum(url), new Observer<BaseEntity<OwnerApplyNumBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<OwnerApplyNumBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity != null && baseEntity.isSuccess() && baseEntity.getData() != null) {
                    tv0wnerApplyNum.setText(baseEntity.getData().getTotal() + "");
                } else {
                    tv0wnerApplyNum.setText("0");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        banner.pause();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        doInBackstage();//后台运行获取一些数据
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public static class BannerViewHolder implements MZViewHolder<BannerBean> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerBean data) {
            // 数据绑定
//            mImageView.setImageResource(data);
            GlideUtils.loadImage(context,OssManager.getInstance().getUrl(data.getMaterialUrl()),mImageView);
        }
    }
}
