package com.gacha.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView assetGroup;
        public TextView warrantyDate;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            assetGroup = v.findViewById(R.id.asset_group);
            warrantyDate = v.findViewById(R.id.warranty_date);
        }
    }

    public MyAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText("Toyota Hilux FAF321");
        holder.assetGroup.setText("Office center");
        holder.warrantyDate.setText("01/11/0014");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
