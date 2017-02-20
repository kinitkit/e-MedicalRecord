package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Past_Medical_History.Bus.Bus_PastMedicalHistory_OnLongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Past_Medical_History.Class.Past_Medical_History;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_PastMedicalHistory extends RecyclerView.Adapter<RecyclerViewAdapter_PastMedicalHistory.ViewHolder> {
    ArrayList<Past_Medical_History> pastMedicalHistories;

    //Constructor
    public RecyclerViewAdapter_PastMedicalHistory(ArrayList<Past_Medical_History> pastMedicalHistories) {
        this.pastMedicalHistories = pastMedicalHistories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_past_medical_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Past_Medical_History pastMedicalHistory = pastMedicalHistories.get(position);
        holder.pastMedicalHistory = pastMedicalHistory;
        holder.tv_date.setText(pastMedicalHistory.strDateTime);
        holder.tv_medicalCondition.setText(pastMedicalHistory.medicalCondition);
    }

    @Override
    public int getItemCount() {
        return pastMedicalHistories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        CardView cardView;
        TextView tv_date, tv_medicalCondition;
        Past_Medical_History pastMedicalHistory;

        public ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_medicalCondition = (TextView) view.findViewById(R.id.tv_medicalCondition);
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_PastMedicalHistory_OnLongClick(getAdapterPosition(), pastMedicalHistory));
            return false;
        }
    }
}
