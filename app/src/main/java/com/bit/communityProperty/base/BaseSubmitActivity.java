package com.bit.communityProperty.base;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 提交文字的基础窗口
 * Created by kezhangzhao on 2018/2/28.
 */

public abstract class BaseSubmitActivity extends BaseActivity {

    protected TitleBarView mTitleBarView;//标题栏
    protected EditText etInput;//输入框
    protected TextView tvLength;//输入文字长度
    protected Activity mActivity;
    protected int maxLines;//文字最大长度

    @Override
    public int getLayoutId() {
        return R.layout.actvity_base_submit;
    }

    @Override
    public void initViewData() {
        mActivity = this;
        mTitleBarView = findViewById(R.id.titlebarview);
        tvLength = findViewById(R.id.tv_length);
        etInput = findViewById(R.id.et_input);
        setTitleBackground(mTitleBarView);
        setTitleString(mTitleBarView);
        setTitleSize(mTitleBarView);
        setTitleStrColor(mTitleBarView);
        setMaxLines(etInput,tvLength);
        setEtInputHint(etInput);
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(s == null ? "0/"+maxLines : s.length() + "/"+maxLines);
            }
        });
    }

    /**
     * 点击完成的抽象方法
     * 执行网络访问提交之类的处理
     */
    protected abstract void submitData();

    /**
     * 设置标题栏目的背景颜色
     * @param titleBarView
     */
    protected void setTitleBackground(TitleBarView titleBarView){
        titleBarView.setLayoutTitleBg(R.color.white);
    }

    /**
     * 设置标题的文字
     * @param titleBarView
     */
    protected void setTitleString(TitleBarView titleBarView){
        titleBarView.setTvTitleText("驳回理由");
        titleBarView.setLeftText("取消");
        titleBarView.setRightText("完成");
    }

    /**
     * 设置标题的文字大小
     * @param titleBarView
     */
    protected void setTitleSize(TitleBarView titleBarView){
        titleBarView.setTvTitleTextSize(18);
        titleBarView.setLeftTextSize(18);
        titleBarView.setRightTextSize(18);
    }

    /**
     * 设置标题文字的颜色
     * @param titleBarView
     */
    protected void setTitleStrColor(TitleBarView titleBarView){
        titleBarView.setTvTitleTextColor(R.color.tv_black_14);
        titleBarView.setLeftTextColor(R.color.tv_gray_66);
        titleBarView.setRightTextColor(R.color.blues5f90f9);
    }

    /**
     * 设置输入框的最大容量的字符长度
     * 这个方面要是子类重新，还是需要赋值maxLines。不然EditText的右下方无法正常统计数量,并在tvLength需进行初始化tvLength.setText("0/"+maxLines);
     * @param et
     */
    protected void setMaxLines(EditText et,TextView tv){
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        maxLines=200;
        tv.setText("0/"+maxLines);
    }

    /**
     * 设置输入框中的提示文字
     * 默认文字为空
     * @param et
     */
    protected void setEtInputHint(EditText et){
        et.setHint("");
    }

}
