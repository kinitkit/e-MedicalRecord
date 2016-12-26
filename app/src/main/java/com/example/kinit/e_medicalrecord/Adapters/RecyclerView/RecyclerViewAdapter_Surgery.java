package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Surgical_History.Bus_Surgical_History_Item;
import com.example.kinit.e_medicalrecord.Classes.Surgical_History.Surgical_History;
import com.example.kinit.e_medicalrecord.Enum.Medical_Transaction;
import com.example.kinit.e_medicalrecord.R;

public class RecyclerViewAdapter_Surgery extends RecyclerView.Adapter<RecyclerViewAdapter_Surgery.ViewHolder> {
    Surgical_History surgical_history;
    int patient_id;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView_surgicalHistory;
        TextView tv_surgeryDate, tv_surgeryTitle;

        public ViewHolder(View view) {
            super(view);
            cardView_surgicalHistory = (CardView) view.findViewById(R.id.cardView_surgicalHistory);
            tv_surgeryDate = (TextView) view.findViewById(R.id.tv_surgeryDate);
            tv_surgeryTitle = (TextView) view.findViewById(R.id.tv_surgeryTitle);
        }
    }

    //Constructor
    public RecyclerViewAdapter_Surgery(Surgical_History surgical_history, int patient_id) {
        this.patient_id = patient_id;
        this.surgical_history = surgical_history;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_surgey_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_surgeryTitle.setText(surgical_history.getSurgicalTitleItem(position));
        holder.tv_surgeryDate.setText(String.valueOf(surgical_history.getSurgicalDateItem(position)));
        holder.cardView_surgicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_Surgical_History_Item(Medical_Transaction.UPDATE_SURGICAL_HISTORY, surgical_history.getSurgicalTitleItem(position), surgical_history.getCalendar(position).getTime().toString(),
                        surgical_history.getSurgicalIdItem(position), surgical_history.getSurgicalAttachName(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return surgical_history.getSize();
    }

    public void remove(int position, boolean isRemove) {
        if(isRemove){
            surgical_history.removeItem(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            notifyDataSetChanged();
        } else {
            notifyItemChanged(position);
        }
    }
}
