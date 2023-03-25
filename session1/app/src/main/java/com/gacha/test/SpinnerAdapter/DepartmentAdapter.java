package com.gacha.test.SpinnerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gacha.test.Model.Department;
import com.gacha.test.R;

import java.util.List;

public class DepartmentAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<Department> list;

    public DepartmentAdapter(Context context, List<Department> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dropdown_item, parent, false);
        }

        TextView txtTitle = convertView.findViewById(R.id.item);
        txtTitle.setText(list.get(position).getName());

        return convertView;
    }
}
