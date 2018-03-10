package com.bit.communityProperty.activity.faultManager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.bean.FaultDetailBean;
import com.bit.communityProperty.activity.faultManager.bean.FaultManagementBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.FaultManagerCommonBean;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.StarsView.StarsView;
import com.bit.communityProperty.view.TitleBarView;
import com.bit.communityProperty.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 故障详情页面
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultDetailsActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
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
    private Button btCancel;//取消申报
    private Button btConfirm;//确认受理


    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_details;
    }

    @Override
    public void initViewData() {
        this.faultID = getIntent().getStringExtra("FaultID");
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
        btCancel.setOnClickListener(new MyOnClickListener());
        btConfirm.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化Date
     */
    private void initDate() {
        getFaultDetail(faultID);
        initGridView();
    }

    /**
     * 初始化网格布局：图片评论
     */
    private void initGridView() {
        mAdapter = new CommonAdapter<HomeMenuBean>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, HomeMenuBean homeMenuBean, final int position, View convertView) {
                holder.setImageResource(R.id.iv_icon, homeMenuBean.getmImageID());
                holder.setOnClickListener(R.id.grid_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (position) {
                            case 0://第一个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 1://第二个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 2://第三个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 3://第四个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        };
        noScrollGridView.setAdapter(mAdapter);
        List<HomeMenuBean> mainWorkBeanList = new ArrayList<>();
        HomeMenuBean bean = new HomeMenuBean();//暂时用这个bean类
        bean.setmImageID(R.mipmap.add);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mAdapter.setDatas(mainWorkBeanList);
    }

    /**
     * 网络请求获取故障详情
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
                if (baseEntity.getData() != null) {
                    mFaultDetailBean = baseEntity.getData();
                    setViewDate(mFaultDetailBean);
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
        //故障状态 0：已取消；1：待接受；2：待分派；3：待检修；4：已完成；-1：已驳回；
        if (mFaultDetailBean.getFaultStatus() == 0) {
            tvStatus.setText("已取消");
        } else if (mFaultDetailBean.getFaultStatus() == 1) {
            tvStatus.setText("待接受");
        } else if (mFaultDetailBean.getFaultStatus() == 2) {
            tvStatus.setText("待分派");
        } else if (mFaultDetailBean.getFaultStatus() == 3) {
            tvStatus.setText("待检修");
        } else if (mFaultDetailBean.getFaultStatus() == 4) {
            tvStatus.setText("已完成");
        } else if (mFaultDetailBean.getFaultStatus() == -1) {
            tvStatus.setText("已驳回");
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

    }


    /**
     * 监听事件
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_cancel://驳回申请
                    break;
                case R.id.bt_confirm://确认受理
                    break;
                default:
                    break;
            }
        }
    }
}
