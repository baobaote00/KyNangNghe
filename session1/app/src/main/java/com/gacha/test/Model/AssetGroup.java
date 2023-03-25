package com.gacha.test.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AssetGroup implements Serializable {
    private int id;
    private String name;

    public AssetGroup(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.getInt("ID"));
        this.setName(jsonObject.getString("Name"));
    }

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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
