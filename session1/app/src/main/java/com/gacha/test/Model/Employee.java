package com.gacha.test.Model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;

    public Employee( JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.getInt("ID"));
        this.setFirstName(jsonObject.getString("FirstName"));
        this.setLastName(jsonObject.getString("LastName"));
        this.setPhone(jsonObject.getString("Phone"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
