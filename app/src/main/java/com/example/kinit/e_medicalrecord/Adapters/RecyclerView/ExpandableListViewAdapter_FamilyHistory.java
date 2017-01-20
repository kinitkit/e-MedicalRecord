package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Classes.Family_History.Family_History;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class ExpandableListViewAdapter_FamilyHistory extends BaseExpandableListAdapter {
    ArrayList<Family_History> familyHistories;
    Context context;

    public ExpandableListViewAdapter_FamilyHistory(Context context, ArrayList<Family_History> familyHistories) {
        this.familyHistories = familyHistories;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return familyHistories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return familyHistories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return familyHistories.get(groupPosition);
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
        String headerTitle = familyHistories.get(groupPosition).medicalCondition;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.family_history_group, parent, false);
        }
        TextView tv_header = (TextView) convertView.findViewById(R.id.tv_familyHistory_header);
        tv_header.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.family_history_list, parent, false);
        }

        CheckBox cb_grandparent = (CheckBox) convertView.findViewById(R.id.cb_grandparent),
                cb_parent = (CheckBox) convertView.findViewById(R.id.cb_parent),
                cb_sibling = (CheckBox) convertView.findViewById(R.id.cb_sibling),
                cb_child = (CheckBox) convertView.findViewById(R.id.cb_child);

        cb_grandparent.setChecked(familyHistories.get(groupPosition).grandP);
        cb_parent.setChecked(familyHistories.get(groupPosition).parent);
        cb_sibling.setChecked(familyHistories.get(groupPosition).sibling);
        cb_child.setChecked(familyHistories.get(groupPosition).child);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
