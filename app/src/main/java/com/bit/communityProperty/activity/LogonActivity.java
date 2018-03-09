package com.bit.communityProperty.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.LoginData;
import com.bit.communityProperty.bean.MessageEvent;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.net.ThrowableUtils;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.PermissionUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.UploadInfo;
import com.bit.communityProperty.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 登录页面
 * Created by kezhangzhao on 2018/1/12.
 */

public class LogonActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private EditText etAccountNum, etPassword;//账号，密码
    private Button btnLogin;
    PromptDialog sinInLogin;
    private static final int REQUEST_CODE_STORAGE = 5;

    @Override
    public int getLayoutId() {
        return R.layout.activity_logon;
    }

    @Override
    public void initViewData() {
        SPUtil.clear(LogonActivity.this);
        sinInLogin = new PromptDialog(LogonActivity.this);
        MessageEvent event=new MessageEvent();
        event.setLoginSuccess(false);
        EventBus.getDefault().post(event);
        SPUtil.put(mContext, AppConfig.IS_LOGIN, false);
        initView();
        initData();
    }

    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        etAccountNum = findViewById(R.id.et_account_num);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void initData() {
        mTitleBarView.setTvTitleText("登录");
//        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        mTitleBarView.setVisiLeftImage(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sinIn();
                //startActivity(new Intent(LogonActivity.this, PersonCenterActivity.class));
                requestPermission();
            }
        });
    }

    // 自定义申请一个权限
    private void requestPermission() {
        PermissionUtils.checkPermission(mContext, Manifest.permission.READ_PHONE_STATE,
                new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        sinIn();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestPermission(mContext, Manifest.permission.READ_PHONE_STATE, REQUEST_CODE_STORAGE);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.requestPermission(mContext, Manifest.permission.READ_PHONE_STATE, REQUEST_CODE_STORAGE);
                    }
                });
    }

    private void sinIn() {
        sinInLogin.showLoading("正在登录");
        Map<String,String> users=new HashMap<>();
        users.put("phone",etAccountNum.getText().toString().trim());
        users.put("pwd",etPassword.getText().toString().trim());
        RetrofitManage.getInstance().subscribe(Api.getInstance().signIn(users), new Observer<BaseEntity<LoginData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<LoginData> logindata) {
                LogManager.printErrorLog("backinfo", "成功返回的数据：" + GsonUtils.getInstance().toJson(logindata));
                if (logindata.isSuccess() == true) {
                    sinInLogin.showSuccess("登录成功");
                    initOssToken();
                    setSPValuse(logindata.getData());
                    SPUtil.put(mContext, AppConfig.IS_LOGIN, true);
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setLoginSuccess(true);
                    EventBus.getDefault().post(messageEvent);

                    SPUtil.put(mContext, AppConfig.ROLE_TYPE, AppConfig.ROLE_CLEANER);//用户角色
                    Intent intent = new Intent(mContext, MainActivity.class);
                    if(getIntent().getBundleExtra(AppConfig.EXTRA_BUNDLE) != null){
                        intent.putExtra(AppConfig.EXTRA_BUNDLE, getIntent().getBundleExtra(AppConfig.EXTRA_BUNDLE));
                    }
                    intent.putExtra(AppConfig.ROLE_TYPE, AppConfig.ROLE_CLEANER);//用户角色
                    RxBus.get().post(logindata.getData());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogonActivity.this, logindata.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                sinInLogin.showError(ThrowableUtils.getInstance(LogonActivity.this).downloadException(e));
                LogManager.printErrorLog("backinfo", "失败返回的数据：" + e.getMessage());
            }

            @Override
            public void onComplete() {

                sinInLogin.dismiss();
            }
        });
    }
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    private void setSPValuse(Object o){
        if (o!=null&&o.getClass().getDeclaredFields()!=null) {
            Field[] fields = o.getClass().getDeclaredFields();
            String[] fieldNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                SPUtil.put(LogonActivity.this, fields[i].getName(), getFieldValueByName(fields[i].getName(), o));
            }
        }
    }
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    sinIn();
                } else {
                    ToastUtil.showShort("获取设备识别码失败!");
                }
                break;
        }
    }


    private void initOssToken() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().ossToken(), new Observer<BaseEntity<UploadInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<UploadInfo> uploadInfoBaseEntity) {
                if (uploadInfoBaseEntity.isSuccess()){
                    UploadInfo uploadInfo = uploadInfoBaseEntity.getData();
                    SPUtil.saveObject(mContext,AppConfig.UPLOAD_INFO,uploadInfo);
                    if (uploadInfo!=null){
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
}
