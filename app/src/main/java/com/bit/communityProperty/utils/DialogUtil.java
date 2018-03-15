package com.bit.communityProperty.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;


/**
 * Created by 92594 on 2018/1/26.
 */

public class DialogUtil {
    private static CustomDialog customBottomDialog;

    /**
     * 带有取消确定按钮的dialog提示
     *
     * @param context
     * @param title
     * @param msg
     * @param needCancel 是否需要取消按钮
     */
    public static void showConfirmDialog(Context context, String title, String msg, boolean needCancel, View.OnClickListener onClickListener) {
        customBottomDialog = new CustomDialog(context);
        customBottomDialog.show();
        customBottomDialog.setCanceledOnTouchOutside(false);
        customBottomDialog.setCancelable(false);
        customBottomDialog.addView(R.layout.dialog_ok_cancel);
        TextView tv_dialog_title = customBottomDialog.getV().findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_msg = customBottomDialog.getV().findViewById(R.id.tv_dialog_msg);
        tv_dialog_title.setText(title);
        tv_dialog_msg.setText(msg);
        if (needCancel) {
            TextView btn_dia_left = customBottomDialog.getV().findViewById(R.id.btn_dia_left);
            btn_dia_left.setVisibility(View.VISIBLE);
            btn_dia_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customBottomDialog.cancel();
                }
            });
        }
        TextView tv_dia_right = customBottomDialog.getV().findViewById(R.id.btn_dia_right);
        tv_dia_right.setOnClickListener(onClickListener);
    }

    /**
     * 带有图片的dialog
     *
     * @param context
     * @param img
     * @param msg
     * @param leftBtnText
     * @param rightBtnText
     * @param needCancel
     */
    public static void showTipDialog(Context context, int img, String msg, String leftBtnText,
                                     String rightBtnText, boolean needCancel, View.OnClickListener onClickListener) {
        customBottomDialog = new CustomDialog(context);
        customBottomDialog.setCancelable(false);
        customBottomDialog.show();
        customBottomDialog.addView(R.layout.dialog_btn_with_img);
        ImageView ivTip = customBottomDialog.getV().findViewById(R.id.iv_tip);
        TextView tv_dialog_msg = customBottomDialog.getV().findViewById(R.id.tv_dialog_msg);
        ivTip.setImageResource(img);
        tv_dialog_msg.setText(msg);
        if (needCancel) {
            TextView btn_dia_left = customBottomDialog.getV().findViewById(R.id.btn_dia_left);
            btn_dia_left.setText(leftBtnText);
            btn_dia_left.setVisibility(View.VISIBLE);
            btn_dia_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customBottomDialog.cancel();
                }
            });
        }
        TextView btn_dia_right = customBottomDialog.getV().findViewById(R.id.btn_dia_right);
        btn_dia_right.setText(rightBtnText);
        btn_dia_right.setOnClickListener(onClickListener);
    }

    /**
     * 开关frame动画dialog
     *
     * @param context
     * @param animDraw
     */
    public static CustomDialog showFrameAnimDialog(Context context, int animDraw) {
        customBottomDialog = new CustomDialog(context);
        customBottomDialog.show();
        customBottomDialog.addView(R.layout.dialog_frame_anim);
        ImageView ivAnim = customBottomDialog.findViewById(R.id.iv_anim);
        ivAnim.setImageResource(animDraw);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getDrawable();
        animationDrawable.start();


        return customBottomDialog;
    }

    public static void dissmiss() {
        if (customBottomDialog != null && customBottomDialog.isShowing()) {
            customBottomDialog.dismiss();
        }
    }
}
