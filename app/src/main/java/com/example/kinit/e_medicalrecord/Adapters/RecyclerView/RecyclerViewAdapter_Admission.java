package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.Admission.Bus_Admission_OnLongClick;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Admission.Bus_Admission_OnClick;
import com.example.kinit.e_medicalrecord.Classes.Admission.Admission;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Admission extends RecyclerView.Adapter<RecyclerViewAdapter_Admission.ViewHolder>{
    ArrayList<Admission> admissions;

    public RecyclerViewAdapter_Admission(ArrayList<Admission> admissions) {
        this.admissions = admissions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_admission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.admission = admissions.get(position);
        holder.tv_dateAdmission.setText(admissions.get(position).strDateAdmitted);
        holder.tv_physicianName.setText(admissions.get(position).physicianName);
        holder.tv_hospital.setText(admissions.get(position).hospital);
        holder.tv_finalDiagnosis.setText(admissions.get(position).finalDiagnosis);
    }

    @Override
    public int getItemCount() {
        return admissions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_dateAdmission, tv_physicianName, tv_hospital, tv_finalDiagnosis;
        CardView cardView;
        Admission admission;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_dateAdmission = (TextView) itemView.findViewById(R.id.tv_dateAdmission);
            tv_physicianName = (TextView) itemView.findViewById(R.id.tv_physicianName);
            tv_hospital = (TextView) itemView.findViewById(R.id.tv_hospital);
            tv_finalDiagnosis = (TextView) itemView.findViewById(R.id.tv_finalDiagnosis);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BusStation.getBus().post(new Bus_Admission_OnClick(getAdapterPosition(), admission));
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Admission_OnLongClick(getAdapterPosition(), admission));
            return true;
        }
    }
}
