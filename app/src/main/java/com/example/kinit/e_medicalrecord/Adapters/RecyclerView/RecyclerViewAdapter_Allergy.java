package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.Allergy.Bus_Allergy;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Classes.Allergy.Allergy;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Allergy extends RecyclerView.Adapter<RecyclerViewAdapter_Allergy.ViewHolder> {
    ArrayList<Allergy> allergies;

    public RecyclerViewAdapter_Allergy(ArrayList<Allergy> allergies) {
        this.allergies = allergies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_allergy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_allergy.setText(allergies.get(position).fr);
        holder.tv_reaction.setText(allergies.get(position).reaction);
        holder.tv_treatment.setText(allergies.get(position).treatment);
    }

    @Override
    public int getItemCount() {
        return allergies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tv_allergy, tv_reaction, tv_treatment;
        CardView cardView;
        public ViewHolder(View itemView){
            super(itemView);
            tv_allergy = (TextView) itemView.findViewById(R.id.tv_allergy);
            tv_reaction = (TextView) itemView.findViewById(R.id.tv_reaction);
            tv_treatment = (TextView) itemView.findViewById(R.id.tv_treatment);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Allergy(getAdapterPosition(), allergies.get(getAdapterPosition())));
            return true;
        }
    }
}
