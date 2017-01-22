package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Add_Physician;
import com.example.kinit.e_medicalrecord.BusStation.Medical_Prescription.Bus_Remove_Physician;
import com.example.kinit.e_medicalrecord.Classes.Medical_Prescription.Tagged_Physician_List;
import com.example.kinit.e_medicalrecord.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Glide_ImgLoader;

import java.util.ArrayList;

public class RecyclerViewAdapter_Tagged_MedPrescription extends RecyclerView.Adapter<RecyclerViewAdapter_Tagged_MedPrescription.ViewHolder> {
    ArrayList<Tagged_Physician_List> taggedPhysicianLists;
    My_Physician_Button_Mode buttonMode;

    public RecyclerViewAdapter_Tagged_MedPrescription(ArrayList<Tagged_Physician_List> taggedPhysicianLists, My_Physician_Button_Mode buttonMode) {
        this.taggedPhysicianLists = taggedPhysicianLists;
        this.buttonMode = buttonMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_myphysician, parent, false);
        return new ViewHolder(view, buttonMode);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tagged_Physician_List taggedPhysicianList = taggedPhysicianLists.get(position);
        Glide_ImgLoader.with(holder.iv_profilePic.getContext(), taggedPhysicianList.image, 0, 0,holder.iv_profilePic);
        holder.tv_name.setText(taggedPhysicianList.name);
    }

    @Override
    public int getItemCount() {
        return taggedPhysicianLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_profilePic;
        TextView tv_name;
        Button btn_add, btn_remove;

        public ViewHolder(View itemView, My_Physician_Button_Mode buttonMode) {
            super(itemView);
            iv_profilePic = (ImageView) itemView.findViewById(R.id.iv_profilePic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_remove = (Button) itemView.findViewById(R.id.btn_remove);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);

            switch (buttonMode) {
                case ADD:
                    btn_remove.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                    btn_add.setOnClickListener(this);
                    break;
                case REMOVE:
                    btn_add.setVisibility(View.GONE);
                    btn_remove.setVisibility(View.VISIBLE);
                    btn_remove.setOnClickListener(this);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    BusStation.getBus().post(new Bus_Add_Physician(getAdapterPosition(), taggedPhysicianLists.get(getAdapterPosition())));
                    break;
                case R.id.btn_remove:
                    BusStation.getBus().post(new Bus_Remove_Physician(getAdapterPosition(), taggedPhysicianLists.get(getAdapterPosition())));
                    break;
            }
        }
    }
}
