package com.example.hospitalbank.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hospitalbank.MainActivity;
import com.example.hospitalbank.PredictionActivity;
import com.example.hospitalbank.R;
import com.example.hospitalbank.Classes.RegionLanguage;
import com.example.hospitalbank.activity_history;
import com.example.hospitalbank.activity_settings;
import com.example.hospitalbank.activity_statistics;
import com.example.hospitalbank.activity_warehouse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment_home extends Fragment {

    CardView mCardStatistics, mCardHistory, mCardWarehouse, mCardSettings;
    TextView mTxtDonner, mTxtUsers;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    int one, zero;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializing(view);

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        getData();

        mCardStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), activity_statistics.class));
            }
        });

        mCardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PredictionActivity.class));
            }
        });

        mCardWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), activity_warehouse.class));
            }
        });

        mCardHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), activity_history.class));
            }
        });
        return view;
    }

    private void getData() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").exists()) {
                    mTxtUsers.setText(String.valueOf(snapshot.child("Users").getChildrenCount()));

//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        switch (dataSnapshot.child("isDonor").getValue(String.class)) {
//                            case "0":
//                                ++zero; //This is the "user not donor count" int we made earlier; ++ increments 1.
//                                break;
//                            case "1":
//                                ++one; //This is the "donor count" int we made earlier; ++ increments 1.
//                                break;
//                        }
//                    }
                }
                if (snapshot.child("Donations").exists()) {
                    mTxtDonner.setText(String.valueOf(snapshot.child("Donations").getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializing(View view) {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.keepSynced(true);

        mCardStatistics = view.findViewById(R.id.card_statistics);
        mCardHistory = view.findViewById(R.id.card_history);
        mCardSettings = view.findViewById(R.id.card_settings);
        mCardWarehouse = view.findViewById(R.id.card_warehouse);

        mTxtDonner = view.findViewById(R.id.txtDonner);
        mTxtUsers = view.findViewById(R.id.txtRequest);


        new RegionLanguage(getActivity());

    }


}