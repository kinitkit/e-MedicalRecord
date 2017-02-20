package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Drug_OnLongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Drug;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_MedicalPrescriptionDrugForm extends RecyclerView.Adapter<RecyclerViewAdapter_MedicalPrescriptionDrugForm.ViewHolder> {

    ArrayList<Drug> drugs;

    public RecyclerViewAdapter_MedicalPrescriptionDrugForm(ArrayList<Drug> drugs) {
        this.drugs = drugs;
    }

    public void addItem(Drug drug) {
        this.drugs.add(drug);
        this.notifyItemInserted(drugs.size() - 1);
    }

    public void updateItem(int position, Drug drug){
        this.drugs.set(position, drug);
        this.notifyItemChanged(position);
    }

    public void removeItem(int position){
        this.drugs.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_drug_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drug drug = drugs.get(position);
        holder.drug = drug;
        holder.tv_drug.setText(drug.drug);
        holder.tv_strength.setText(drug.strength);
        holder.tv_amount.setText(drug.dosage);
        holder.tv_route.setText(drug.route);
        holder.tv_frequency.setText(drug.frequency);
        holder.tv_why.setText(drug.why);
        holder.tv_many.setText(drug.quantity);
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        Drug drug;
        TextView tv_drug, tv_strength, tv_amount, tv_route, tv_frequency, tv_why, tv_many;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_drug = (TextView) itemView.findViewById(R.id.tv_drug);
            tv_strength = (TextView) itemView.findViewById(R.id.tv_strength);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_route = (TextView) itemView.findViewById(R.id.tv_route);
            tv_frequency = (TextView) itemView.findViewById(R.id.tv_frequency);
            tv_why = (TextView) itemView.findViewById(R.id.tv_why);
            tv_many = (TextView) itemView.findViewById(R.id.tv_many);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Drug_OnLongClick(getAdapterPosition(), drug));
            return false;
        }
    }
}
