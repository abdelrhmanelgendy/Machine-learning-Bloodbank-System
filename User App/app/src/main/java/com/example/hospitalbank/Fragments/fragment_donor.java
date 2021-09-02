package com.example.hospitalbank.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hospitalbank.Adapters.Donors_Adapter;
import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.R;
import com.example.hospitalbank.Classes.RegionLanguage;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class fragment_donor extends Fragment {

    Donors_Adapter mDonorsAdapter;
    RecyclerView mRecyclerView;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseRecyclerOptions<Donors> options;
    SwipeRefreshLayout refreshLayout;
    LinearLayout mLayoutFilter;
    ImageView mImageFilter, imageRecycleEmpNull;
    Spinner mSpinnerFilter;
    //    SearchView mSearchView;
    TextInputLayout mEditSearcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donor, container, false);

        new RegionLanguage(getActivity());
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);

        initialVar(view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        getDonorsData();
        doRefreshLayout();

        mImageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayoutFilter.getVisibility() == View.GONE) {
                    mLayoutFilter.setVisibility(View.VISIBLE);

                } else {
                    mLayoutFilter.setVisibility(View.GONE);
                    mEditSearcher.getEditText().setText(null);
                    mDonorsAdapter.startListening();
                    mSpinnerFilter.setSelection(0);
                }
            }
        });
        return view;
    }

    private void filterDonors() {
        mSpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchIntoRecycle(parent.getSelectedItem().toString(), "bloodType");
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
                searchIntoRecycle(s.toString(), "userName");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchIntoRecycle(String value, String key) {
        Query queries = reference.child("Users").orderByChild(key).startAt(value).endAt(value + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Donors>()
                .setQuery(queries, Donors.class).build();

        mDonorsAdapter = new Donors_Adapter(options, getActivity());
        mRecyclerView.setAdapter(mDonorsAdapter);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mDonorsAdapter.startListening();
                    imageRecycleEmpNull.setVisibility(View.GONE);
                } else {
                    imageRecycleEmpNull.setVisibility(View.VISIBLE);
                }
                mDonorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initialVar(View view) {
        mRecyclerView = view.findViewById(R.id.recycleDonorsList);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        mLayoutFilter = view.findViewById(R.id.linearFilter);
        mImageFilter = view.findViewById(R.id.imageFilter);
        imageRecycleEmpNull = view.findViewById(R.id.imageRecycleEmpNull);
        mSpinnerFilter = view.findViewById(R.id.spinnerBloodTypeFilter);
        mEditSearcher = view.findViewById(R.id.editSearcher);

        mSpinnerFilter.setPopupBackgroundDrawable(new ColorDrawable(Color.rgb(162, 29, 42)));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 0, 0));
        refreshLayout.setColorSchemeColors(Color.WHITE);

        filterDonors();
    }

    private void doRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDonorsData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDonorsData() {
        Query query = reference.child("Users");
        options = new FirebaseRecyclerOptions.Builder<Donors>().setQuery(query, Donors.class).build();
        mDonorsAdapter = new Donors_Adapter(options,getActivity());
        mRecyclerView.setAdapter(mDonorsAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mDonorsAdapter.startListening();
                    imageRecycleEmpNull.setVisibility(View.GONE);
                } else {
                    imageRecycleEmpNull.setVisibility(View.VISIBLE);
                }
                mDonorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mDonorsAdapter.startListening();
    }
}