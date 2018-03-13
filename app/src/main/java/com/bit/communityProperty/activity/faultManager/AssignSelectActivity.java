package com.bit.communityProperty.activity.faultManager;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.adapter.AssignSelectAdapter;
import com.bit.communityProperty.activity.faultManager.bean.AssignPersonBean;
import com.bit.communityProperty.activity.faultManager.bean.FaultDetailBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 选择维修人员分派页面
 * Created by kezhangzhao on 2018/3/12.
 */

public class AssignSelectActivity extends BaseActivity{

    private ListView mListView;//维修人员listview
    private TitleBarView mTitleBarView;//标题栏
    private AssignSelectAdapter adapter;
    private ArrayList<AssignPersonBean> personList = new ArrayList<>();
    private AssignPersonBean assignPersonBean;//维修人信息类
    private boolean isFirst=true;//是否第一次
    private int lastPosition = -1;//上一次选择的点
    private String faultId;//故障订单ID

    @Override
    public int getLayoutId() {
        return R.layout.activity_assign_select;
    }

    @Override
    public void initViewData() {
        faultId = getIntent().getStringExtra("Fault_Id");
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("维修人员");
        mTitleBarView.setRightText("确定");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastPosition!=-1){
                    assignPersonBean = (personList.get(lastPosition));
                    if (assignPersonBean!=null){
                        Intent intent = new Intent();
                        intent.putExtra("FaultId",faultId);
                        intent.putExtra("UserId",assignPersonBean.getUserId());
                        intent.putExtra("UserName",assignPersonBean.getUserName());
                        intent.putExtra("Phone",assignPersonBean.getPhone());
                        AssignSelectActivity.this.setResult(RESULT_OK,intent);
                        finish();
                    }
                }else {
                    ToastUtil.showTextShort(mContext,"请选择分派人员");
                }
            }
        });
        mListView = findViewById(R.id.lv_personnel);
        getPersonnelList("5a82adf3b06c97e0cd6c0f3d","SERVICEMAN");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isFirst){
                    adapter.setPositionGray(position,true);
                    adapter.notifyDataSetChanged();
                    lastPosition = position;
                    isFirst = false;
                }else {
                    adapter.setPositionGray(lastPosition,false);
                    adapter.setPositionGray(position,true);
                    lastPosition = position;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取维修故障人员列表
     * @param communityId 社区id 5a82adf3b06c97e0cd6c0f3d
     * @param postCode CM_ADMIN=社区管理员，MANAGER=物业管理员，SECURITY=保安，
     *                 CLEANER=保洁，SERVICEMAN=维修工，HOUSEHOLD=住户，SUPPORTSTAFF=客服人员；
     */
    private void getPersonnelList(String communityId,String postCode){
        Map<String,Object> map = new HashMap<>();
        map.put("postCode",postCode);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getPersonnelList(communityId,map), new Observer<BaseEntity<ArrayList<AssignPersonBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ArrayList<AssignPersonBean>> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.getData() != null) {
                    personList = baseEntity.getData();
                    adapter = new AssignSelectAdapter(mContext,personList);
                    mListView.setAdapter(adapter);
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
