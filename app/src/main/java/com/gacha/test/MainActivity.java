package com.gacha.test;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gacha.test.Model.Asset;
import com.gacha.test.Model.AssetGroup;
import com.gacha.test.Model.Department;
import com.gacha.test.Model.DepartmentLocation;
import com.gacha.test.Model.Employee;
import com.gacha.test.SpinnerAdapter.AccountablePartyAdapter;
import com.gacha.test.SpinnerAdapter.AssetGroupAdapter;
import com.gacha.test.SpinnerAdapter.DepartmentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static android.widget.AdapterView.*;

public class MainActivity extends AppCompatActivity {

    private Spinner spDepartment, spAssetGroup, spAccountableParty;
    private DepartmentAdapter adapterDepartment;
    private AssetGroupAdapter adapterAssetGroup;
    private AccountablePartyAdapter adapterAccountableParty;

    private RecyclerView recyclerView;
    private Adapter mAdapter;

    private final List<Asset> dataAsset = new ArrayList<>();
    private final List<Asset> dataAssetToAdapter = new ArrayList<>();

    private final List<DepartmentLocation> dataDepartmentLocation = new ArrayList<>();
    private final List<Department> dataDepartment = new ArrayList<>();
    private final List<AssetGroup> dataAssetGroup = new ArrayList<>();
    private final List<Employee> dataEmployee = new ArrayList<>();

    boolean firstTimeDepartment = true;
    boolean firstTimeAssetGroup = true;
    boolean firstTimeEmployee = true;

    private EditText startDate, endDate, search;
    Calendar calendar = Calendar.getInstance();

    private Date startDateValue, endDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();

        setAdapter();

        callAPI();

