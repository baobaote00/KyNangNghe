package com.gacha.test.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gacha.test.Model.Location;
import com.gacha.test.R;

import java.util.List;

public class LocationAdapter extends BaseAdapter {
    List<Location> dataLocation;
    Context context;
    LayoutInflater layoutInflater;

    public LocationAdapter(List<Location> dataLocation, Context context) {
        this.dataLocation = dataLocation;
        this.context = context;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return dataLocation.size();
    }

    @Override
    public Object getItem(int i) {
        return dataLocation.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataLocation.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.dropdown_item,viewGroup,false);
        }

        TextView textView = view.findViewById(R.id.item);
        textView.setText(dataLocation.get(i).getName());

        return view;
    }
}
