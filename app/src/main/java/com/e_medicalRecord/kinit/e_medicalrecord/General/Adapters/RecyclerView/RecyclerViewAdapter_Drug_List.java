package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Drug;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Bus.Bus_Open_Add_Drug;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Drug_List;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Enum.Query_Type;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Drug_List extends RecyclerView.Adapter<RecyclerViewAdapter_Drug_List.ViewHolder> {
    Drug_List[] drugLists;
    ArrayList<Bus_Drug> drugArrayList;

    public RecyclerViewAdapter_Drug_List(Drug_List[] drugLists) {
        this.drugLists = drugLists;
    }

    public RecyclerViewAdapter_Drug_List(ArrayList<Bus_Drug> drugArrayList) {
        this.drugArrayList = drugArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_drug_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(drugLists != null) {
            holder.tv_drug.setText(drugLists[position].drug);
            holder.tv_strength.setText(drugLists[position].strength);
            holder.tv_amount.setText(drugLists[position].dosage);
            holder.tv_route.setText(drugLists[position].route);
            holder.tv_frequency.setText(drugLists[position].frequency);
            holder.tv_why.setText(drugLists[position].indication);
            holder.tv_many.setText(String.valueOf(drugLists[position].many));
        } else {
            final String drug = drugArrayList.get(position).drug, strength = drugArrayList.get(position).strength,
                    amount = drugArrayList.get(position).amount, route = drugArrayList.get(position).route,
                    frequency = drugArrayList.get(position).frequency, why = drugArrayList.get(position).why,
                    many = drugArrayList.get(position).quantity;
            holder.tv_drug.setText(drugArrayList.get(position).drug);
            holder.tv_strength.setText(drugArrayList.get(position).strength);
            holder.tv_amount.setText(drugArrayList.get(position).amount);
            holder.tv_route.setText(drugArrayList.get(position).route);
            holder.tv_frequency.setText(drugArrayList.get(position).frequency);
            holder.tv_why.setText(drugArrayList.get(position).why);
            holder.tv_many.setText(drugArrayList.get(position).quantity);
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BusStation.getBus().post(new Bus_Open_Add_Drug(drug, strength, amount, route, frequency, why, many, Query_Type.UPDATE, position));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(drugLists != null)
            return drugLists.length;
        else
            return drugArrayList.size();
    }

    public void remove(int position, boolean isRemove){
        if(isRemove){
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            notifyDataSetChanged();
        } else {
            notifyItemChanged(position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_drug, tv_strength, tv_amount, tv_route, tv_frequency, tv_why, tv_many;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            tv_drug = (TextView) view.findViewById(R.id.tv_drug);
            tv_strength = (TextView) view.findViewById(R.id.tv_strength);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_route = (TextView) view.findViewById(R.id.tv_route);
            tv_frequency = (TextView) view.findViewById(R.id.tv_frequency);
            tv_why = (TextView) view.findViewById(R.id.tv_why);
            tv_many = (TextView) view.findViewById(R.id.tv_many);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }
}
