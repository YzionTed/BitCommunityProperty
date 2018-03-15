package com.bit.communityProperty.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;

/**
 * 提前呼梯的Fragment页面
 * Created by kezhangzhao on 2018/1/21.
 */

public class AdvanceControlFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context mContext;

    public AdvanceControlFragment() {
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public static AdvanceControlFragment newInstance(int sectionNumber, Context context) {
        AdvanceControlFragment fragment = new AdvanceControlFragment();
        fragment.setContext(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_advance_control, container, false);

//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
