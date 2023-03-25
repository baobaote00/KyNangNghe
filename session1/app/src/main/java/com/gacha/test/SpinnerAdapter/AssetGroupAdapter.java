package com.gacha.test.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gacha.test.Model.AssetGroup;
import com.gacha.test.R;

import java.util.List;
import java.util.zip.Inflater;

public class AssetGroupAdapter extends BaseAdapter {
    private List<AssetGroup> dataAssetGroup;
    private Context context;
    private LayoutInflater inflater;

    public AssetGroupAdapter(List<AssetGroup> dataAssetGroup, Context context) {
        this.dataAssetGroup = dataAssetGroup;
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return dataAssetGroup.size();
    }

    @Override
    public Object getItem(int i) {
        return dataAssetGroup.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataAssetGroup.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.dropdown_item, viewGroup, false);
        }

        TextView txtTitle = view.findViewById(R.id.item);
        txtTitle.setText(dataAssetGroup.get(i).getName());

        return view;
    }
}
