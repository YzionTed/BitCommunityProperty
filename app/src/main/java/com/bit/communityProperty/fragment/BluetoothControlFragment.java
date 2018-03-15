package com.bit.communityProperty.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;

/**
 * 蓝牙梯控的fargment页面
 * Created by kezhangzhao on 2018/1/21.
 */

public class BluetoothControlFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context mContext;

    public BluetoothControlFragment() {
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public static BluetoothControlFragment newInstance(int sectionNumber, Context context) {
        BluetoothControlFragment fragment = new BluetoothControlFragment();
        fragment.setContext(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bluetooth_control, container, false);

//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

}
