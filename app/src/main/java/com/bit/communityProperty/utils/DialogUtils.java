package com.bit.communityProperty.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;

/**
 * Created by kezhangzhao on 2018/1/13.
 */

public class DialogUtils {
    private static Dialog mDialog;
    private static TextView mTvTitle, mTvContent;
    private static Button mBtnConfirm, mBtnCancel;
    private View mDivider;
    private static DialogUtils mDialogUtils = null;

    public static DialogUtils getInstance() {
        if (null == mDialogUtils) {
            synchronized (DialogUtils.class) {
                if (mDialogUtils == null) {
                    mDialogUtils = new DialogUtils();
                }
            }
        }
        return mDialogUtils;
    }

    public void initSubmitDialog(Context context) {
        mDialog = new Dialog(context, R.style.AlertDialog);
        View view = mDialog.getWindow().getLayoutInflater().inflate(R.layout.dialog_submit, null);
        mDialog.setContentView(view);
        mTvTitle = view.findViewById(R.id.dialog_submit_tv_title);
        mTvContent = view.findViewById(R.id.dialog_submit_tv_content);
        mBtnCancel = view.findViewById(R.id.dialog_submit_btn_cancel);
        mBtnConfirm = view.findViewById(R.id.dialog_submit_btn_confirm);
        mDivider = view.findViewById(R.id.dialog_submit_view_divider_vertical);
        mDialog.setCanceledOnTouchOutside(false);
    }
    public void setTitleVisibility(int visi) {
        mTvTitle.setVisibility(visi);
    }

    public void setSubmitTitle(int tvRes) {
        mTvTitle.setText(tvRes);
    }

    public void setSubmitTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setSubmitContent(String text) {
        mTvContent.setText(text);
    }

    public void setSubBtnCancelText(int tvRes) {
        mBtnCancel.setText(tvRes);
    }
    public void setSubBtnCancelText(String tvRes) {
        mBtnCancel.setText(tvRes);
    }

    public void setSubBtnConfirmText(int tvRes) {
        mBtnConfirm.setText(tvRes);
    }
    public void setSubBtnConfirmText(String tvRes) {
        mBtnConfirm.setText(tvRes);
    }

    public void setBtnDiderVisi(int btnVisi, int divderVisi) {
        mBtnCancel.setVisibility(btnVisi);
        mDivider.setVisibility(divderVisi);
    }

    public void setSubTvCenter(Context context) {
        if (mTvContent != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            mTvContent.setLayoutParams(params);
        }
    }

    public void setSubCancel(boolean flag) {
        if (mDialog != null) {
            mDialog.setCancelable(flag);
        }
    }

    public void setBtnTextColor(int color) {
        mBtnConfirm.setTextColor(color);
        mBtnCancel.setTextColor(color);
    }

    public void setSubCancelClick(View.OnClickListener listener) {
        if (listener != null) {
            mBtnCancel.setOnClickListener(listener);
        }
    }

    public void setSubConfirmClick(View.OnClickListener listener) {
        if (listener != null) {
            mBtnConfirm.setOnClickListener(listener);
        }
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public synchronized void disMissDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
