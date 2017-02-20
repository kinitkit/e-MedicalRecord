package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Bus.Bus_Search_OnClick;
import com.e_medicalRecord.kinit.e_medicalrecord.Profile.Class.Search_User;
import com.e_medicalRecord.kinit.e_medicalrecord.R;
import com.e_medicalRecord.kinit.e_medicalrecord.General.Request.Glide_ImgLoader;

import java.util.ArrayList;

public class RecyclerViewAdapter_Search extends RecyclerView.Adapter<RecyclerViewAdapter_Search.ViewHolder> {
    private ArrayList<Search_User> searchUsers;

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
        }
    }
}
