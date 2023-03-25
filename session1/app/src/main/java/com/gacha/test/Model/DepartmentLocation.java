package com.gacha.test.Model;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DepartmentLocation implements Serializable {
    private int id;
    private int departmentID;
    private int locationID;
    private String startDate;
    private String endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Date getStartDateFormat() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try {
            date = formatter.parse(this.startDate);
        } catch (ParseException e) {
            Log.d("TAG", "getStartDateFormat: " + e.getMessage());
        }
        return date;
    }

    public Date getDateFormat() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try {
            date = formatter.parse(this.endDate);
        } catch (ParseException e) {
            Log.d("TAG", "getStartDateFormat: " + e.getMessage());
        }
        return date;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public DepartmentLocation(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.getInt("ID"));
        this.setDepartmentID(jsonObject.getInt("DepartmentID"));
        this.setLocationID(jsonObject.getInt("LocationID"));
        this.setStartDate(jsonObject.getString("StartDate"));
        this.setEndDate(jsonObject.getString("EndDate"));
    }
}
