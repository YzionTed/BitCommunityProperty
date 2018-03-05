package com.bit.communityProperty.activity.propertyFee.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.activity.propertyFee.PropertyFeeDetailActivity;
import com.bit.communityProperty.activity.propertyFee.adapter.PropertyFeeAdapter;

import java.util.ArrayList;

/**
 * 物业费的fragment
 * Created by kezhangzhao on 2018/2/10.
 */

public class PropertyFeeFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<AuditingBean> mAuditingBeanList = new ArrayList<>();//数据列表
    private Context mContext;
    private ExpandableListView mExpandableListView;//可展开的listview
    private PropertyFeeAdapter adapter;//可展开listview的adapter
    public String[] groupStrings= {"Group1", "Group2", "Group3", "Group4", "Group5", "Group6", "Group7",
            "Group8","Group9", "Group10", "Group11", "Group12"};
    public String[][] childStrings={ {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"} ,
            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"},
            {"Child1", "Child2", "Child3", "Child4"}, {"Child1", "Child2", "Child3", "Child4"}};
    public PropertyFeeFragment() {
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void initView(Context context) {
        this.mContext = context;
    }


    public static PropertyFeeFragment newInstance(int sectionNumber, Context context) {
        PropertyFeeFragment fragment = new PropertyFeeFragment();
//        fragment.setContext(context);
        fragment.initView(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        //add
        //add
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_property_fee, container, false);
        mExpandableListView = rootView.findViewById(R.id.expandableListView);
//        mExpandableListView.setGroupIndicator(null);
        adapter = new PropertyFeeAdapter(mContext,groupStrings,childStrings);
        mExpandableListView.setAdapter(adapter);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//                Toast.makeText(mContext,childStrings[groupPosition][childPosition],Toast.LENGTH_LONG).show();
                startActivity(new Intent(mContext,PropertyFeeDetailActivity.class));
                return false;
            }
        });
        return rootView;
    }
}
