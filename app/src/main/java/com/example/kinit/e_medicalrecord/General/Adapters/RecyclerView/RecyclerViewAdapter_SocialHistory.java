package com.example.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kinit.e_medicalrecord.General.BusStation.BusStation;
import com.example.kinit.e_medicalrecord.Social_History.Bus.Bus_SocialHistory_OnLongClick;
import com.example.kinit.e_medicalrecord.Social_History.Class.Social_History;
import com.example.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_SocialHistory extends RecyclerView.Adapter<RecyclerViewAdapter_SocialHistory.ViewHolder> {
    ArrayList<Social_History> socialHistories;

    public RecyclerViewAdapter_SocialHistory(ArrayList<Social_History> socialHistories) {
        this.socialHistories = socialHistories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_layout_social_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Social_History socialHistory = socialHistories.get(position);
        holder.socialHistory = socialHistory;
        holder.tv_subtance.setText(socialHistory.substance);
        holder.cb_currentlyUse.setChecked(socialHistory.currentlyUse);
        holder.cb_previouslyUsed.setChecked(socialHistory.previouslyUsed);
        holder.tv_frequency.setText(socialHistory.frequency);
        holder.tv_length.setText(String.valueOf(socialHistory.length));
        holder.tv_stopped.setText(String.valueOf(socialHistory.stoppedYear));
    }

    @Override
    public int getItemCount() {
        return socialHistories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        Social_History socialHistory;
        CardView cardView;
        CheckBox cb_currentlyUse, cb_previouslyUsed;
        TextView tv_subtance, tv_frequency, tv_length, tv_stopped;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_subtance = (TextView) itemView.findViewById(R.id.tv_substance);
            cb_currentlyUse = (CheckBox) itemView.findViewById(R.id.cb_currentlyUse);
            cb_previouslyUsed = (CheckBox) itemView.findViewById(R.id.cb_previouslyUsed);
            tv_frequency = (TextView) itemView.findViewById(R.id.tv_frequency);
            tv_length = (TextView) itemView.findViewById(R.id.tv_length);
            tv_stopped = (TextView) itemView.findViewById(R.id.tv_stopped);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            BusStation.getBus().post(new Bus_SocialHistory_OnLongClick(getAdapterPosition(), socialHistory));
            return true;
        }
    }
}
