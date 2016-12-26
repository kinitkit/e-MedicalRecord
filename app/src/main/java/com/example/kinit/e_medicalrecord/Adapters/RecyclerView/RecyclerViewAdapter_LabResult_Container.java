package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Laboratory.Bus_View_Lab_Test;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Chemistry;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Fecalysis;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Hematology;
import com.example.kinit.e_medicalrecord.Classes.Laboratory.Lab_Urinalysis;
import com.example.kinit.e_medicalrecord.Enum.Laboratory_Tests;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_LabResult_Container extends RecyclerView.Adapter<RecyclerViewAdapter_LabResult_Container.ViewHolder> {

    ArrayList<Lab_Chemistry> labChemistries;
    ArrayList<Lab_Fecalysis> labFecalysis;
    ArrayList<Lab_Hematology> labHematologies;
    ArrayList<Lab_Urinalysis> labUrinalysises;

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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_physicianName, tv_labName;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_physicianName = (TextView) view.findViewById(R.id.tv_physicianName);
            tv_labName = (TextView) view.findViewById(R.id.tv_labName);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_laboratory, parent, false);
        return new RecyclerViewAdapter_LabResult_Container.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (labChemistries != null) {
            labChemistry_BindViewHolder(holder, position);
        } else if (labFecalysis != null) {
            labFecalysis_BindViewHolder(holder, position);
        } else if (labHematologies != null) {
            labHematology_BindViewHolder(holder, position);
        } else if (labUrinalysises != null) {
            labUrinalysis_BindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (labChemistries != null) {
            return labChemistries.size();
        } else if (labFecalysis != null) {
            return labFecalysis.size();
        } else if (labHematologies != null) {
            return labHematologies.size();
        } else if (labUrinalysises != null) {
            return labUrinalysises.size();
        }
        return 0;
    }

    void labChemistry_BindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(labChemistries.get(position).datePerformed);
        holder.tv_physicianName.setText(labChemistries.get(position).physicianName);
        holder.tv_labName.setText(labChemistries.get(position).labName);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_View_Lab_Test(Laboratory_Tests.BLOOD_CHEMISTRY, labChemistries.get(position)));
            }
        });
    }

    void labFecalysis_BindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(labFecalysis.get(position).datePerformed);
        holder.tv_physicianName.setText(labFecalysis.get(position).physicianName);
        holder.tv_labName.setText(labFecalysis.get(position).labName);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_View_Lab_Test(Laboratory_Tests.FECALYSIS, labFecalysis.get(position)));
            }
        });
    }

    void labHematology_BindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(labHematologies.get(position).datePerformed);
        holder.tv_physicianName.setText(labHematologies.get(position).physicianName);
        holder.tv_labName.setText(labHematologies.get(position).labName);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_View_Lab_Test(Laboratory_Tests.HEMATOLOGY, labHematologies.get(position)));
            }
        });
    }

    void labUrinalysis_BindViewHolder(ViewHolder holder, final int position) {
        holder.tv_date.setText(labUrinalysises.get(position).datePerformed);
        holder.tv_physicianName.setText(labUrinalysises.get(position).physicianName);
        holder.tv_labName.setText(labUrinalysises.get(position).labName);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_View_Lab_Test(Laboratory_Tests.URINALYSIS, labUrinalysises.get(position)));
            }
        });
    }

}
