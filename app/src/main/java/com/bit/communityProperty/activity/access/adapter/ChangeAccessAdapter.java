package com.bit.communityProperty.activity.access.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.view.CustomExpandListview;
import com.bit.communityProperty.widget.locktable.DisplayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL60 on 2018/2/8.
 */

public class ChangeAccessAdapter extends BaseExpandableListAdapter implements CustomExpandListview.HeaderAdapter {

    private ArrayList<String> parent;
    private Map<String, ArrayList<String>> datas = new HashMap<>();
    private Context context;
    private CustomExpandListview listview;
    private Map<Integer,Map<Integer,Boolean>> selMap;

    public ChangeAccessAdapter(Context context, ArrayList<String> parent, Map<String, ArrayList<String>> datas, CustomExpandListview listview) {
        this.context = context;
        this.parent = parent;
        this.datas = datas;
        this.listview = listview;
        selMap = new HashMap<>();
        for (int i = 0; i < parent.size(); i++) {
            Map<Integer, Boolean> map = new HashMap<>();
            for (int j = 0; j < datas.get(parent.get(i)).size(); j++) {
                map.put(j, false);
            }
            selMap.put(i, map);
        }
    }

    @Override
    public int getGroupCount() {
        int temp = 0;
        if (parent != null) {
            temp = parent.size();
        }
        return temp;
    }

    @Override
    public int getChildrenCount(int i) {
        int size = 0;
        if (datas != null && parent != null && parent.size() > 0) {
            String key = parent.get(i);
            size = datas.get(key).size();
        }
        return size;
    }

    @Override
    public Object getGroup(int i) {
        return parent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String key = parent.get(i);
        return (datas.get(key).get(i1));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_access_group, null);
        }
        TextView tv = (TextView) view
                .findViewById(R.id.tv_parent);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
        if (i==0){
            lp.topMargin = 0;
            tv.setLayoutParams(lp);
        }else{
            lp.topMargin = DisplayUtil.dip2px(context,10);
            tv.setLayoutParams(lp);
        }
        tv.setText(parent.get(i));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {

        final PlaceHolder holder;
        if (view == null) {
            holder = new PlaceHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_access_child, null);
            holder.tv_title = view.findViewById(R.id.tv_item);
            holder.llItem = view.findViewById(R.id.ll_item);
            view.setTag(holder);
        } else {
            holder = (PlaceHolder) view.getTag();
        }
        String key = parent.get(groupPosition);
        String s = datas.get(key).get(childPosition);
        holder.tv_title.setText(s);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selMap.get(groupPosition).put(childPosition, !holder.tv_title.isChecked());
                holder.tv_title.setChecked(!holder.tv_title.isChecked());
            }
        });
        holder.tv_title.setChecked(selMap.get(groupPosition).get(childPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 根据当前的groupPosition和childPosition判断指示布局是哪种状态（隐藏、可见、正在向上推）
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listview.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    /**
     * 给指示布局设置内容
     *
     * @param header
     * @param groupPosition
     * @param childPosition
     * @param alpha
     */
    @Override
    public void configureHeader(View header, int groupPosition, int childPosition, int alpha) {
        if (groupPosition > -1) {
            ((TextView) header.findViewById(R.id.tv_indictor))
                    .setText(parent.get(groupPosition));
        }
    }

    private class PlaceHolder {
        private LinearLayout llItem;
        private CheckBox tv_title;
    }
}
