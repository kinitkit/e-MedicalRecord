package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_MedicalPrescription_Item;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Medical_Prescription;
import com.example.kinit.e_medicalrecord.R;

public class RecyclerViewAdapter_MedicalPrescription extends RecyclerView.Adapter<RecyclerViewAdapter_MedicalPrescription.ViewHolder> {

    Medical_Prescription[] medicalPrescriptions;

    public RecyclerViewAdapter_MedicalPrescription(Medical_Prescription[] medicalPrescriptions) {
        this.medicalPrescriptions = medicalPrescriptions;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_medical_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(medicalPrescriptions[position].date);
        holder.tv_physicianName.setText(medicalPrescriptions[position].physicianName);
        if (medicalPrescriptions[position].clinic_name.isEmpty())
            holder.tv_clinicName.setVisibility(View.GONE);
        else
            holder.tv_clinicName.setText(medicalPrescriptions[position].clinic_name);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_MedicalPrescription_Item(medicalPrescriptions[position].medicalPrescription_id, medicalPrescriptions[position].patient_id,
                        medicalPrescriptions[position].physicianName, medicalPrescriptions[position].clinic_name, medicalPrescriptions[position].date));
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalPrescriptions.length;
    }
}
