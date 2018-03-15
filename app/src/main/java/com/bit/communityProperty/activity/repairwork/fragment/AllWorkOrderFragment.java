package com.bit.communityProperty.activity.repairwork.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.repairwork.RepairWorkDetailActivity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DELL60 on 2018/2/8.
 */

public class AllWorkOrderFragment extends BaseFragment {
    @BindView(R.id.listview)
    ListView listview;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_work_order_list;
    }

    @Override
    protected void initViewAndData() {
        CommonAdapter adapter = new CommonAdapter<String>(mActivity,R.layout.item_work_order) {
            @Override
            public void convert(ViewHolder holder, String o, int position, View convertView) {
                holder.setOnClickListener(R.id.item_main, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mActivity, RepairWorkDetailActivity.class));
                    }
                });
            }
        };
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            s.add("");
        }
        listview.setAdapter(adapter);
        adapter.setDatas(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
