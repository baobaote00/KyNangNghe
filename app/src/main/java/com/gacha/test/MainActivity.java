package com.gacha.test;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.gacha.test.Layout.AssetInformationActivity;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Spinner spDepartment, spAssetGroup, spAccountableParty;
    private DepartmentAdapter adapterDepartment;
    private AssetGroupAdapter adapterAssetGroup;
    private AccountablePartyAdapter adapterAccountableParty;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private final List<Asset> dataAsset = new ArrayList<>();
    private final List<Asset> dataAssetToAdapter = new ArrayList<>();

    private final List<DepartmentLocation> dataDepartmentLocation = new ArrayList<>();
    private final List<Department> dataDepartment = new ArrayList<>();
    private final List<AssetGroup> dataAssetGroup = new ArrayList<>();
    private final List<Employee> dataEmployee = new ArrayList<>();

    boolean firstTimeDepartment = true;
    boolean firstTimeAssetGroup = true;
    boolean firstTimeEmployee = true;
    boolean create = true;

    private EditText startDate, endDate, search;
    private final Calendar calendar = Calendar.getInstance();
    private TextView numberOfRecord;

    private Button buttonAdd;

    private Date startDateValue, endDateValue;
    private final SimpleDateFormat formatWarrantyDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();

        setAdapter();

        callAPI();

        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (create) {
            create = false;
            return;
        }

        dataAssetToAdapter.clear();
        dataAsset.clear();
        getListAssets();
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

                    mAdapter.notifyData();
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
                    mAdapter.notifyData();
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
                    mAdapter.notifyData();
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

                    //kiểm tra ngày kết thúc đã được set chưa
                    if (endDateValue != null && endDateValue.compareTo(calendar.getTime()) < 0)
                        return;

                    startDateValue = setTimeToEditText(startDate);

                    dataAssetToAdapter.clear();

                    if (endDateValue != null) {
                        for (int i = 0; i < dataAsset.size(); i++) {
                            if (dataAsset.get(i).getWarrantyDateFormat().compareTo(startDateValue) > 0
                                    && dataAsset.get(i).getWarrantyDateFormat().compareTo(endDateValue) < 0) {
                                dataAssetToAdapter.add(dataAsset.get(i));
                            }
                        }
                        mAdapter.notifyData();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        endDate.setOnFocusChangeListener((view, b) -> {
            //nếu focus ra ngoài thì không hiện
            if (b) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (v, year, monthOfYear, dayOfMonth) -> {
                    // Lưu ngày được chọn vào biến Calendar
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Hiển thị ngày được chọn


                    //kiểm tra ngày bắt đầu đã được set và ngày bắt đầu nhỏ hơn ngày kết thúc thì hiển thị
                    if (startDateValue != null && startDateValue.compareTo(calendar.getTime()) > 0)
                        return;

                    endDateValue = setTimeToEditText(endDate);

                    dataAssetToAdapter.clear();

                    if (startDateValue != null) {
                        Log.d(TAG, "setEvent: " + dataAsset.size());
                        for (int i = 0; i < dataAsset.size(); i++) {
                            if (!dataAsset.get(i).getWarrantyDate().isEmpty()
                                    && dataAsset.get(i).getWarrantyDateFormat().compareTo(startDateValue) > 0
                                    && dataAsset.get(i).getWarrantyDateFormat().compareTo(endDateValue) < 0) {
                                dataAssetToAdapter.add(dataAsset.get(i));
                            }
                        }
                        mAdapter.notifyData();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        //add event listener
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    dataAssetToAdapter.clear();

                    for (int i = 0; i < dataAsset.size(); i++) {
                        if (dataAsset.get(i).search(s)) {
                            dataAssetToAdapter.add(dataAsset.get(i));
                        }
                    }

                    mAdapter.notifyData();
                } else {
                    dataAssetToAdapter.clear();
                    dataAssetToAdapter.addAll(dataAsset);
                    mAdapter.notifyData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonAdd.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, AssetInformationActivity.class);
            intent.putExtra("dataEmployee", (Serializable) dataEmployee);
            intent.putExtra("dataAssetGroup", (Serializable) dataAssetGroup);
            intent.putExtra("dataDepartment", (Serializable) dataDepartment);
            intent.putExtra("dataAsset", (Serializable) dataAsset);
            startActivity(intent);
        });
    }

    /**
     * hiển thị warranty date
     *
     * @param editText edit text hiển thị ngày được chọn
     * @return ngày được chọn
     */
    private Date setTimeToEditText(EditText editText) {
        editText.setText(formatWarrantyDate.format(calendar.getTime()));
        return calendar.getTime();
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

        mAdapter = new MyAdapter(this, dataAssetToAdapter, dataDepartment, numberOfRecord, dataAsset);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setData(dataDepartmentLocation, dataEmployee, dataAssetGroup);
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
        numberOfRecord = findViewById(R.id.number_of_return_record);

        buttonAdd = findViewById(R.id.button_add);
    }

    private void getListAssets() {
        String url = CallAPI.getLink("GetAssets");
        CallAPI.useVolley(this, url, Request.Method.GET, null,
                (JSONArray response) -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            dataAsset.add(new Asset(jsonObject));
                        }
                        dataAssetToAdapter.addAll(dataAsset);
                        mAdapter.notifyData();
                    } catch (JSONException e) {
                        Log.d("TAG", "getListAssets: " + e.getMessage());
                    }
                },
                error -> Log.d(TAG, "getListAssets:  " + error.getMessage()));
    }

    private void getListDepartmentLocation() {
        String url = CallAPI.getLink("GetDepartmentLocations");
        CallAPI.useVolley(this, url, Request.Method.GET, null,
                (JSONArray response) -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            dataDepartmentLocation.add(new DepartmentLocation(jsonObject));
                        }
                        mAdapter.notifyData();
                    } catch (Exception e) {
                        Log.d("TAG", "getListDepartmentLocation: " + e.getMessage());
                    }
                }, error -> Log.d(TAG, "getListAssets:  " + error.getMessage()));
    }

    private void getListDepartment() {
        String url = CallAPI.getLink("GetDepartments");
        CallAPI.useVolley(this, url, Request.Method.GET, null, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    dataDepartment.add(new Department(jsonObject));
                }
                mAdapter.notifyData();
                adapterDepartment.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        }, error -> Log.d(TAG, "getListAssets:  " + error.getMessage()));
    }

    private void getListAssetGroup() {
        String url = CallAPI.getLink("GetAssetGroups");
        CallAPI.useVolley(this, url, Request.Method.GET, null, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    dataAssetGroup.add(new AssetGroup(jsonObject));
                }
                adapterAssetGroup.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        }, error -> Log.d(TAG, "getListAssets:  " + error.getMessage()));
    }

    private void getListEmployee() {
        String url = CallAPI.getLink("GetEmployees");
        CallAPI.useVolley(this, url, Request.Method.GET, null, (JSONArray response) -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);

                    dataEmployee.add(new Employee(jsonObject));
                }
                adapterAccountableParty.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("TAG", "getListDepartment: " + e.getMessage());
            }
        }, error -> Log.d(TAG, "getListAssets:  " + error.getMessage()));
    }


}