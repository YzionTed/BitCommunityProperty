package com.bit.communityProperty.activity.safetywarning;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SafeWarningReportActivity extends BaseActivity {


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
    @BindView(R.id.et_input)
    protected
    EditText etInput;
    @BindView(R.id.tv_length)
    TextView tvLength;

    private String id;
    @Override
    public int getLayoutId() {
        return R.layout.activity_safe_warning;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView() {
        actionBarTitle.setText("排查报告");
        btnRightActionBar.setText("完成");
        btnRightActionBar.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(s == null ? "0/200" : s.length() + "/200");
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_right_action_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right_action_bar:
                if (TextUtils.isEmpty(etInput.getText().toString())){
                    ToastUtil.showShort(etInput.getHint().toString());
                    return;
                }
                sendReport();
                break;
        }
    }

    public void sendReport(){
        Map<String, Object> map = new HashMap<>();
        map.put("Id", id);
        map.put("troubleShootingReport", etInput.getText().toString());
        map.put("troubleShootingTime", TimeUtils.getCurrentDateWithHMS());
        RetrofitManage.getInstance().subscribe(Api.getInstance().troubleShoot(map), new Observer<BaseEntity<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<Object> baseEntity) {
                if (baseEntity.isSuccess()){
                    ToastUtil.showShort("提交成功");
                    RxBus.get().post("finish");
                    RxBus.get().post("update");
                    finish();
                }else{
                    ToastUtil.showShort(baseEntity.getErrorMsg());
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
