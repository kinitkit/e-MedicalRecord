package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_Laboratory_OnClick;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_Laboratory_OnLongClick;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Laboratory;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_LabResult_Container extends RecyclerView.Adapter<RecyclerViewAdapter_LabResult_Container.ViewHolder> {

    ArrayList<Lab_Chemistry> labChemistries;
    ArrayList<Lab_Fecalysis> labFecalysis;
    ArrayList<Lab_Hematology> labHematologies;
    ArrayList<Lab_Urinalysis> labUrinalysises;

    ArrayList<Laboratory> laboratories;

    public RecyclerViewAdapter_LabResult_Container(ArrayList<Laboratory> laboratories) {
        this.laboratories = laboratories;
    }

    public void setLabChemistries(ArrayList<Lab_Chemistry> labChemistries) {
        this.labChemistries = labChemistries;
    }

    public void setLabFecalysis(ArrayList<Lab_Fecalysis> lab_fecalysises) {
        this.labFecalysis = lab_fecalysises;
    }

    public void setLabHematologies(ArrayList<Lab_Hematology> labHematologies) {
        this.labHematologies = labHematologies;
    }

    public void setLabUrinalysises(ArrayList<Lab_Urinalysis> labUrinalysises) {
        this.labUrinalysises = labUrinalysises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_laboratory, parent, false);
        return new RecyclerViewAdapter_LabResult_Container.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_date.setText(laboratories.get(position).strDatePerformed);
        holder.tv_physicianName.setText(laboratories.get(position).physician_name);
        holder.tv_labName.setText(laboratories.get(position).lab_name);
    }

    @Override
    public int getItemCount() {
        return laboratories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_date, tv_physicianName, tv_labName;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_physicianName = (TextView) view.findViewById(R.id.tv_physicianName);
            tv_labName = (TextView) view.findViewById(R.id.tv_labName);
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BusStation.getBus().post(new Bus_Laboratory_OnClick(getAdapterPosition(), laboratories.get(getAdapterPosition())));
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Laboratory_OnLongClick(getAdapterPosition(), laboratories.get(getAdapterPosition())));
            return true;
        }
    }

}
