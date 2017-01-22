package com.example.kinit.e_medicalrecord.Adapters.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kinit.e_medicalrecord.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_Item_OnClick;
import com.example.kinit.e_medicalrecord.BusStation.Search.Bus_Search_OnClick;
import com.example.kinit.e_medicalrecord.Classes.Search.Search_Item;
import com.example.kinit.e_medicalrecord.Classes.Search.Search_User;
import com.example.kinit.e_medicalrecord.R;
import com.example.kinit.e_medicalrecord.Request.Glide_ImgLoader;
import com.example.kinit.e_medicalrecord.Request.UrlString;

import java.util.ArrayList;

public class RecyclerViewAdapter_Search extends RecyclerView.Adapter<RecyclerViewAdapter_Search.ViewHolder> {
    ArrayList<Search_User> searchUsers;

    //Constructor
    public RecyclerViewAdapter_Search(ArrayList<Search_User> searchUsers) {
        this.searchUsers = searchUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Search_User searchUser = searchUsers.get(position);
        Glide_ImgLoader.with(holder.iv_profilePic.getContext(), searchUser.image, 0, 0,holder.iv_profilePic);
        holder.searchUser = searchUser;
        holder.tv_name.setText(searchUser.fullName);
        if(searchUser.userType != null){
            holder.tv_medicalType.setText(searchUser.userType);
        } else {
            holder.tv_medicalType.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Search_User searchUser;
        LinearLayout linear_layout;
        ImageView iv_profilePic;
        TextView tv_name, tv_medicalType;

        public ViewHolder(View view) {
            super(view);
            linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
            iv_profilePic = (ImageView) view.findViewById(R.id.iv_profilePic);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_medicalType = (TextView) view.findViewById(R.id.tv_medicalType);

            linear_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BusStation.getBus().post(new Bus_Search_OnClick(getAdapterPosition(), searchUser));
            /*BusStation.getBus().post(new Bus_Search_Item_OnClick(search_item.user_id.get(position), search_item.patient_id.get(position),
                    search_item.medicalStaff_id.get(position)));*/
        }
    }
}
