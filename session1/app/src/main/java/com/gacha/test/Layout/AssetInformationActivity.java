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
import com.gacha.test.Model.Employee;
import com.gacha.test.Model.Location;
import com.gacha.test.R;
import com.gacha.test.SpinnerAdapter.AccountablePartyAdapter;
import com.gacha.test.SpinnerAdapter.AssetGroupAdapter;
import com.gacha.test.SpinnerAdapter.DepartmentAdapter;
import com.gacha.test.SpinnerAdapter.LocationAdapter;

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

public class AssetInformationActivity extends AppCompatActivity {
    private static final String TAG = AssetInformationActivity.class.getSimpleName();

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;

    EditText edtAssetName, edtAssetDescription;
    Spinner spDepartment, spLocation, spAssetGroup, spAccountableParty;
    TextView tvWarranty, tvAssetSN, btnCapture, btnBrowse, btnSubmit, btnCancel;
    ImageView imgAvatar;

    private final Calendar calendar = Calendar.getInstance();

    private DepartmentAdapter adapterDepartment;
    private AssetGroupAdapter adapterAssetGroup;
    private AccountablePartyAdapter adapterAccountableParty;
    private LocationAdapter adapterLocation;

    private final List<Department> dataDepartment = new ArrayList<>();
    private final List<Asset> dataAsset = new ArrayList<>();
    private final List<AssetGroup> dataAssetGroup = new ArrayList<>();
    private final List<Employee> dataEmployee = new ArrayList<>();
    private final List<Location> dataLocation = new ArrayList<>();
    private final SimpleDateFormat formatWarrantyDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    int type_image = 0;
    Uri uri;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_asset);

        setControl();

        getIntentData();

        callAPI();

        setAdapter();

        setEvent();

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
        tvWarranty.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AssetInformationActivity.this, (v, year, monthOfYear, dayOfMonth) -> {
                // Lưu ngày được chọn vào biến Calendar
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (calendar.getTime().compareTo(new Date(System.currentTimeMillis())) <= 0) return;

                tvWarranty.setText(formatWarrantyDate.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnCapture.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with the intent and request code
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        });

        btnBrowse.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Set the MIME type to filter images
            intent.setType("image/*");
            // Start the activity with the intent and request code
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnCancel.setOnClickListener(click -> finish());

        btnSubmit.setOnClickListener(click -> {
            String url = CallAPI.getLink("AddAsset");

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
            int nnnn = Asset.getNNNNNew(dataAsset, departmentID, assetGroupID);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("AssetName", edtAssetName.getText());
                jsonObject.put("DepartmentLocationID", ((Department) spDepartment.getSelectedItem()).getId());
                jsonObject.put("EmployeeID", ((Employee) spAccountableParty.getSelectedItem()).getId());
                jsonObject.put("AssetGroupID", ((AssetGroup) spAssetGroup.getSelectedItem()).getId());
                jsonObject.put("Description", edtAssetDescription.getText());
                jsonObject.put("WarrantyDate", tvWarranty.getText());
                jsonObject.put("AssetSN", String.format(Locale.ENGLISH, "%02d/%02d/%04d", departmentID, assetGroupID, nnnn));
            } catch (JSONException e) {
                Log.d(TAG, "putImage:  " + e.getMessage());
            }


            CallAPI.useVolleyJSONObject(this, url, Request.Method.POST, jsonObject,
                    response -> {
                        try {
                            putImage(response.getInt("insertId"));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(this, "putImage: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        });
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
                adapterLocation.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("TAG", "getListAssets: " + e.getMessage());
            }
        }, error -> Log.d(TAG, "getLocations:  " + error.getMessage()));
    }

    private void getIntentData() {
        dataDepartment.addAll((List<Department>) getIntent().getExtras().get("dataDepartment"));
        dataAssetGroup.addAll((List<AssetGroup>) getIntent().getExtras().get("dataAssetGroup"));
        dataEmployee.addAll((List<Employee>) getIntent().getExtras().get("dataEmployee"));
        dataAsset.addAll((List<Asset>) getIntent().getExtras().get("dataAsset"));
    }

    private void setAdapter() {
        adapterDepartment = new DepartmentAdapter(this, dataDepartment);
        spDepartment.setAdapter(adapterDepartment);

        adapterAssetGroup = new AssetGroupAdapter(dataAssetGroup, this);
        spAssetGroup.setAdapter(adapterAssetGroup);

        adapterAccountableParty = new AccountablePartyAdapter(dataEmployee, this);
        spAccountableParty.setAdapter(adapterAccountableParty);

        adapterLocation = new LocationAdapter(dataLocation, this);
        spLocation.setAdapter(adapterLocation);
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

    private void putImage(int assetId) {
        String image_base64;
        String url = CallAPI.getLink("AddAssetPhoto");

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
