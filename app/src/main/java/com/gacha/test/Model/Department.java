package com.gacha.test.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Department implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.getInt("ID"));
        this.setName(jsonObject.getString("Name"));
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