        setEvent();
    }

    private void setEvent() {
        spDepartment.setOnItemSelectedListener(new OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstTimeDepartment) {
                    List<DepartmentLocation> list = dataDepartmentLocation.stream()
                            .filter(e -> e.getDepartmentID() == ((Department) adapterView.getItemAtPosition(i)).getId()).collect(Collectors.toList());

                    dataAssetToAdapter.clear();

                    for (int j = 0; j < list.size(); j++) {
                        for (int k = 0; k < dataAsset.size(); k++) {
                            if (list.get(j).getId() == dataAsset.get(k).getDepartmentLocationID()) {
                                dataAssetToAdapter.add(dataAsset.get(k));
                            }
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                } else {
                    firstTimeDepartment = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spAssetGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstTimeAssetGroup) {
                    List<Asset> list = dataAsset.stream()
                            .filter(e -> e.getAssetGroupID() == ((AssetGroup) adapterView.getItemAtPosition(i)).getId()).collect(Collectors.toList());

                    dataAssetToAdapter.clear();
                    dataAssetToAdapter.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    firstTimeAssetGroup = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spAccountableParty.setOnItemSelectedListener(new OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstTimeEmployee) {
                    List<Asset> list = dataAsset.stream()
                            .filter(e -> e.getEmployeeID() == ((Employee) adapterView.getItemAtPosition(i)).getId()).collect(Collectors.toList());

                    dataAssetToAdapter.clear();
                    dataAssetToAdapter.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    firstTimeEmployee = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startDate.setOnFocusChangeListener((view, b) -> {
            if (b) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (v, year, monthOfYear, dayOfMonth) -> {
                    // Lưu ngày được chọn vào biến Calendar
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Hiển thị ngày được chọn
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    if (endDateValue == null) return;

                    if (endDateValue.compareTo(calendar.getTime()) < 0) return;

                    startDateValue = calendar.getTime();
                    startDate.setText(sdf.format(calendar.getTime()));

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        endDate.setOnFocusChangeListener((view, b) -> {
            if (b) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (v, year, monthOfYear, dayOfMonth) -> {
                    // Lưu ngày được chọn vào biến Calendar
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Hiển thị ngày được chọn
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    if (startDateValue == null) return;

                    if (startDateValue.compareTo(calendar.getTime()) > 0) return;

                    endDateValue = calendar.getTime();
                    endDate.setText(sdf.format(calendar.getTime()));

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        //add event listener
//        edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("EditText", "Giá trị mới: " + s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    private void callAPI() {
        getListAssets();
        getListDepartment();
        getListDepartmentLocation();
        getListAssetGroup();
        getListEmployee();
    }

    private void setAdapter() {
        //set adapter
        adapterDepartment = new DepartmentAdapter(this, dataDepartment);
        spDepartment.setAdapter(adapterDepartment);

        adapterAssetGroup = new AssetGroupAdapter(dataAssetGroup, this);
        spAssetGroup.setAdapter(adapterAssetGroup);

        adapterAccountableParty = new AccountablePartyAdapter(dataEmployee, this);
        spAccountableParty.setAdapter(adapterAccountableParty);


        //config layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(dataAssetToAdapter, dataDepartmentLocation, dataDepartment);
        recyclerView.setAdapter(mAdapter);
    }

    private void setControl() {
        spDepartment = findViewById(R.id.department_spinner);
        spAssetGroup = findViewById(R.id.my_spinner_2);
        spAccountableParty = findViewById(R.id.my_spinner_3);

        recyclerView = findViewById(R.id.recycler_view);

        startDate = findViewById(R.id.start_date);
        startDate.setInputType(InputType.TYPE_NULL);

        endDate = findViewById(R.id.end_date);
        endDate.setInputType(InputType.TYPE_NULL);

        search = findViewById(R.id.search);
    }

    private void useVolley(String url, Response.Listener<org.json.JSONArray> listener) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, error -> Log.d("TAG", "onCreate: " + error));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getListAssets() {
        String url = getLink("GetAssets");
        useVolley(url, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    Asset asset = new Asset();
                    asset.setId(jsonObject.getInt("ID"));
                    asset.setAssetSN(jsonObject.getString("AssetSN"));
                    asset.setAssetGroupID(jsonObject.getInt("AssetGroupID"));
                    asset.setAssetName(jsonObject.getString("AssetName"));
                    asset.setDescription(jsonObject.getString("Description"));
                    asset.setEmployeeID(jsonObject.getInt("EmployeeID"));
                    asset.setWarrantyDate(jsonObject.getString("WarrantyDate"));
                    asset.setDepartmentLocationID(jsonObject.getInt("DepartmentLocationID"));

                    dataAsset.add(asset);
                }
                dataAssetToAdapter.addAll(dataAsset);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("TAG", "getListAssets: " + e.getMessage());
            }
        });
    }

    private void getListDepartmentLocation() {
        String url = getLink("GetDepartmentLocations");
        useVolley(url, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    DepartmentLocation departmentLocation = new DepartmentLocation();
                    departmentLocation.setId(jsonObject.getInt("ID"));
                    departmentLocation.setDepartmentID(jsonObject.getInt("DepartmentID"));
                    departmentLocation.setLocationID(jsonObject.getInt("LocationID"));
                    departmentLocation.setStartDate(jsonObject.getString("StartDate"));
                    departmentLocation.setEndDate(jsonObject.getString("EndDate"));

                    dataDepartmentLocation.add(departmentLocation);
                }
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartmentLocation: " + e.getMessage());
            }
        });
    }

    private void getListDepartment() {
        String url = getLink("GetDepartments");
        useVolley(url, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    Department department = new Department();
                    department.setId(jsonObject.getInt("ID"));
                    department.setName(jsonObject.getString("Name"));

                    dataDepartment.add(department);
                }
                mAdapter.notifyDataSetChanged();
                adapterDepartment.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        });
    }

    private void getListAssetGroup() {
        String url = getLink("GetAssetGroups");
        useVolley(url, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    AssetGroup assetGroup = new AssetGroup();
                    assetGroup.setId(jsonObject.getInt("ID"));
                    assetGroup.setName(jsonObject.getString("Name"));

                    dataAssetGroup.add(assetGroup);
                }
                adapterAssetGroup.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        });
    }

    private void getListEmployee() {
        String url = getLink("GetEmployees");
        useVolley(url, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    Employee employee = new Employee();
                    employee.setId(jsonObject.getInt("ID"));
                    employee.setFirstName(jsonObject.getString("FirstName"));
                    employee.setLastName(jsonObject.getString("LastName"));
                    employee.setPhone(jsonObject.getString("Phone"));

                    dataEmployee.add(employee);
                }
                adapterAccountableParty.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        });
    }

    private String getLink(String link) {
        String IP = "10.0.0.29";
        return "http://" + IP + ":3000/" + link;
    }
}