package com.bit.communityProperty.activity.household;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.activity.releasePass.bean.ReleasePassDetailsBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.TitleBarView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 住户详情、业主信息
 * Created by kezhangzhao on 2018/2/9.
 */

public class HouseholdInfoActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private AuditingBean.RecordsBean mRecordsBean;//用户详情类
    private TextView tvName;//名字
    private TextView tvGender;//性别
    private TextView tvDate;//出生年月
    private TextView tvPhone;//联系电话
    private TextView tvCardId;//身份证号
    private TextView tvHouseNum;//住房编号
    private TextView tvAgreementId;//合同编号
    private TextView tvStatus;//状态
    private Button btPass;//审核通过
    private Button btReject;//驳回申请

    @Override
    public int getLayoutId() {
        return R.layout.activity_household_info;
    }

    @Override
    public void initViewData() {
        mRecordsBean = (AuditingBean.RecordsBean) getIntent().getSerializableExtra("RecordsBean");
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("业主信息");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvName = findViewById(R.id.tv_name);
        tvGender = findViewById(R.id.tv_gender);
        tvDate = findViewById(R.id.tv_date);
        tvPhone = findViewById(R.id.tv_phone);
        tvCardId = findViewById(R.id.tv_card_id);
        tvHouseNum = findViewById(R.id.tv_house_num);
        tvAgreementId = findViewById(R.id.tv_agreement_id);
        tvStatus = findViewById(R.id.tv_status);
        btPass = findViewById(R.id.bt_pass);
        btReject = findViewById(R.id.bt_reject);
        btPass.setOnClickListener(new MyOnClickListener());
        btReject.setOnClickListener(new MyOnClickListener());

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mRecordsBean != null) {
            tvName.setText(mRecordsBean.getName());
//            tvGender.getText(mRecordsBean.getSex().toString());
            tvDate.setText(mRecordsBean.getBirthday());
            tvPhone.setText(mRecordsBean.getPhone());
            tvCardId.setText(mRecordsBean.getIdentityCard());
            tvHouseNum.setText(mRecordsBean.getRoomName());
            tvAgreementId.setText(mRecordsBean.getContract());
            //0未审核，1审核通过，-1驳回，-2违规
            if (mRecordsBean.getAuditStatus() == -1) {//只有被驳回才显示
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
            }
            if (mRecordsBean.getAuditStatus() == 0) {//未审核,显示审核通过按钮和驳回申请按钮
                btPass.setVisibility(View.VISIBLE);
                btReject.setVisibility(View.VISIBLE);
            } else {
                btPass.setVisibility(View.GONE);
                btReject.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 审核房屋认证
     *
     * @param id          id
     * @param auditStatus 要改变的状态（0：未审核；1：审核通过；-1：驳回；-2：违规; 2: 已注销; 3: 已解绑;）
     * @param remark      备注（非必传）
     */
    private void auditingHouseUser(String id, int auditStatus, String remark) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("auditStatus", auditStatus);
        if (!TextUtils.isEmpty(remark)) {
            map.put("remark", remark);
        }
        RetrofitManage.getInstance().subscribe(Api.getInstance().auditingHouseUser(map), new Observer<BaseEntity<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<String> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess()) {
                    Toast.makeText(mContext, "审核成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, BaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show();
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
     * 点击事件监听
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_pass://审核通过
                    auditingHouseUser(mRecordsBean.getId(), 1, null);
                    break;
                case R.id.bt_reject://驳回申请
                    Intent intent = new Intent(mContext, SubmitActivity.class);
                    intent.putExtra("ID", mRecordsBean.getId());
                    intent.putExtra("AuditStatus", -1);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
