package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hospitalbank.Classes.RandomData;
import com.example.hospitalbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CodeImage extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    TextView txtCode;
    ImageView imgCode, imgVisible;
    String Current_Value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_image);

        initialize();
        RandomData.settRequestID();
        viewQRCOde();
    }

    private void viewQRCOde() {
        try {
            mReference = mDatabase.getReference("Request").child("request").child("message_id");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        return;
                    }
                    String data = snapshot.getValue().toString();

                    QRGEncoder encoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 1000);
                    Bitmap bitmap = encoder.getBitmap();
                    imgCode.setImageBitmap(bitmap);
                    txtCode.setText(data);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception E) {
            return;
        }
    }

    private void initialize() {
        mDatabase = FirebaseDatabase.getInstance();
        txtCode = findViewById(R.id.txtCode);
        imgCode = findViewById(R.id.img_code);
        imgVisible = findViewById(R.id.img_visible);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static String last;

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Request").child("request").child("blood_recieved");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    last = snapshot.getValue().toString();
                } catch (Exception e) {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}