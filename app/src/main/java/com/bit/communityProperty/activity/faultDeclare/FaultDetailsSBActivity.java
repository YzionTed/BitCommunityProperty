package com.bit.communityProperty.activity.faultDeclare;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.bean.FaultDetailBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.StarsView.StarsView;
import com.bit.communityProperty.view.TitleBarView;
import com.bit.communityProperty.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 故障详情页面(故障申报详情)
 * Created by kezhangzhao on 2018/3/13.
 */

public class FaultDetailsSBActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private PromptDialog mPromptDialog;//弹窗
    private String faultID;
    private ArrayList<HomeMenuBean> mHomeMenuBeanList = new ArrayList<>();
    private NoScrollGridView noScrollGridView;//评论图片布局
    private StarsView mStarsView;//五星评论自定义view
    private CommonAdapter mAdapter;
    private FaultDetailBean mFaultDetailBean;//故障详情
    private TextView tvType;//故障类型 1：住户；2：公共；
    private TextView tvReason;//故障原因种类 1：水电煤气；2：房屋结构；3：消防安防；9：其它；10：电梯；11：门禁；99：其它；
    private TextView tvStatus;//故障状态 0：已取消；1：待接受；2：待分派；3：待检修；4：已完成；-1：已驳回；
    private TextView tvName;//申报人
    private TextView tvPhone;//申报人电话
    private TextView tvTime;//申报时间
    private TextView tvAddress;//申报地址
    private TextView tvFaultDescribe;//故障描述
    private TextView tvRejectInfo;//驳回理由
    private TextView tvRepairUser;//维修人员
    private TextView tvRepairPhone;//维修人员电话
    private TextView tvEvaluate;//评价
    private LinearLayout layoutReject;//拒绝理由布局，只有被驳回的申报才显示这个布局
    private LinearLayout layoutRepair;//维修人员信息布局，指派后才显示这个布局
    private LinearLayout layoutEvaluate;//评价布局，评价后才显示这个布局
    private Button btCancel;//驳回申请
    private Button btConfirm;//确认受理
    private Button btAssign;//确认分派
    /**
     * TYPE = 0：故障申报进来的，1：是故障管理进来的
     */
    private int TYPE = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_details;
    }

    @Override
    public void initViewData() {
        this.faultID = getIntent().getStringExtra("FaultID");
        this.TYPE = getIntent().getIntExtra("TYPE", 0);
        initView();
        initDate();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("故障详情");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noScrollGridView = findViewById(R.id.ns_grid);
        mStarsView = findViewById(R.id.view_stars);
//        mStarsView.setmStarsNum(0, 5);//设置黄色星星和灰色星星数量
//        mStarsView.setPadding(3, 3, 3, 3);//设置单个星星的padding

        tvType = findViewById(R.id.tv_type);
        tvReason = findViewById(R.id.tv_reason);
        tvStatus = findViewById(R.id.tv_status);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvTime = findViewById(R.id.tv_time);
        tvAddress = findViewById(R.id.tv_address);
        tvFaultDescribe = findViewById(R.id.tv_fault_describe);
        tvRejectInfo = findViewById(R.id.tv_reject_info);
        tvRepairUser = findViewById(R.id.tv_repair_user);
        tvRepairPhone = findViewById(R.id.tv_repair_phone);
        tvEvaluate = findViewById(R.id.tv_evaluate);
        btCancel = findViewById(R.id.bt_cancel);
        btConfirm = findViewById(R.id.bt_confirm);
        btAssign = findViewById(R.id.bt_assign);
        layoutReject = findViewById(R.id.layout_reject);
        layoutRepair = findViewById(R.id.layout_repair);
        layoutEvaluate = findViewById(R.id.layout_evaluate);
        btCancel.setOnClickListener(new MyOnClickListener());
        btConfirm.setOnClickListener(new MyOnClickListener());
        btAssign.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化Date
     */
    private void initDate() {
        getFaultDetail(faultID);
        initGridView();
        mPromptDialog = new PromptDialog((Activity) mContext);
    }

    /**
     * 初始化网格布局：图片评论
     */
    private void initGridView() {
        mAdapter = new CommonAdapter<HomeMenuBean>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, HomeMenuBean homeMenuBean, final int position, View convertView) {
                String url = OssManager.getInstance().getUrl(homeMenuBean.getmImageUrl());
                holder.setImageResource(R.id.iv_icon, url);
            }
        };
        noScrollGridView.setAdapter(mAdapter);
    }

    /**
     * 网络请求获取故障详情
     *
     * @param faultID 故障ID
     */
    private void getFaultDetail(String faultID) {
        RetrofitManage.getInstance().subscribe(Api.getInstance().getFaultDetail(faultID), new Observer<BaseEntity<FaultDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<FaultDetailBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.isSuccess() && baseEntity.getData() != null) {
                    mPromptDialog.dismiss();
                    mFaultDetailBean = baseEntity.getData();
                    setViewDate(mFaultDetailBean);
                } else {
                    mPromptDialog.showError(baseEntity.getErrorMsg());
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
     * 删除故障申报单
     *
     * @param faultID
     */
    private void deleteFault(String faultID) {
        RetrofitManage.getInstance().subscribe(Api.getInstance().deleteFault(faultID), new Observer<BaseEntity<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<String> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.isSuccess()&&baseEntity.getData() != null) {
                    finish();
//                    mFaultDetailBean = baseEntity.getData();
//                    setViewDate(mFaultDetailBean);
                }else {
                    mPromptDialog.showError(baseEntity.getErrorMsg());
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
     * 将信息设置到页面中去
     *
     * @param mFaultDetailBean 故障详情bean
     */
    private void setViewDate(FaultDetailBean mFaultDetailBean) {
        if (mFaultDetailBean.getFaultType() == 1) {//故障类型 1：住户；2：公共；
            tvType.setText("住户");
        } else {
            tvType.setText("公共");
        }
        //故障原因种类 1：水电煤气；2：房屋结构；3：消防安防；9：其它；10：电梯；11：门禁；99：其它；
        if (mFaultDetailBean.getFaultItem() == 1) {
            tvReason.setText("水电煤气");
        } else if (mFaultDetailBean.getFaultItem() == 2) {
            tvReason.setText("房屋结构");
        } else if (mFaultDetailBean.getFaultItem() == 3) {
            tvReason.setText("消防安防");
        } else if (mFaultDetailBean.getFaultItem() == 9) {
            tvReason.setText("其它");
        } else if (mFaultDetailBean.getFaultItem() == 10) {
            tvReason.setText("电梯");
        } else if (mFaultDetailBean.getFaultItem() == 11) {
            tvReason.setText("门禁");
        } else {
            tvReason.setText("其它");
        }
        //故障状态 0：已取消；1：待受理；2：待分派；3：待检修；4：已完成；-1：已驳回；
        if (mFaultDetailBean.getFaultStatus() == 0) {
            tvStatus.setText("已取消");
            btCancel.setText("删除订单");
            btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
            btAssign.setVisibility(View.GONE);//隐藏确认分派按钮
            btCancel.setVisibility(View.VISIBLE);//显示驳回申请按钮（删除订单）
        } else if (mFaultDetailBean.getFaultStatus() == 1) {
            tvStatus.setText("待受理");
            btCancel.setText("取消申报");
            btCancel.setVisibility(View.VISIBLE);//显示：取消申请按钮
            btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
            btAssign.setVisibility(View.GONE);//隐藏确认分派按钮
        } else if (mFaultDetailBean.getFaultStatus() == 2) {
            tvStatus.setText("待分派");
            btCancel.setVisibility(View.GONE);//隐藏驳回申请按钮
            btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
            btAssign.setVisibility(View.GONE);//显示确认分派按钮
        } else if (mFaultDetailBean.getFaultStatus() == 3) {
            tvStatus.setText("待检修");
            btCancel.setVisibility(View.GONE);//隐藏驳回申请按钮
            btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
            btAssign.setVisibility(View.GONE);//隐藏确认分派按钮
            layoutRepair.setVisibility(View.VISIBLE);//维修人员信息布局
        } else if (mFaultDetailBean.getFaultStatus() == 4) {
            if (mFaultDetailBean.getEvaluate() == 0) {//未评价
                tvStatus.setText("待评价");
                btConfirm.setText("评价订单");
                btConfirm.setVisibility(View.VISIBLE);//隐藏确认受理按钮（评价订单）
                btCancel.setVisibility(View.GONE);//隐藏驳回申请按钮
                layoutEvaluate.setVisibility(View.GONE);//评价布局
            } else {//已经评价
                tvStatus.setText("已完成");
                btCancel.setText("删除订单");
                btCancel.setVisibility(View.VISIBLE);//显示驳回申请按钮（删除订单）
                btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
                layoutEvaluate.setVisibility(View.VISIBLE);//评价布局
            }
            btAssign.setVisibility(View.GONE);//隐藏确认分派按钮
            layoutRepair.setVisibility(View.VISIBLE);//维修人员信息布局
        } else if (mFaultDetailBean.getFaultStatus() == -1) {
            tvStatus.setText("已驳回");
            btCancel.setText("删除订单");
            btCancel.setVisibility(View.VISIBLE);//显示驳回申请按钮（删除订单）
            btConfirm.setVisibility(View.GONE);//隐藏确认受理按钮
            btAssign.setVisibility(View.GONE);//隐藏确认分派按钮
            layoutReject.setVisibility(View.VISIBLE);//驳回理由布局
        }
        tvName.setText(mFaultDetailBean.getUserName());
        tvPhone.setText(mFaultDetailBean.getContact());
        tvTime.setText(TimeUtils.stampToDateWithHms(mFaultDetailBean.getPlayTime()));
        tvAddress.setText(mFaultDetailBean.getFaultAddress());
        tvFaultDescribe.setText(mFaultDetailBean.getFaultContent());
        tvRejectInfo.setText(mFaultDetailBean.getRejectReason());
        tvRepairUser.setText(mFaultDetailBean.getRepairName());
        tvRepairPhone.setText(mFaultDetailBean.getRepairContact());
        tvEvaluate.setText(mFaultDetailBean.getEvaluation());
        mStarsView.setmStarsNum(mFaultDetailBean.getEvaluationGrade(), 5);//设置黄色星星和灰色星星数量
        mStarsView.setPadding(3, 3, 3, 3);//设置单个星星的padding
        if (mFaultDetailBean.getFaultAccessory() != null) {
            List<HomeMenuBean> mainWorkBeanList = new ArrayList<>();
            for (int i = 0; i < mFaultDetailBean.getFaultAccessory().size(); i++) {
                HomeMenuBean bean = new HomeMenuBean();//暂时用这个bean类
                bean.setmImageUrl(mFaultDetailBean.getFaultAccessory().get(i));
                mainWorkBeanList.add(bean);
            }
            mAdapter.setDatas(mainWorkBeanList);
        }
    }


    /**
     * 监听事件
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_cancel://取消申报、删除订单
                    if (mFaultDetailBean != null) {
                        if (mFaultDetailBean.getFaultStatus() == 1) {//取消申请
                            handleFault(mFaultDetailBean.getId(), 0);
                        } else {//删除订单
                            deleteFault(mFaultDetailBean.getId());
                        }
                    }
                    break;
                case R.id.bt_confirm://评价订单
                    if (mFaultDetailBean != null) {
                        Intent intent = new Intent(mContext, EvaluateActivity.class);
                        intent.putExtra("FaultID", mFaultDetailBean.getId());
                        startActivityForResult(intent, 1);

                    }
//                    if (mFaultDetailBean != null)
//                        handleFault(mFaultDetailBean.getId(), 2);
                    break;
                case R.id.bt_assign://确认分派
//                    if (mFaultDetailBean != null) {
//                        Intent intent = new Intent(mContext, AssignSelectActivity.class);
//                        intent.putExtra("Fault_Id", mFaultDetailBean.getId());
//                        startActivity(intent);
//                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String ID = data.getStringExtra("FaultID");
            getFaultDetail(ID);//从新获取申报单详情刷新页面
        }
    }

    /**
     * 网络请求处理故障单（驳回、受理）
     *
     * @param faultID     故障ID
     * @param faultStatus （0：已取消；1：待接受；2：待分派；3：待检修；4：已完成；-1：已驳回；）
     */
    private void handleFault(String faultID, final int faultStatus) {
        mPromptDialog.showLoading("正在取消中...");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", faultID);
        map.put("faultStatus", faultStatus);
        RetrofitManage.getInstance().subscribe(Api.getInstance().handleFault(map), new Observer<BaseEntity<FaultDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<FaultDetailBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.isSuccess() && baseEntity.getData() != null) {
                    mPromptDialog.showSuccess("已取消");
                    mFaultDetailBean = baseEntity.getData();
                    //重新设置页面的数据
                    setViewDate(mFaultDetailBean);
                } else {
                    mPromptDialog.showError(baseEntity.getErrorMsg());
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
