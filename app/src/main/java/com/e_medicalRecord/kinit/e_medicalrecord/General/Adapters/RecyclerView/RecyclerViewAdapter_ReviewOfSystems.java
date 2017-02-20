package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Bus.Bus_ConsultationRos_OnLongClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;
import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_ReviewOfSystems extends RecyclerView.Adapter<RecyclerViewAdapter_ReviewOfSystems.ViewHolder> {

    private ArrayList<Consultation_ROS> consultationRoses;

    public RecyclerViewAdapter_ReviewOfSystems(ArrayList<Consultation_ROS> consultationRoses) {
        this.consultationRoses = consultationRoses;
    }

    public void addItem(Consultation_ROS consultationRos) {
        this.consultationRoses.add(consultationRos);
        this.notifyItemInserted(consultationRoses.size() - 1);
    }

    public void updateItem(int position, Consultation_ROS consultationRos){
        this.consultationRoses.set(position, consultationRos);
        this.notifyItemChanged(position);
    }

    public void removeItem(int position){
        this.consultationRoses.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_review_of_systems, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Consultation_ROS consultationRos = consultationRoses.get(position);
        holder.consultationRos = consultationRos;
        holder.tv_examination.setText(consultationRos.item);
        holder.tv_remarks.setText(consultationRos.remarks);
    }

    @Override
    public int getItemCount() {
        return consultationRoses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        Consultation_ROS consultationRos;
        LinearLayout linearLayout;
        TextView tv_examination, tv_remarks;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_examination = (TextView) itemView.findViewById(R.id.tv_examination);
            tv_remarks = (TextView) itemView.findViewById(R.id.tv_remarks);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_ConsultationRos_OnLongClick(getAdapterPosition(), consultationRos));
            return true;
        }
    }
}
