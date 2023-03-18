package com.gacha.test.Model;

public class Asset {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public Asset(int id, String assetSN, String assetName, int departmentLocationID, int employeeID, int assetGroupID, String description, String warrantyDate) {
        this.id = id;
        this.assetSN = assetSN;
        this.assetName = assetName;
        this.departmentLocationID = departmentLocationID;
        this.employeeID = employeeID;
        this.assetGroupID = assetGroupID;
        this.description = description;
        this.warrantyDate = warrantyDate;
    }

    public Asset() {
    }

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
}
