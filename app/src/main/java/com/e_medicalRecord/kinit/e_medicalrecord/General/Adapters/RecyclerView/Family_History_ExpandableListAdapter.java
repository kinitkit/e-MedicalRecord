package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.List;

public class Family_History_ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private List<String> listDataChild;
    private Boolean[] isChecked;

    public Family_History_ExpandableListAdapter(Context context, List<String> listDataHeader,
                                                List<String> listChildData, Boolean[] isChecked) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.isChecked = isChecked;
    }

    //Group
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.family_history_group, null);
        }
        TextView tv_header = (TextView) convertView.findViewById(R.id.tv_familyHistory_header);
        tv_header.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.family_history_list, null);
        }

        CheckBox cb_grandparent = (CheckBox) convertView.findViewById(R.id.cb_grandparent),
                cb_parent = (CheckBox) convertView.findViewById(R.id.cb_parent),
                cb_sibling = (CheckBox) convertView.findViewById(R.id.cb_sibling),
                cb_child = (CheckBox) convertView.findViewById(R.id.cb_child);

        cb_grandparent.setChecked(isChecked[groupPosition * 4]);
        cb_parent.setChecked(isChecked[(groupPosition * 4) + 1]);
        cb_sibling.setChecked(isChecked[(groupPosition * 4) + 2]);
        cb_child.setChecked(isChecked[(groupPosition * 4) + 3]);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
