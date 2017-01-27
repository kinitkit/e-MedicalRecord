package com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Consultation.Bus.Bus_Consultation;
import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation;
import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Consultation extends RecyclerView.Adapter<RecyclerViewAdapter_Consultation.ViewHolder> {
    private ArrayList<Consultation> consultations;

    public RecyclerViewAdapter_Consultation(ArrayList<Consultation> consultations) {
        this.consultations = consultations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_consultation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Consultation consultation = consultations.get(position);
        holder.consultation = consultation;
        holder.tv_date.setText(consultation.strDateTime);
        holder.tv_physician.setText(consultation.physicianName);
        holder.tv_chiefComplaint.setText(consultation.chiefComplaint);
        holder.tv_diagnosis.setText(consultation.diagnosis);
    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Consultation consultation;
        CardView cardView;
        TextView tv_date, tv_physician, tv_chiefComplaint, tv_diagnosis;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_physician = (TextView) itemView.findViewById(R.id.tv_physician);
            tv_chiefComplaint = (TextView) itemView.findViewById(R.id.tv_chiefComplaint);
            tv_diagnosis = (TextView) itemView.findViewById(R.id.tv_diagnosis);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BusStation.getBus().post(new Bus_Consultation(getAdapterPosition(), consultation, 0));
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Consultation(getAdapterPosition(), consultation, 1));
            return true;
        }
    }
}
