package com.gacha.test;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gacha.test.Model.Asset;
import com.gacha.test.Model.Department;
import com.gacha.test.Model.DepartmentLocation;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Asset> dataAsset;
    private List<Department> dataDepartment;
    private List<DepartmentLocation> dataDepartmentLocation;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView departmentName;
        public TextView assetSN;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            departmentName = v.findViewById(R.id.department_name);
            assetSN = v.findViewById(R.id.asset_sn);
        }
    }

    public MyAdapter(List<Asset> dataAsset,List<DepartmentLocation> dataDepartmentLocation,List<Department> dataDepartment) {
        this.dataAsset = dataAsset;
        this.dataDepartment = dataDepartment;
        this.dataDepartmentLocation = dataDepartmentLocation;

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
        holder.name.setText(dataAsset.get(position).getAssetName());

        for (DepartmentLocation departmentLocation: dataDepartmentLocation) {
            if (departmentLocation.getId() ==dataAsset.get(position).getDepartmentLocationID()){
                for (Department department : dataDepartment) {
                    if (department.getId() == departmentLocation.getDepartmentID()){
                        holder.departmentName.setText(department.getName());
                        break;
                    }
                }
                break;
            }
        }

        holder.assetSN.setText(dataAsset.get(position).getAssetSN());
    }

    @Override
    public int getItemCount() {
        return dataAsset.size();
    }
}
