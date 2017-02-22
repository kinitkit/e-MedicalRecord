package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_LaboratoryFields extends RecyclerView.Adapter<RecyclerViewAdapter_LaboratoryFields.ViewHolder> {

    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(String key, Object value, String normal) {
        items.add(new Item(key, String.valueOf(value), normal));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_key, tv_value, tv_normal;
        LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            tv_key = (TextView) view.findViewById(R.id.tv_key);
            tv_value = (TextView) view.findViewById(R.id.tv_value);
            tv_normal = (TextView) view.findViewById(R.id.tv_normal);
            linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_layout_laboratory_field, parent, false);
        return new RecyclerViewAdapter_LaboratoryFields.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tv_key.setText(item.key);
        holder.tv_value.setText(item.value);
        if (item.normal == null) {
            holder.linearLayout.setVisibility(View.GONE);
        } else {
            holder.tv_normal.setText(item.normal);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Item {
        String key, value, normal;

        Item(String key, String value, String normal) {
            this.key = key;
            this.value = value;
            this.normal = normal;
        }
    }
}
