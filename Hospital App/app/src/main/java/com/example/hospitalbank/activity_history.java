package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hospitalbank.Adapters.Donors_Adapter;
import com.example.hospitalbank.Adapters.HistoryAdapter;
import com.example.hospitalbank.Classes.RegionLanguage;
import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.Model.History;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_history extends AppCompatActivity {
    ArrayList<History> mArrayList;
    HistoryAdapter mAdapter;
    RecyclerView mRecyclerView;
    FirebaseAuth auth;
    DatabaseReference reference;
    SwipeRefreshLayout refreshLayout;
    LinearLayout mLayoutFilter;
    ImageView mImageFilter, imageRecycleEmpNull;
    Spinner mSpinnerFilter;
    TextInputLayout mEditSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        new RegionLanguage(this);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mArrayList = new ArrayList<>();

        initialVar();
        getHistoryData();
        doRefreshLayout();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity_history.this));

        mImageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayoutFilter.getVisibility() == View.GONE) {
                    mLayoutFilter.setVisibility(View.VISIBLE);

                } else {
                    mLayoutFilter.setVisibility(View.GONE);
                    mEditSearcher.getEditText().setText(null);
                    mAdapter.notifyDataSetChanged();
                    mSpinnerFilter.setSelection(0);
                }
            }
        });


    }

    private void getHistoryData() {
        mAdapter = new HistoryAdapter(mArrayList, activity_history.this);
        mRecyclerView.setAdapter(mAdapter);

        reference.child("Donations_History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mArrayList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        History history = dataSnapshot.getValue(History.class);
                        mArrayList.add(history);
                    }
                    imageRecycleEmpNull.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                } else {
                    imageRecycleEmpNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void doRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistoryData();
                mEditSearcher.getEditText().setText(null);
                mSpinnerFilter.setSelection(0);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initialVar() {
        mRecyclerView = findViewById(R.id.recycleHistory);
        refreshLayout = findViewById(R.id.refreshLayout);

        mLayoutFilter = findViewById(R.id.linearFilter);
        mImageFilter = findViewById(R.id.imageFilter);
        imageRecycleEmpNull = findViewById(R.id.imageRecycleEmpNull);
        mSpinnerFilter = findViewById(R.id.spinnerBloodTypeFilter);
        mEditSearcher = findViewById(R.id.editSearcher);

        mSpinnerFilter.setPopupBackgroundDrawable(new ColorDrawable(Color.rgb(162, 29, 42)));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 0, 0));
        refreshLayout.setColorSchemeColors(Color.WHITE);

        filterDonors();

    }

    private void filterDonors() {
        mSpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchIntoRecycle(parent.getSelectedItem().toString(), "BloodType");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mEditSearcher.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchIntoRecycle(s.toString(), "Email");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchIntoRecycle(String value, String key) {

        mAdapter = new HistoryAdapter(mArrayList, activity_history.this);
        mRecyclerView.setAdapter(mAdapter);

        reference.child("Donations_History").orderByChild(key).startAt(value).endAt(value + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mArrayList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        History history = dataSnapshot.getValue(History.class);
                        mArrayList.add(history);
                    }
                    imageRecycleEmpNull.setVisibility(View.GONE);

                    mAdapter.notifyDataSetChanged();
                } else {
                    imageRecycleEmpNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_history.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}