package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Classes.Vaccination.Vaccination;
import com.example.kinit.e_medicalrecord.Classes.Vaccination.Vaccine;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_Vaccination extends RecyclerView.Adapter<RecyclerViewAdapter_Vaccination.ViewHolder> {

    ArrayList<Vaccine> vaccines;
    ArrayList<Vaccination> vaccinations;

    public RecyclerViewAdapter_Vaccination(ArrayList<Vaccine> vaccines, ArrayList<Vaccination> vaccinations) {
        this.vaccines = vaccines;
        this.vaccinations = vaccinations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_vaccination_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_date.setText(vaccinations.get(position).strDateTaken);
        holder.tv_vaccine.setText(vaccinations.get(position).vaccine);
    }

    @Override
    public int getItemCount() {
        return vaccinations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tv_date, tv_vaccine, tv_provider, tv_place, tv_nextSchedule;
        LinearLayout linear_layoutNextSchedule;
        CardView cardView;

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
            return false;
        }
    }

}
