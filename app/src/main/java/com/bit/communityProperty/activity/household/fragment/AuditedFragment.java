package com.bit.communityProperty.activity.household.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.adapter.AuditedExpandableListAdapter;
import com.bit.communityProperty.activity.household.bean.AuditedBean;
import com.bit.communityProperty.activity.household.bean.AuditedUserBean;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 已审核的fragment
 * Created by kezhangzhao on 2018/2/8.
 */

public class AuditedFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<AuditingBean> mAuditingBeanList = new ArrayList<>();//数据列表
    private Context mContext;
    private ExpandableListView mExpandableListView;//可展开的listview
    private AuditedExpandableListAdapter adapter;//可展开listview的adapter
    public List<AuditedBean> groupList = new ArrayList();
    public List<List<AuditedUserBean.RecordsBean>> childList = new ArrayList();
    private ArrayList<Boolean> isGetOverData = new ArrayList<>();//是否获取过展开的数据

    private String communityId = AppConfig.COMMUNITYID;

    //    public String[] groupStrings= {"Group1", "Group2", "Group3", "Group4", "Group5", "Group6", "Group7",
//            "Group8","Group9", "Group10", "Group11", "Group12"};
//    public String[][] childStrings={ {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
//            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
//            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
//            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"} ,
//            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
//            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"}};
    public AuditedFragment() {
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void initView(Context context) {
        this.mContext = context;
    }


    public static AuditedFragment newInstance(int sectionNumber, Context context,String communityId) {
        AuditedFragment fragment = new AuditedFragment();
//        fragment.setContext(context);
        fragment.initView(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("id",communityId);
        fragment.setArguments(args);
        //add
        //add
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audited, container, false);
        mExpandableListView = rootView.findViewById(R.id.expandableListView);
//        mExpandableListView.setGroupIndicator(null);
        communityId = getArguments().getString("id", AppConfig.COMMUNITYID);
        getHouseholdNum(communityId);
//        adapter = new AuditedExpandableListAdapter(mContext,groupList,childList);
//        mExpandableListView.setAdapter(adapter);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        //分组的点击事件
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, final int groupPosition, long id) {
                //如果分组被展开 直接关闭
                if (mExpandableListView.isGroupExpanded(groupPosition)) {
                    mExpandableListView.collapseGroup(groupPosition);
                } else {//点击展开
                    //判断是否访问过网络数据
                    if (!isGetOverData.get(groupPosition)) {//没有展开过，网络访问数据
                        isGetOverData.set(groupPosition, true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();
                                bundle.putInt("SELECT_GROUP_POISITION", groupPosition);
                                bundle.putString("BUILDING_ID", groupList.get(groupPosition).getBuildingEntity().get(0).get_id());
                                msg.what = 1;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }).start();
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message msg = handler.obtainMessage();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("SELECT_GROUP_POISITION",groupPosition);
//                            bundle.putString("BUILDING_ID",groupList.get(groupPosition).getBuildingEntity().get(0).get_id());
//                            msg.what = 1;
//                            msg.setData(bundle);
//                            handler.sendMessage(msg);
//                        }
//                    }, 200);
                    } else {
                        mExpandableListView.expandGroup(groupPosition, true);
                    }
                }
                return true;
            }
        });

//        RxBus.get().toObservable().subscribe(new Consumer<Object>() {
//
//            @Override
//            public void accept(Object o) throws Exception {
//                if (o instanceof String) {
//                    if (o != null && o.equals("update_house")) {
//
//                    }
//                }
//            }
//        });
        return rootView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://网络访问展开的子数据
                    if (msg.getData() != null)
                        getUsersByBuildingId(msg.getData().getInt("SELECT_GROUP_POISITION"),
                                msg.getData().getString("BUILDING_ID"),
                                1,
                                1,
                                1,
                                500);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 根据社区统计各楼宇有效业主数量
     *
     * @param communityId 社区ID=5a82adf3b06c97e0cd6c0f3d
     */
    private void getHouseholdNum(String communityId) {
        String url = "v1/user/" + communityId + "/proprietors-statistics";
        RetrofitManage.getInstance().subscribe(Api.getInstance().getHouseholdNum(url), new Observer<BaseEntity<ArrayList<AuditedBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ArrayList<AuditedBean>> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess() && BaseEntity.getData() != null) {
                    groupList = BaseEntity.getData();
                    for (int i = 0; i < groupList.size(); i++) {
                        isGetOverData.add(false);
                    }
                    adapter = new AuditedExpandableListAdapter(mContext, BaseEntity.getData(), childList);
                    mExpandableListView.setAdapter(adapter);
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

    /**
     * 按楼宇ID获取用户关系列表
     *
     * @param selectGroupPoisition 选择展开列表的Poisition
     * @param buildingId           楼宇ID
     * @param relationship         （1：业主；2：家属；3：租客）
     * @param auditStatus          （0：未审核；1：审核通过；-1：驳回；-2：违规; 2: 已注销; 3: 已解绑;）
     * @param page                 当前页数
     * @param size                 一页多少条数据
     */
    private void getUsersByBuildingId(final int selectGroupPoisition, String buildingId, int relationship, int auditStatus, int page, int size) {
        String url = "v1/user/" + buildingId + "/by-building-id";
        Map<String, Object> map = new HashMap<>();
        map.put("relationship", relationship);
        map.put("auditStatus", auditStatus);
        map.put("page", page);
        map.put("size", size);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getUsersByBuildingId(url, map), new Observer<BaseEntity<AuditedUserBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<AuditedUserBean> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess() && BaseEntity.getData() != null) {
                    List<AuditedUserBean.RecordsBean> beans = BaseEntity.getData().getRecords();
                    adapter.addChildData(selectGroupPoisition, BaseEntity.getData().getRecords());
                    adapter.notifyDataSetChanged();
                    mExpandableListView.expandGroup(selectGroupPoisition, true);
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


}
