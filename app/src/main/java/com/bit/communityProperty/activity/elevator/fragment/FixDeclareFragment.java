package com.bit.communityProperty.activity.elevator.fragment;

import android.view.View;
import android.widget.ListView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class FixDeclareFragment extends BaseFragment {
    private ListView lvLog;
    private CommonAdapter commonAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fault_log;
    }

    @Override
    protected void initViewAndData() {
        lvLog = mView.findViewById(R.id.lv_log);
        commonAdapter = new CommonAdapter<String>(mActivity, R.layout.item_fault_log) {
            @Override
            public void convert(ViewHolder holder, String o, int position, View convertView) {

            }
        };
        lvLog.setAdapter(commonAdapter);
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            s.add("");
        }
        commonAdapter.setDatas(s);
    }
}
