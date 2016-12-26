package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.Classes.My_Physician.Physician_List;
import com.example.kinit.e_medicalrecord.Enum.My_Physician_Button_Mode;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_SearchMyPhysician extends RecyclerView.Adapter<RecyclerViewAdapter_SearchMyPhysician.ViewHolder> {

    ArrayList<Physician_List> physicianLists = new ArrayList<>();
    My_Physician_Button_Mode buttonMode;

    public RecyclerViewAdapter_SearchMyPhysician(ArrayList<Physician_List> physicianLists, My_Physician_Button_Mode buttonMode) {
        this.buttonMode = buttonMode;
        this.physicianLists = physicianLists;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        Button btn_add, btn_remove;

        public ViewHolder(View itemView, My_Physician_Button_Mode buttonMode) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_remove = (Button) itemView.findViewById(R.id.btn_remove);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);

            switch (buttonMode) {
                case ADD:
                    btn_remove.setVisibility(View.INVISIBLE);
                    btn_add.setVisibility(View.VISIBLE);
                    btn_add.setOnClickListener(this);
                    break;
                case REMOVE:
                    btn_add.setVisibility(View.INVISIBLE);
                    btn_remove.setVisibility(View.VISIBLE);
                    btn_remove.setOnClickListener(this);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    Log.d("error", physicianLists.get(getAdapterPosition()).name);
                    break;
                case R.id.btn_remove:
                     Log.d("error", physicianLists.get(getAdapterPosition()).name);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_myphysician, parent, false);
        return new ViewHolder(view, buttonMode);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_name.setText(physicianLists.get(position).name);
    }

    @Override
    public int getItemCount() {
        return physicianLists.size();
    }
}
