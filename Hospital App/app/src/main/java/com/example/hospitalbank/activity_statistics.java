package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.hospitalbank.Classes.RegionLanguage;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class activity_statistics extends AppCompatActivity {

    BarChart mBarChart;
    PieChart mPieChartBloodGroup;
    HorizontalBarChart mPieChartStock;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    long one, two, three, four, five, six, seven, eight, nine, ten, elevn, twilv;
    long om, abm, bm, am, op, abp, bp, ap;
    long oms, abms, bms, ams, ops, abps, bps, aps;
    long[] monthArray, bloodArray, stockArray;
    String[] bloodNameArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.keepSynced(true);

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        new RegionLanguage(this);
        mBarChart = findViewById(R.id.chart1);
        mPieChartBloodGroup = findViewById(R.id.chart2);
        mPieChartStock = findViewById(R.id.chart3);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        ProgressDialog mProgressDialog = new ProgressDialog(activity_statistics.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIcon(R.drawable.ic_baseline_visibility_24);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mProgressDialog.show();

        getBarData(colors, mProgressDialog);
        getPiData(colors, mProgressDialog);
        getPiStockData(colors, mProgressDialog);
    }

    //number of donors per month
    private void getBarData(ArrayList<Integer> colors, ProgressDialog mProgressDialog) {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        mReference.child("Donations History").child(String.valueOf(year)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    monthArray = new long[]{0, one, two, three, four, five, six, seven, eight, nine, ten, elevn, twilv};

                    for (int i = 1; i <= 12; i++) {
                        if (snapshot.child(String.valueOf(i)).exists()) {
                            monthArray[i] = snapshot.child(String.valueOf(i)).getChildrenCount();
                        } else {
                            monthArray[i] = 0;
                        }
                    }
                    mProgressDialog.dismiss();
                }
                try {
                    displayBarChart(colors, monthArray[1], monthArray[2], monthArray[3], monthArray[4], monthArray[5], monthArray[6], monthArray[7], monthArray[8], monthArray[9], monthArray[10], monthArray[11], monthArray[12]);
                }
                catch (Exception e)
                {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayBarChart(ArrayList<Integer> colors, long one, long two, long three,
                                 long four, long five, long six, long seven, long eight, long nine, long ten, long elevn,
                                 long twilv) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (one > 0)
            entries.add(new BarEntry(one, 0));
        if (two > 0)
            entries.add(new BarEntry(two, 1));
        if (three > 0)
            entries.add(new BarEntry(three, 2));
        if (four > 0)
            entries.add(new BarEntry(four, 3));
        if (five > 0)
            entries.add(new BarEntry(five, 4));
        if (six > 0)
            entries.add(new BarEntry(six, 5));
        if (seven > 0)
            entries.add(new BarEntry(seven, 6));
        if (eight > 0)
            entries.add(new BarEntry(eight, 7));
        if (nine > 0)
            entries.add(new BarEntry(nine, 8));
        if (ten > 0)
            entries.add(new BarEntry(ten, 9));
        if (elevn > 0)
            entries.add(new BarEntry(elevn, 10));
        if (twilv > 0)
            entries.add(new BarEntry(twilv, 11));

        BarDataSet barDataSet = new BarDataSet(entries, " ");

        ArrayList<String> label = new ArrayList<>();
        label.add("Jan");
        label.add("Feb");
        label.add("Mar");
        label.add("Apr");
        label.add("May");
        label.add("Jun");
        label.add("Jul");
        label.add("Aug");
        label.add("Sep");
        label.add("Okt");
        label.add("Nov");
        label.add("Dec");

        barDataSet.setColors(colors);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
//        barDataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("#")));
//        barDataSet.setHighlightEnabled(true);

        BarData barData = new BarData(label, barDataSet);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setData(barData);
        mBarChart.setDescription(" ");
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.animateY(1000);
//        mBarChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
//        mBarChart.getLegend().setTextSize(14f);
//        mBarChart.getLegend().setYOffset(12);
        mBarChart.setExtraBottomOffset(20);
        mBarChart.invalidate();

        Legend legend = mBarChart.getLegend();
        legend.setFormSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(14f);
        legend.setYOffset(12);
        legend.setXOffset(10f);
        legend.setYEntrySpace(10);
        legend.setFormToTextSpace(20);
    }


    //count of blood taken
    private void getPiData(ArrayList<Integer> colors, ProgressDialog mProgressDialog) {
        mReference.child("Blood_Group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    bloodArray = new long[]{0, om, abm, bm, am, op, abp, bp, ap};
                    bloodNameArray = new String[]{null, "O-", "AB-", "B-", "A-", "O+", "AB+", "B+", "A+"};

                    for (int i = 1; i <= 8; i++) {
                        if (snapshot.child(String.valueOf(bloodNameArray[i])).exists()) {
                            bloodArray[i] = snapshot.child(String.valueOf(bloodNameArray[i])).getChildrenCount();
                        } else {
                            bloodArray[i] = 0;
                        }
                    }
                    mProgressDialog.dismiss();
                }
                displayPiChart(colors, bloodArray[1], bloodArray[2], bloodArray[3], bloodArray[4], bloodArray[5], bloodArray[6], bloodArray[7], bloodArray[8]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayPiChart(ArrayList<Integer> colors, long om, long abm, long bm,
                                long am, long op, long abp, long bp, long ap) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> label = new ArrayList<>();

        if (abp > 0) {
            entries.add(new Entry(abp, 0));
            label.add("AB+");
        }
        if (ap > 0) {
            entries.add(new Entry(ap, 1));
            label.add("A+");
        }
        if (bp > 0) {
            entries.add(new Entry(bp, 2));
            label.add("B+");
        }
        if (op > 0) {
            entries.add(new Entry(op, 3));
            label.add("O+");
        }

        if (abm > 0) {
            entries.add(new Entry(abm, 4));
            label.add("AB-");
        }
        if (am > 0) {
            entries.add(new Entry(am, 5));
            label.add("A-");
        }
        if (bm > 0) {
            entries.add(new Entry(bm, 6));
            label.add("B-");
        }
        if (om > 0) {
            entries.add(new Entry(om, 7));
            label.add("O-");
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");

        pieDataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("#")));

        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setLabel(null);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setSelectionShift(5);

        PieData piData = new PieData(label, pieDataSet);
        mPieChartBloodGroup.setData(piData);
        mPieChartBloodGroup.setDescription(" ");
        mPieChartBloodGroup.setCenterText("Number of\nBlood Bags");
        mPieChartBloodGroup.setCenterTextSize(15f);
        mPieChartBloodGroup.setUsePercentValues(false);
        mPieChartBloodGroup.animateXY(900, 900);
        mPieChartBloodGroup.invalidate();

        Legend legend = mPieChartBloodGroup.getLegend();
        legend.setFormSize(12f);
        legend.setTextSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(8f);
    }


    //stock chart
    private void getPiStockData(ArrayList<Integer> colors, ProgressDialog mProgressDialog) {

        mReference.child("Stock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    stockArray = new long[]{0, oms, abms, bms, ams, ops, abps, bps, aps};
                    bloodNameArray = new String[]{null, "O-", "AB-", "B-", "A-", "O+", "AB+", "B+", "A+"};

                    for (int i = 1; i <= 8; i++) {
                        if (snapshot.child(String.valueOf(bloodNameArray[i])).exists()) {
                            stockArray[i] = Long.parseLong(snapshot.child(String.valueOf(bloodNameArray[i])).child("Number In Stock").getValue(String.class));
                        } else {
                            stockArray[i] = 0;
                        }
                    }
                    mProgressDialog.dismiss();
                }
                displayStockPiChart(colors, stockArray[1], stockArray[2], stockArray[3], stockArray[4], stockArray[5], stockArray[6], stockArray[7], stockArray[8]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayStockPiChart(ArrayList<Integer> colors, long oms, long abms,
                                     long bms, long ams, long ops, long abps, long bps, long aps) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> label = new ArrayList<>();

        entries.add(new BarEntry(abps, 0));
        label.add("AB+");

        entries.add(new BarEntry(aps, 1));
        label.add("A+");

        entries.add(new BarEntry(bps, 2));
        label.add("B+");

        entries.add(new BarEntry(ops, 3));
        label.add("O+");

        entries.add(new BarEntry(abms, 4));
        label.add("AB-");

        entries.add(new BarEntry(ams, 5));
        label.add("A-");

        entries.add(new BarEntry(bms, 6));
        label.add("B-");

        entries.add(new BarEntry(oms, 7));
        label.add("O-");

        BarDataSet barDataSet = new BarDataSet(entries, "");
//        barDataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("#")));

        barDataSet.setColors(colors);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
        barDataSet.setLabel("");

        BarData barData = new BarData(label, barDataSet);
        mPieChartStock.setData(barData);
        mPieChartStock.setDescription(" ");
        mPieChartStock.animateXY(900, 900);
        mPieChartStock.invalidate();
        mPieChartStock.setExtraBottomOffset(20);

        Legend legend = mPieChartStock.getLegend();
        legend.setFormSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setYOffset(8f);
        legend.setYEntrySpace(10);
        legend.setFormToTextSpace(20);
        legend.setXOffset(10f);
        legend.setWordWrapEnabled(true);
        legend.setStackSpace(20f);
    }

}
