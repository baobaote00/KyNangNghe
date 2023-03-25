package com.gacha.test.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gacha.test.Model.Employee;
import com.gacha.test.R;

import java.util.List;

public class AccountablePartyAdapter extends BaseAdapter {

    private final List<Employee> dataEmployee;
    private final LayoutInflater inflater;

    public AccountablePartyAdapter(List<Employee> dataEmployee, Context context) {
        this.dataEmployee = dataEmployee;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataEmployee.size();
    }

    @Override
    public Object getItem(int i) {
        return dataEmployee.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataEmployee.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.dropdown_item, viewGroup, false);
        }

        Employee employee = dataEmployee.get(i);

        TextView txtTitle = view.findViewById(R.id.item);
        txtTitle.setText(String.format("%s %s", employee.getFirstName(), employee.getLastName()));

        return view;
    }


}
