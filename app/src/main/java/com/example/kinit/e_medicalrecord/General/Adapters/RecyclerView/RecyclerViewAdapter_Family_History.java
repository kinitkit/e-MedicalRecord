package com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Family_History.Bus.Bus_FamilyHistory_OnLongClick;
import com.example.kinit.e_medicalrecord.Family_History.Class.Family_History;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Family_History extends RecyclerView.Adapter<RecyclerViewAdapter_Family_History.ViewHolder> {
    ArrayList<Family_History> familyHistories;

    public RecyclerViewAdapter_Family_History(ArrayList<Family_History> familyHistories) {
        this.familyHistories = familyHistories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_family_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Family_History familyHistory = familyHistories.get(position);
        holder.familyHistory = familyHistory;
        holder.tv_medicalCondition.setText(familyHistory.medicalCondition);
        holder.cb_grandparent.setChecked(familyHistory.grandP);
        holder.cb_parent.setChecked(familyHistory.parent);
        holder.cb_sibling.setChecked(familyHistory.sibling);
        holder.cb_child.setChecked(familyHistory.child);
    }

    @Override
    public int getItemCount() {
        return familyHistories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        Family_History familyHistory;
        CardView cardView;
        TextView tv_medicalCondition;
        CheckBox cb_grandparent, cb_parent, cb_sibling, cb_child;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tv_medicalCondition = (TextView) itemView.findViewById(R.id.tv_medicalCondition);
            cb_grandparent = (CheckBox) itemView.findViewById(R.id.cb_grandparent);
            cb_parent = (CheckBox) itemView.findViewById(R.id.cb_parent);
            cb_sibling = (CheckBox) itemView.findViewById(R.id.cb_sibling);
            cb_child = (CheckBox) itemView.findViewById(R.id.cb_child);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_FamilyHistory_OnLongClick(getAdapterPosition(), familyHistory));
            return true;
        }
    }
}
