package com.example.hospitalbank;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalbank.Classes.InternetConnection;
import com.example.hospitalbank.Classes.RegionLanguage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mTxtEmail;
    private TextInputLayout mTxtPassword;

    private FirebaseAuth mAuth;
    DatabaseReference mReference;
//    private static final String EMAIL1 = "bloodbank@gmail.com";
//    private static final String PASSWORD1 = "123456";
    private String EMAIL = "";
    private String PASSWORD = "";
    InternetConnection connection = new InternetConnection();
    ConnectivityManager connectivityManager;
    TextView txtconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        if (mAuth.getCurrentUser() != null) {
            goToDashboard2();
        }

        getUserAuth();
        new RegionLanguage(this);

    }

    private void goToDashboard2() {
        Intent intent = new Intent(MainActivity.this, activity_home.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initialize() {
        txtconnection = findViewById(R.id.txt_activitysign_connection);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mTxtEmail = findViewById(R.id.txt_input_sign_in_email);
        mTxtPassword = findViewById(R.id.txt_input_sign_in_password);
    }

    //get data from firbase
    private void getUserAuth() {
        mReference.child("Hospital").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        EMAIL = dataSnapshot.child("Email").getValue(String.class);
                        PASSWORD = dataSnapshot.child("Password").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "failure " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //new sign by firebase not static in program
    public void signIn(View view) {
        EMAIL = mTxtEmail.getEditText().getText().toString().trim();
        PASSWORD= mTxtPassword.getEditText().getText().toString();

        Log.d("TAG15", "signIn: ");
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isconnected = connection.isconnected(connectivityManager);

        if (!isconnected) {
            txtconnection.setVisibility(View.VISIBLE);

        } else {
            Log.d("TAG15", "signIn: Connect");
            txtconnection.setVisibility(View.GONE);



                        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG15", "onComplete: ");
                                    goToDashboard(EMAIL, PASSWORD);
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("TAG15", "onComplete: "+task.getException().getMessage());
                                }
                            }
                        });






        }

    }


    //method not used XXXXXX
    private void goToDashboard(String email, String password) {
//        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        Intent intent = new Intent(MainActivity.this, activity_home.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }

}