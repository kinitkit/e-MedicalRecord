package com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Surgical_History.Bus.Bus_SurgicalHistory_OnLongClick;
import com.example.kinit.e_medicalrecord.Surgical_History.Class.Surgical_History;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Surgery extends RecyclerView.Adapter<RecyclerViewAdapter_Surgery.ViewHolder> {
    ArrayList<Surgical_History> surgicalHistories;

    //Constructor
    public RecyclerViewAdapter_Surgery(ArrayList<Surgical_History> surgicalHistories) {
        this.surgicalHistories = surgicalHistories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_surgey_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Surgical_History surgicalHistory = surgicalHistories.get(position);
        holder.surgicalHistory = surgicalHistory;
        holder.tv_surgeryDate.setText(surgicalHistory.strDatePerformed);
        holder.tv_surgeryTitle.setText(surgicalHistory.surgeryTitle);
    }

    @Override
    public int getItemCount() {
        return surgicalHistories.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
         Surgical_History surgicalHistory;
        CardView cardView_surgicalHistory;
        TextView tv_surgeryDate, tv_surgeryTitle;

        public ViewHolder(View view) {
            super(view);
            cardView_surgicalHistory = (CardView) view.findViewById(R.id.cardView_surgicalHistory);
            cardView_surgicalHistory.setOnLongClickListener(this);
            tv_surgeryDate = (TextView) view.findViewById(R.id.tv_surgeryDate);
            tv_surgeryTitle = (TextView) view.findViewById(R.id.tv_surgeryTitle);
        }

         @Override
         public boolean onLongClick(View v) {
             BusStation.getBus().post(new Bus_SurgicalHistory_OnLongClick(getAdapterPosition(), surgicalHistory));
             return true;
         }
     }
}
