package com.gacha.test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gacha.test.Layout.AssetEditActivity;
import com.gacha.test.Layout.AssetTransferActivity;
import com.gacha.test.Model.Asset;
import com.gacha.test.Model.AssetGroup;
import com.gacha.test.Model.Department;
import com.gacha.test.Model.DepartmentLocation;
import com.gacha.test.Model.Employee;
import com.gacha.test.Model.Location;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final List<Asset> dataAsset;
    private final List<Asset> dataAssetToAdapter;
    private final List<Department> dataDepartment;
    private final TextView numberOfRecord;
    private final Context context;
    private List<DepartmentLocation> dataDepartmentLocation;
    private List<Employee> dataEmployee;
    private List<AssetGroup> dataAssetGroup;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView departmentName;
        public TextView assetSN;
        public ImageButton btnEdit,btnTranfer,btnTranferHistory;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            departmentName = v.findViewById(R.id.department_name);
            assetSN = v.findViewById(R.id.asset_sn);

            btnEdit = v.findViewById(R.id.btn_edit);
            btnTranfer = v.findViewById(R.id.btn_tranfer);
            btnTranferHistory = v.findViewById(R.id.btn_tranfer_history);
        }
    }

    public MyAdapter(Context context, List<Asset> dataAssetToAdapter, List<Department> dataDepartment, TextView numberOfRecord, List<Asset> dataAsset) {
        this.context = context;
        this.dataAsset = dataAsset;
        this.dataDepartment = dataDepartment;

        this.dataAssetToAdapter = dataAssetToAdapter;
        this.numberOfRecord = numberOfRecord;
    }

    public void notifyData() {
        super.notifyDataSetChanged();
        numberOfRecord.setText(String.format(Locale.ENGLISH, "%d form %s", dataAssetToAdapter.size(), dataAsset.size()));
    }

    public void setData(List<DepartmentLocation> dataDepartmentLocation,List<Employee> dataEmployee,List<AssetGroup> dataAssetGroup){
        this.dataDepartmentLocation = dataDepartmentLocation;
        this.dataEmployee = dataEmployee;
        this.dataAssetGroup = dataAssetGroup;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(dataAssetToAdapter.get(position).getAssetName());
        for (Department department : dataDepartment) {
            if (department.getId() != dataAssetToAdapter.get(position).getDepartmentID())
                continue;

            holder.departmentName.setText(department.getName());
            break;
        }

        holder.assetSN.setText(dataAssetToAdapter.get(position).getAssetSN());

        holder.btnEdit.setOnClickListener(click->{
            Intent intent = new Intent(context, AssetEditActivity.class);
            intent.putExtra("asset", dataAssetToAdapter.get(position));
            intent.putExtra("dataDepartment",(Serializable) dataDepartment);
            intent.putExtra("dataDepartmentLocation",(Serializable) dataDepartmentLocation);
            intent.putExtra("dataEmployee",(Serializable) dataEmployee);
            intent.putExtra("dataAssetGroup",(Serializable) dataAssetGroup);
            context.startActivity(intent);
        });

        holder.btnTranfer.setOnClickListener(click->{
            Intent intent = new Intent(context, AssetTransferActivity.class);
            intent.putExtra("asset", dataAssetToAdapter.get(position));
            intent.putExtra("dataDepartment",(Serializable) dataDepartment);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataAssetToAdapter.size();
    }
}
