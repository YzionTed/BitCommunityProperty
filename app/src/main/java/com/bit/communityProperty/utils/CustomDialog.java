package com.bit.communityProperty.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseDialog;

/**
 * Created by 92594 on 2018/1/26.
 */

public class CustomDialog extends BaseDialog {
    private Window window;
    private LinearLayout ll_dialogcustom;
    private View v;
    private Context context;

    public CustomDialog(Context context) {
        super(context, R.style.appgameDialog);
        this.context = context;

        // TODO Auto-generated constructor stub
    }

    public CustomDialog(Context context, int menuType) {
        super(context, menuType);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        // initView(buttonText.length, mTopInfo);
        windowDeploy();
        initView();
        setCanceledOnTouchOutside(false);

    }

    public void windowDeploy() {
        window = getWindow();

        // window.setBackgroundDrawableResource(R.color.white);

        window.setWindowAnimations(R.style.centre_dialog_ani);

        WindowManager.LayoutParams wl = window.getAttributes();

        WindowManager windowManager = window.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        wl.width = displayMetrics.widthPixels * 5 / 6;

        // wl.alpha = 1.5f;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);

    }

    private void initView() {
        ll_dialogcustom = (LinearLayout) findViewById(R.id.ll_dialogcustom);
    }

    public LinearLayout getLl_dialogcustom() {
        return ll_dialogcustom;
    }

    public void setLl_dialogcustom(LinearLayout ll_dialogcustom) {
        this.ll_dialogcustom = ll_dialogcustom;
    }

    public void showDialog() {

        show();
    }


    public View getV() {
        return v;
    }

    public void setV(View v) {
        this.v = v;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
            dismiss();
            return true;

        }
        return super.dispatchKeyEvent(event);
    }

    public void addView(Context context, int layout) {
        v = LayoutInflater.from(context).inflate(layout, null);
        ll_dialogcustom.addView(v);
    }
    public void addView( int layout) {
        v = LayoutInflater.from(context).inflate(layout, null);
        ll_dialogcustom.addView(v);
    }



}
