package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Vaccination.Bus.Bus_Vaccination_OnLongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Vaccination.Class.Vaccination;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Vaccination extends RecyclerView.Adapter<RecyclerViewAdapter_Vaccination.ViewHolder> {

    private ArrayList<Vaccination> vaccinations;

    public RecyclerViewAdapter_Vaccination(ArrayList<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_vaccination_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Vaccination vaccination = vaccinations.get(position);
        holder.vaccination = vaccination;
        holder.tv_date.setText(vaccination.strDateTaken);
        holder.tv_vaccine.setText(vaccination.item);
        holder.tv_provider.setText(vaccination.providerName);
        holder.tv_place.setText(vaccination.placeTaken);
        switch (vaccination.scheduleStatus){
            case 1:
                holder.cardView.setBackgroundColor(Color.parseColor("#FFFDE7"));
                break;
            case -1:
                holder.cardView.setBackgroundColor(Color.parseColor("#FBE9E7"));
                break;
            default:
                holder.cardView.setBackgroundColor(Color.WHITE);
                break;
        }
        if (vaccination.strNextSchedule != null) {
            holder.linear_layoutNextSchedule.setVisibility(View.VISIBLE);
            holder.tv_nextSchedule.setText(vaccination.strNextSchedule);
        } else {
            holder.linear_layoutNextSchedule.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return vaccinations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tv_date, tv_vaccine, tv_provider, tv_place, tv_nextSchedule;
        LinearLayout linear_layoutNextSchedule;
        CardView cardView;
        Vaccination vaccination;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_vaccine = (TextView) itemView.findViewById(R.id.tv_vaccine);
            tv_provider = (TextView) itemView.findViewById(R.id.tv_provider);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
            tv_nextSchedule = (TextView) itemView.findViewById(R.id.tv_nextSchedule);
            linear_layoutNextSchedule = (LinearLayout) itemView.findViewById(R.id.linear_layoutNextSchedule);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_Vaccination_OnLongClick(getAdapterPosition(), vaccination));
            return true;
        }
    }
}
