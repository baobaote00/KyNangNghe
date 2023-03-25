package com.gacha.test.Model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Asset implements Serializable {

    private int id;
    private String assetSN;
    private String assetName;
    private int departmentLocationID;
    private int employeeID;
    private int assetGroupID;
    private String description;
    private String warrantyDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetSN() {
        return assetSN;
    }

    public void setAssetSN(String assetSN) {
        this.assetSN = assetSN;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getDepartmentLocationID() {
        return departmentLocationID;
    }

    public void setDepartmentLocationID(int departmentLocationID) {
        this.departmentLocationID = departmentLocationID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getAssetGroupID() {
        return assetGroupID;
    }

    public void setAssetGroupID(int assetGroupID) {
        this.assetGroupID = assetGroupID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public Date getWarrantyDateFormat() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try {
            date = formatter.parse(this.warrantyDate);
        } catch (ParseException e) {
            Log.d("TAG", "getStartDateFormat: " + e.getMessage() + (this.warrantyDate.isEmpty()));
        }
        return date;
    }

    public boolean search(CharSequence key) {
        return this.assetSN.contains(key) || this.assetName.contains(key);
    }

    public Asset(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.getInt("ID"));
        this.setAssetSN(jsonObject.getString("AssetSN"));
        this.setAssetGroupID(jsonObject.getInt("AssetGroupID"));
        this.setAssetName(jsonObject.getString("AssetName"));
        this.setDescription(jsonObject.getString("Description"));
        this.setEmployeeID(jsonObject.getInt("EmployeeID"));
        this.setWarrantyDate(jsonObject.getString("WarrantyDate"));
        this.setDepartmentLocationID(jsonObject.getInt("DepartmentLocationID"));
    }

    @NonNull
    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", assetSN='" + assetSN + '\'' +
                ", assetName='" + assetName + '\'' +
                ", departmentLocationID=" + departmentLocationID +
                ", employeeID=" + employeeID +
                ", assetGroupID=" + assetGroupID +
                ", description='" + description + '\'' +
                ", warrantyDate='" + warrantyDate + '\'' +
                '}';
    }

    public int getDepartmentID(){
        String[] assetSNSplit = getAssetSN().split("/");

        return Integer.parseInt(assetSNSplit[0]);
    }

    public int getLocationID(List<DepartmentLocation> departmentLocations){
        for (int i = 0; i < departmentLocations.size(); i++) {
            if (departmentLocations.get(i).getId() == this.departmentLocationID){
                return departmentLocations.get(i).getLocationID();
            }
        }
        return -1;
    }

    public int getNNNN() {
        String[] assetSNSplit = getAssetSN().split("/");

        return Integer.parseInt(assetSNSplit[assetSNSplit.length - 1]);
    }

    public static int getNNNNNew(List<Asset> dataAsset, int departmentID, int assetGroupID) {
        List<Asset> assets = new ArrayList<>();

        for (int i = 0; i < dataAsset.size(); i++) {
            if (dataAsset.get(i).getDepartmentID() == departmentID
                    && dataAsset.get(i).getAssetGroupID() == assetGroupID) {
                assets.add(dataAsset.get(i));
            }
        }

        Asset assetNNNNMax = Collections.max(assets, (Asset a1, Asset a2) -> a1.getNNNN() - a2.getNNNN());

        return assetNNNNMax.getNNNN() + 1;
    }
}
