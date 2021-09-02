package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hospitalbank.Classes.RegionLanguage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.temporal.ValueRange;

public class activity_warehouse extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    SwipeRefreshLayout refreshLayout;

    ImageView mImageAMinus, mImageBMinus, mImageABMinus, mImageOMinus;
    ImageView mImageAPlus, mImageBPlus, mImageABPlus, mImageOPlus;

    TextView mTxtAMinus, mTxtBMinus, mTxtABMinus, mTxtOMinus;
    TextView mTxtAPlus, mTxtBPlus, mTxtABPlus, mTxtOPlus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        new RegionLanguage(this);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        initialVar();
        getStockData();
        doRefreshLayout();
    }

    private void getStockData() {
        getBloodStock("A+", mImageAPlus, mTxtAPlus);
        getBloodStock("B+", mImageBPlus, mTxtBPlus);
        getBloodStock("AB+", mImageABPlus, mTxtABPlus);
        getBloodStock("O+", mImageOPlus, mTxtOPlus);

        getBloodStock("A-", mImageAMinus, mTxtAMinus);
        getBloodStock("B-", mImageBMinus, mTxtBMinus);
        getBloodStock("AB-", mImageABMinus, mTxtABMinus);
        getBloodStock("O-", mImageOMinus, mTxtOMinus);
    }

    private void getBloodStock(String bloodType, ImageView imageBloodLevels, TextView textView) {

        reference.child("Stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(bloodType).exists()) {
                    int count = Integer.parseInt(snapshot.child(bloodType).child("Number In Stock").getValue(String.class));
                    textView.setText(bloodType + "\n\n" + count);
                    if (1 <= count && count <= 20) {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_5);
                    } else if (21 <= count && count <= 60) {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_35);
                    } else if (61 <= count && count <= 100) {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_50);
                    } else if (101 <= count && count <= 200) {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_75);
                    } else if (count == 0) {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_empty);
                    } else {
                        imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_99);
                    }
                } else {
                    textView.setText(bloodType + "\n\n0");
                    imageBloodLevels.setImageResource(R.drawable.ic_blood_bag_empty);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialVar() {
        refreshLayout = findViewById(R.id.refreshLayout);

        //blood positive
        mImageAPlus = findViewById(R.id.mImageAPlus);
        mImageBPlus = findViewById(R.id.mImageBPlus);
        mImageABPlus = findViewById(R.id.mImageABPlus);
        mImageOPlus = findViewById(R.id.mImageOPlus);

        mTxtAPlus = findViewById(R.id.txtAPlus);
        mTxtBPlus = findViewById(R.id.txtBPlus);
        mTxtABPlus = findViewById(R.id.txtABPlus);
        mTxtOPlus = findViewById(R.id.txtOPlus);

        //blood negative
        mImageAMinus = findViewById(R.id.mImageAMinus);
        mImageBMinus = findViewById(R.id.mImageBMinus);
        mImageABMinus = findViewById(R.id.mImageABMinus);
        mImageOMinus = findViewById(R.id.mImageOMinus);

        mTxtAMinus = findViewById(R.id.txtAMinus);
        mTxtBMinus = findViewById(R.id.txtBMinus);
        mTxtABMinus = findViewById(R.id.txtABMinus);
        mTxtOMinus = findViewById(R.id.txtOMinus);

        refreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 0, 0));
        refreshLayout.setColorSchemeColors(Color.WHITE);

    }

    private void doRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockData();
                refreshLayout.setRefreshing(false);
            }
        });
    }
}