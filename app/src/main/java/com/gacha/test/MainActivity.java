package com.gacha.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        Spinner mySpinnerDepartment = findViewById(R.id.my_spinner_1);
        Spinner mySpinnerAssetGroup = findViewById(R.id.my_spinner_2);
        Spinner mySpinnerAccountableParty = findViewById(R.id.my_spinner_3);

        String[] optionsDepartment = {"Department", "Option 2", "Option 3", "Option 4"};
        String[] optionsAssetGroup = {"Asset group", "Option 2", "Option 3", "Option 4"};
        String[] optionsAccountableParty = {"Accountable Party  ", "Option 2", "Option 3", "Option 4"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsDepartment);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterAssetGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsAssetGroup);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterAccountableParty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsAccountableParty);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinnerDepartment.setAdapter(adapter);
        mySpinnerAssetGroup.setAdapter(adapterAssetGroup);
        mySpinnerAccountableParty.setAdapter(adapterAccountableParty);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> dataset = new ArrayList<>();
        dataset.add("Item 1");
        dataset.add("Item 2");
        dataset.add("Item 3");

        EditText edt = findViewById(R.id.test);

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("EditText", "Giá trị mới: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerView.Adapter mAdapter = new MyAdapter(dataset);
        recyclerView.setAdapter(mAdapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}