package com.bit.communityProperty.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by DELL60 on 2018/1/30.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    protected Context mContext;
    protected View mView;

    //获取布局文件ID
    protected abstract int getLayoutId();
    protected abstract void initViewAndData();
    @Override
    public void onAttach(Context context) {
        mActivity = (BaseActivity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView==null){
            mView = inflater.inflate(getLayoutId(), container,false);
            ButterKnife.bind(this, mView);
            initViewAndData();
        }
//        ViewGroup parent = (ViewGroup) mView.getParent();
//        if (parent != null) {
//            parent.removeView(mView);
//        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

