package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Past_Medical_History.Bus_Past_Medical_History_Item;
import com.example.kinit.e_medicalrecord.Classes.Past_Medical_History.Past_Medical_History;
import com.example.kinit.e_medicalrecord.R;

public class RecyclerViewAdapter_PastMedicalHistory extends RecyclerView.Adapter<RecyclerViewAdapter_PastMedicalHistory.ViewHolder> {
    Past_Medical_History past_medical_history;
    int patient_id;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv_medicalCondition;
        TextView tv_medicalCondition_header, tv_medicalCondition_data, tv_medicalCondition_yr;

        public ViewHolder(View view) {
            super(view);
            cv_medicalCondition = (CardView) view.findViewById(R.id.cv_medicalCondition);
            tv_medicalCondition_header = (TextView) view.findViewById(R.id.tv_medicalCondition_header);
            tv_medicalCondition_data = (TextView) view.findViewById(R.id.tv_medicalCondition_data);
            tv_medicalCondition_yr = (TextView) view.findViewById(R.id.tv_medicalCondition_yr);
        }
    }

    //Constructor
    public RecyclerViewAdapter_PastMedicalHistory(Past_Medical_History past_medical_history, int patient_id) {
        this.patient_id = patient_id;
        this.past_medical_history = past_medical_history;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_past_medical_history, parent, false);
        //ViewHolder viewHolder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //holder
        holder.tv_medicalCondition_header.setText(past_medical_history.getMedicalCondition_header(position));
        holder.tv_medicalCondition_data.setText(convertToYesNo(past_medical_history.getMedicalCondition_data(position)));
        holder.tv_medicalCondition_yr.setText(String.valueOf(past_medical_history.getYear(position)));
        holder.cv_medicalCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_Past_Medical_History_Item(patient_id, past_medical_history.getYear(position),
                        past_medical_history.getKey(position * 2), past_medical_history.getKey((position * 2) + 1),
                        past_medical_history.getMedicalCondition_header(position), past_medical_history.getMedicalCondition_data(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return past_medical_history.length();
    }

    String convertToYesNo(boolean str) {
        return (str) ? "Yes" : "No";
    }
}
