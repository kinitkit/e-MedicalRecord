package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_Item_OnClick;
import com.example.kinit.e_medicalrecord.Classes.Search.Search_Item;
import com.example.kinit.e_medicalrecord.R;

public class RecyclerViewAdapter_Search extends RecyclerView.Adapter<RecyclerViewAdapter_Search.ViewHolder> {
    Search_Item search_item;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_layout;
        ImageView iv_profilePic;
        TextView tv_name, tv_medicalType;

        public ViewHolder(View view) {
            super(view);
            linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
            iv_profilePic = (ImageView) view.findViewById(R.id.iv_profilePic);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_medicalType = (TextView) view.findViewById(R.id.tv_medicalType);
        }
    }

    //Constructor
    public RecyclerViewAdapter_Search(Search_Item search_item) {
        this.search_item = search_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (search_item.medicalType.get(position).isEmpty()) {
            holder.tv_medicalType.setVisibility(View.GONE);
        } else {
            holder.tv_medicalType.setText(search_item.medicalType.get(position));
        }
        holder.tv_name.setText(search_item.name.get(position));
        holder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new Bus_Search_Item_OnClick(search_item.user_id.get(position), search_item.patient_id.get(position),
                        search_item.medicalStaff_id.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return search_item.name.size();
    }
}
