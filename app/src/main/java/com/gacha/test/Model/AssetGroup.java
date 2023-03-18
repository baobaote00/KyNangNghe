package com.gacha.test.Model;

import androidx.annotation.NonNull;

public class AssetGroup {
    private int id;
    private String name;

    public AssetGroup() {
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
        return "AssetGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
