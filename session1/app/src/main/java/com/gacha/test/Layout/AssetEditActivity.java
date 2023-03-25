package com.gacha.test.Layout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.gacha.test.CallAPI;
import com.gacha.test.Model.Asset;
import com.gacha.test.Model.AssetGroup;
import com.gacha.test.Model.Department;
import com.gacha.test.Model.DepartmentLocation;
import com.gacha.test.Model.Employee;
import com.gacha.test.Model.Location;
import com.gacha.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssetEditActivity extends AppCompatActivity {
    private static final String TAG = "AssetEditActivity";
    EditText edtAssetName, edtAssetDescription;
    Spinner spDepartment, spLocation, spAssetGroup, spAccountableParty;
    TextView tvWarranty, tvAssetSN, btnCapture, btnBrowse, btnSubmit, btnCancel;
    ImageView imgAvatar;

    ArrayAdapter<Department> departmentAdapter;
    ArrayAdapter<AssetGroup> assetGroupAdapter;
    ArrayAdapter<Location> locationAdapter;
    ArrayAdapter<Employee> accountableAdapter;

    ArrayList<Location> dataLocation = new ArrayList<>();
    ArrayList<Location> dataLocationToAdapter = new ArrayList<>();
    ArrayList<DepartmentLocation> dataDepartmentLocation = new ArrayList<>();
    ArrayList<Department> dataDepartment = new ArrayList<>();
    ArrayList<AssetGroup> dataAssetGroups = new ArrayList<>();
    ArrayList<Employee> dataEmployees = new ArrayList<>();

    Asset asset;

    Uri uri;
    Bitmap bitmap;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat formatWarrantyDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private int type_image = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_asset);

        setControl();

        setAdapter();

        getIntentData();

        callAPI();

        setEvent();
    }

    private void getIntentData() {
        asset = (Asset) getIntent().getExtras().get("asset");
        List<Department> departments = (List<Department>) getIntent().getExtras().get("dataDepartment");
        dataDepartmentLocation.addAll((List<DepartmentLocation>) getIntent().getExtras().get("dataDepartmentLocation"));
        List<AssetGroup> assetGroups = (List<AssetGroup>) getIntent().getExtras().get("dataAssetGroup");

        dataEmployees.addAll((List<Employee>) getIntent().getExtras().get("dataEmployee"));
        accountableAdapter.notifyDataSetChanged();

        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == asset.getDepartmentID()) {
                dataDepartment.add(departments.get(i));
            }
        }
        departmentAdapter.notifyDataSetChanged();

        for (int i = 0; i < assetGroups.size(); i++) {
            if (assetGroups.get(i).getId() == asset.getAssetGroupID()) {
                dataAssetGroups.add(assetGroups.get(i));
            }
        }
        assetGroupAdapter.notifyDataSetChanged();

        edtAssetName.setText(asset.getAssetName());

        edtAssetDescription.setText(asset.getDescription());

        if (!asset.getWarrantyDate().equals("null")) {
            tvWarranty.setText(asset.getWarrantyDate());
        }
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

                for (int i = 0; i < dataLocation.size(); i++) {
                    if (dataLocation.get(i).getId() == asset.getLocationID(dataDepartmentLocation)) {
                        dataLocationToAdapter.add(dataLocation.get(i));
                    }
                }

                locationAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("TAG", "getListAssets: " + e.getMessage());
            }
        }, error -> Log.d(TAG, "getLocations:  " + error.getMessage()));
    }

    private void setControl() {
        edtAssetName = findViewById(R.id.edtAssetName);
        edtAssetDescription = findViewById(R.id.edtAssetDescription);
        spDepartment = findViewById(R.id.spDeparment);
        spLocation = findViewById(R.id.spLocation);
        spAssetGroup = findViewById(R.id.spAssetGroup);
        spAccountableParty = findViewById(R.id.spAccoutableParty);
        tvWarranty = findViewById(R.id.tvWarranty);
        tvAssetSN = findViewById(R.id.tvAssetSN);
        btnCapture = findViewById(R.id.btnCapture);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    private void setAdapter() {
        departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataDepartment);
        spDepartment.setAdapter(departmentAdapter);

        spDepartment.setEnabled(false);

        assetGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataAssetGroups);
        spAssetGroup.setAdapter(assetGroupAdapter);

        spAssetGroup.setEnabled(false);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataLocationToAdapter);
        spLocation.setAdapter(locationAdapter);

        spLocation.setEnabled(false);

        accountableAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataEmployees);
        spAccountableParty.setAdapter(accountableAdapter);
    }

    private void setEvent() {
        btnCapture.setOnClickListener(click -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with the intent and request code
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        });
        btnBrowse.setOnClickListener(click -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Set the MIME type to filter images
            intent.setType("image/*");
            // Start the activity with the intent and request code
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        tvWarranty.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AssetEditActivity.this, (v, year, monthOfYear, dayOfMonth) -> {
                // Lưu ngày được chọn vào biến Calendar
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (calendar.getTime().compareTo(new Date(System.currentTimeMillis())) <= 0) return;

                tvWarranty.setText(formatWarrantyDate.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnSubmit.setOnClickListener(click -> {
            String url = CallAPI.getLink("UpdateAsset");

            if (edtAssetName.getText().length() == 0) {
                Toast.makeText(this, "Asset Name Cannot null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtAssetDescription.getText().length() == 0) {
                Toast.makeText(this, "Asset Description Cannot null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tvWarranty.getText().equals(getResources().getString(R.string.expired_warranty))) {
                Toast.makeText(this, "Warranty Date Cannot null", Toast.LENGTH_SHORT).show();
                return;
            }

            int departmentID = ((Department) spDepartment.getSelectedItem()).getId();
            int assetGroupID = ((AssetGroup) spAssetGroup.getSelectedItem()).getId();
            int nnnn = asset.getNNNN();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID",asset.getId());
                jsonObject.put("AssetName", edtAssetName.getText());
                jsonObject.put("DepartmentLocationID", asset.getDepartmentLocationID());
                jsonObject.put("EmployeeID", ((Employee) spAccountableParty.getSelectedItem()).getId());
                jsonObject.put("AssetGroupID", asset.getAssetGroupID());
                jsonObject.put("Description", edtAssetDescription.getText());
                jsonObject.put("WarrantyDate", tvWarranty.getText());
                jsonObject.put("AssetSN", String.format(Locale.ENGLISH, "%02d/%02d/%04d", departmentID, assetGroupID, nnnn));
            } catch (JSONException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            CallAPI.useVolleyJSONObject(this, url, Request.Method.PUT, jsonObject,
                    response -> {
                        try {
                            putImage(response.getInt("insertId"));
                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(click -> finish());
    }

    private void putImage(int assetId) {
        String image_base64;
        String url = CallAPI.getLink("AddAssetPhoto");

        Toast.makeText(this, type_image+"", Toast.LENGTH_SHORT).show();

        if (type_image != 0) {
            if (type_image == PICK_IMAGE_REQUEST) {
                InputStream iStream = null;
                try {
                    iStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "putImage:  " + e.getMessage());
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeStream(iStream, null, options);

                if (bitmap.getWidth() > 640 || bitmap.getHeight() > 480) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, false);
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            image_base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("AssetID", assetId);
                jsonObject.put("AssetPhoto", image_base64);
            } catch (JSONException e) {
                Log.d(TAG, "putImage:  " + e.getMessage());
            }

            CallAPI.useVolleyJSONObject(this, url, Request.Method.POST, jsonObject,
                    response -> Log.d(TAG, "putImage: test " + response),
                    error -> Log.d(TAG, "putImage: test " + error.getMessage()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            uri = data.getData();
            imgAvatar.setImageURI(uri);
        }
        if (requestCode == CAPTURE_IMAGE_REQUEST && data != null && data.getExtras() != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgAvatar.setImageBitmap(bitmap);
        }
        type_image = requestCode;
    }
}

