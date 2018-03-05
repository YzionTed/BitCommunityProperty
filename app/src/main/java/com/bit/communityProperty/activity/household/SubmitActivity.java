package com.bit.communityProperty.activity.household;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.activity.safetywarning.SafeWarningReportActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseSubmitActivity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.TitleBarView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 驳回理由提交页面
 * Created by kezhangzhao on 2018/2/28.
 */

public class SubmitActivity extends BaseSubmitActivity{

    private String id ;
    private int auditStatus;

    //不用重新这个方法，因为默认值就是200
//    @Override
//    protected void setMaxLines(EditText et, TextView tv) {
//        et.setMaxLines(200);
//        maxLines=200;
//        tv.setText("0/"+maxLines);
//    }


    /**
     * 设置编辑框的默认文字
     * @param et EditText
     */
    @Override
    protected void setEtInputHint(EditText et) {
        super.setEtInputHint(et);
        et.setHint("请输入驳回理由");
    }


    /**
     * 点击右边的完成按钮触发事件
     */
    @Override
    protected void submitData() {
        id = getIntent().getStringExtra("ID");
        auditStatus = getIntent().getIntExtra("AuditStatus",-1);
        auditingHouseUser(id,auditStatus,etInput.getText().toString());
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
                    finish();
                    RxBus.get().post("finish_house");
                    Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
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
}
