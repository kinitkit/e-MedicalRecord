package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.My_Physician.Bus.Bus_Add_Physician;
import com.e_medicalRecord.kinit.e_medicalrecord.My_Physician.Bus.Bus_Remove_Physician;
import com.e_medicalRecord.kinit.e_medicalrecord.My_Physician.Class.Physician_List;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Enum.My_Physician_Button_Mode;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.UrlString;

import java.util.ArrayList;

public class RecyclerViewAdapter_SearchMyPhysician extends RecyclerView.Adapter<RecyclerViewAdapter_SearchMyPhysician.ViewHolder> {

    private ArrayList<Physician_List> physicianLists = new ArrayList<>();
    private My_Physician_Button_Mode buttonMode;

    public RecyclerViewAdapter_SearchMyPhysician(ArrayList<Physician_List> physicianLists, My_Physician_Button_Mode buttonMode) {
        this.buttonMode = buttonMode;
        this.physicianLists = physicianLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_myphysician, parent, false);
        return new ViewHolder(view, buttonMode);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Physician_List physicianList = physicianLists.get(position);
        holder.physicianList = physicianList;

        Glide.with(holder.iv_profilePic.getContext())
                .load(UrlString.getImageUrl(physicianList.image))
                .error(R.mipmap.icon_user_default)
                .into(holder.iv_profilePic);

        holder.tv_name.setText(physicianList.name);
    }

    @Override
    public int getItemCount() {
        return physicianLists.size();
    }

    public void removeItem(int position) {
        physicianLists.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Physician_List physicianList;
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
                    BusStation.getBus().post(new Bus_Add_Physician(getAdapterPosition(), physicianList));
                    break;
                case R.id.btn_remove:
                    BusStation.getBus().post(new Bus_Remove_Physician(getAdapterPosition(), physicianList));
                    break;
            }
        }
    }
}
