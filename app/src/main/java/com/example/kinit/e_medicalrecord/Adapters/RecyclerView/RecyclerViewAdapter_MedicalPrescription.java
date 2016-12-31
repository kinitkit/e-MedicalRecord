package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_Click;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Medical_Prescription_LongClick;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_MedicalPrescription extends RecyclerView.Adapter<RecyclerViewAdapter_MedicalPrescription.ViewHolder> {

    ArrayList<Medical_Prescription> medicalPrescriptions;

    public RecyclerViewAdapter_MedicalPrescription(ArrayList<Medical_Prescription> medicalPrescriptions) {
        this.medicalPrescriptions = medicalPrescriptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_medical_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(medicalPrescriptions.get(position).date);
        holder.tv_physicianName.setText(medicalPrescriptions.get(position).physicianName);
        if (medicalPrescriptions.get(position).clinic_name.isEmpty())
            holder.tv_clinicName.setVisibility(View.GONE);
        else
            holder.tv_clinicName.setText(medicalPrescriptions.get(position).clinic_name);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_Medical_Prescription_Click(medicalPrescriptions.get(position).medicalPrescription_id, medicalPrescriptions.get(position).patient_id,
                        medicalPrescriptions.get(position).physicianName, medicalPrescriptions.get(position).clinic_name, medicalPrescriptions.get(position).date));
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BusStation.getBus().post(new Bus_Medical_Prescription_LongClick(medicalPrescriptions.get(position).medicalPrescription_id,
                        medicalPrescriptions.get(position).patient_id, medicalPrescriptions.get(position).physicianName,
                        medicalPrescriptions.get(position).clinic_name, medicalPrescriptions.get(position).date,
                        medicalPrescriptions.get(position).calendarStr, position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalPrescriptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_physicianName, tv_clinicName;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_physicianName = (TextView) view.findViewById(R.id.tv_physicianName);
            tv_clinicName = (TextView) view.findViewById(R.id.tv_clinicName);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }
}
