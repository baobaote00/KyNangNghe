package com.gacha.test.Layout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.gacha.test.CallAPI;
import com.gacha.test.Model.Asset;
import com.gacha.test.Model.Department;
import com.gacha.test.Model.Location;
import com.gacha.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AssetTransferActivity extends AppCompatActivity {

    TextView tvAssetName, tvCurrentDepartment, tvAssetSN, tvAssetSNNew, btnSubmit, btnCancel;
    Spinner spDestinationDepartment, spDestinationLocation;
    Asset asset;

    ArrayAdapter<Department> departmentArrayAdapter;
    ArrayAdapter<Location> locationArrayAdapter;

    List<Location> dataLocation = new ArrayList<>();
    ArrayList<Department> dataDepartment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tranfer_asset);

        setControl();

        setAdapter();

        getIntentData();

        setEvent();

        callAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEvent() {
        btnCancel.setOnClickListener(click->finish());

        btnSubmit.setOnClickListener(click->{

        });
    }

    private void getIntentData() {
        asset = (Asset) getIntent().getExtras().get("asset");
        dataDepartment.addAll((List<Department>) getIntent().getExtras().get("dataDepartment"));

        tvAssetName.setText(asset.getAssetName());

        for (int i = 0; i < dataDepartment.size(); i++) {
            if (dataDepartment.get(i).getId() == asset.getDepartmentID()){
                tvCurrentDepartment.setText(dataDepartment.get(i).getName());
            }
        }

        tvAssetSN.setText(asset.getAssetSN());

        departmentArrayAdapter.notifyDataSetChanged();

        tvAssetSNNew.setText(String.format(Locale.ENGLISH, "??/%02d/????", asset.getAssetGroupID()));
    }

    private void setAdapter() {
        departmentArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataDepartment);
        spDestinationDepartment.setAdapter(departmentArrayAdapter);

        locationArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataLocation);
        spDestinationLocation.setAdapter(locationArrayAdapter);
    }

    private void setControl() {
        tvAssetName = findViewById(R.id.tvAssetName);
        tvCurrentDepartment = findViewById(R.id.tvCurrentDepartment);
        tvAssetSN = findViewById(R.id.tvAssetSN);
        tvAssetSNNew = findViewById(R.id.tvAssetSNNew);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        spDestinationDepartment = findViewById(R.id.spDestinationDepartment);
        spDestinationLocation = findViewById(R.id.spDestinationLocation);
    }

    private void callAPI() {
        getLocations();
    }

    private void getLocations() {
        String url = CallAPI.getLink("GetLocations");
        CallAPI.useVolley(this, url, Request.Method.GET, null, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    dataLocation.add(new Location(jsonObject));
                }
                locationArrayAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("TAG", "getListAssets: " + e.getMessage());
            }
        }, error -> Log.d("TAG", "getLocations:  " + error.getMessage()));
    }
}
