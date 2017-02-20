package com.e_medicalRecord.kinit.e_medicalrecord.General.Adapters.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_medicalRecord.kinit.e_medicalrecord.R;

import java.util.ArrayList;

public class RecyclerViewAdapter_LaboratoryFields extends RecyclerView.Adapter<RecyclerViewAdapter_LaboratoryFields.ViewHolder> {

    ArrayList<Item> items = new ArrayList<>();

    public void addItem(String key, Object value) {
        items.add(new Item(key, String.valueOf(value)));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_key, tv_value;

        ViewHolder(View view) {
            super(view);
            tv_key = (TextView) view.findViewById(R.id.tv_key);
            tv_value = (TextView) view.findViewById(R.id.tv_value);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_layout_laboratory_field, parent, false);
        return new RecyclerViewAdapter_LaboratoryFields.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_key.setText(items.get(position).key);
        holder.tv_value.setText(items.get(position).value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Item {
        String key, value;

        public Item(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
