package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.Model.MyDonations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class activity_settings extends AppCompatActivity {

    Button logoutBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Donors mUser = dataSnapshot.getValue(Donors.class);

                            String day = mUser.getLastDonationDay().trim();
                            String month = mUser.getLastDonationMonth().trim();
                            String year = mUser.getLastDonationYear().trim();

                            if (day.length() == 1) {
                                day = "0" + day;
                            }
                            if (month.length() == 1) {
                                month = "0" + month;
                            }

                            LocalDate date = LocalDate.now();

                            LocalDate date1 = LocalDate.parse(year + "-" + month + "-" + day);


                            Long months = ChronoUnit.MONTHS.between(date1, date);
                            Log.d("MYTAG", "onDataChange: " + months);

                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Donations").child(mUser.getUserID());
                            ArrayList<MyDonations> myarraylisy = new ArrayList<>();
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                        MyDonations myDonations = snapshot1.getValue(MyDonations.class);
                                        myarraylisy.add(myDonations);


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            String lastDonationMonth = mUser.getLastDonationMonth();
                            String numberOfDonation = myarraylisy.size() + "";
                            int totalVoulumeDonated = myarraylisy.size() * 450;
                            long monthsSinceLastDonation = months;

                            Log.d("MYTAG", "onDataChange: " + lastDonationMonth + " num " + numberOfDonation + " vol " + totalVoulumeDonated + " months " + monthsSinceLastDonation);


                            String url = "http://192.168.1.19:5000/predict";

                            RequestBody formBody = new FormBody.Builder()
                                    .add("v1", lastDonationMonth)
                                    .add("v2", numberOfDonation)
                                    .add("v3", String.valueOf(totalVoulumeDonated))
                                    .add("v4", String.valueOf(monthsSinceLastDonation))
                                    .build();
                            Request request = new Request.Builder().url(url).post(formBody).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.d("TAGM", "onResponse: "+e.getMessage());
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    Log.d("MYTAG", "onResponse: "+response.body().string());
                                    DatabaseReference updateRef =FirebaseDatabase.getInstance().getReference("Users")
                                            .child(mUser.getUserID()).child("prediction_msg");



                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

//        initializing();
    }

    private void init() {
        logoutBtn = findViewById(R.id.btnLogout);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
    }

//    private void initializing() {
//        auth = FirebaseAuth.getInstance();
//        reference = FirebaseDatabase.getInstance().getReference();
//        reference.keepSynced(true);
//
//        if (auth.getCurrentUser() == null) {
//            //login page
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
//
//        logoutBtn = findViewById(R.id.btnLogout);
//
//        txt_forget_password = findViewById(R.id.txt_forget_password);
//
//        emailText = findViewById(R.id.adminEmail);
//        passText = findViewById(R.id.adminPassword);
//        editNewPassword = findViewById(R.id.newPassword);
//
////        imgBtnEmail = findViewById(R.id.editEmail);
//        imgBtnPassword = findViewById(R.id.editPassword);
//        imgBtnNewPassword = findViewById(R.id.editNewPassword);
//
//        linearNewPassword = findViewById(R.id.linearNewPassword);
//
//        emailText.getEditText().setText(auth.getCurrentUser().getEmail());
//
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.setMessage("Please wait, this process may take a serval minutes");
//
//        logout();
//        makeUpdate();
//        forgetPassword();
//    }
//
//
//    private void makeUpdate() {
//        imgBtnPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (linearNewPassword.getVisibility() == View.GONE) {
//                    linearNewPassword.setVisibility(View.VISIBLE);
//                    passText.setEnabled(true);
//                    editNewPassword.setEnabled(true);
//                    imgBtnNewPassword.setImageResource(R.drawable.ic_ok);
//                    imgBtnPassword.setImageResource(R.drawable.ic_arrow_drop_up);
//
//                    imgBtnNewPassword.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getPassword();
//                        }
//                    });
//
//                } else {
//                    linearNewPassword.setVisibility(View.GONE);
//                    passText.setError(null);
//                    passText.getEditText().setText(null);
//                    passText.setEnabled(false);
//                    editNewPassword.setEnabled(false);
//                    editNewPassword.setError(null);
//                    editNewPassword.getEditText().setText(null);
//                    imgBtnNewPassword.setImageResource(R.drawable.ic_edit);
//                    imgBtnPassword.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
//                }
//            }
//        });
//    }
//
//    private void updatePassword(String pass) {
//        String newPass = editNewPassword.getEditText().getText().toString().trim();
//        String oldPass = passText.getEditText().getText().toString();
//        if (!oldPass.equals(pass)) {
//            Toast.makeText(this, oldPass + "this " + pass, Toast.LENGTH_SHORT).show();
//            passText.requestFocus();
//            passText.setError("entered password not match with old password!");
//        } else {
//            passText.setError(null);
//            if (!validatePassword(pass)) {
//                //
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mProgressDialog.create();
//                    mProgressDialog.show();
//                }
//                AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(), oldPass);
//                auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            auth.getCurrentUser().updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        try {
//                                            reference.child("Hospital").child(auth.getCurrentUser().getUid()).child("Password").setValue(newPass);
//                                            if (task.isComplete()) {
//                                                mProgressDialog.dismiss();
//                                                Toast.makeText(getApplicationContext(), "successfully update password", Toast.LENGTH_LONG).show();
//                                                passText.setError(null);
//                                                passText.getEditText().setText(null);
//                                                passText.setEnabled(false);
//                                                editNewPassword.setEnabled(false);
//                                                editNewPassword.setError(null);
//                                                editNewPassword.getEditText().setText(null);
//                                                imgBtnNewPassword.setImageResource(R.drawable.ic_edit);
//                                                imgBtnPassword.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
//                                            }
//
//                                        } catch (Exception e) {
//                                            Toast.makeText(getApplicationContext(), "Failed update password " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    private void getPassword() {
//        reference.child("Hospital").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String pass;
//                if (snapshot.exists()) {
//                    pass = snapshot.child("Password").getValue(String.class);
//                    updatePassword(pass);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    private void logout() {
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity_settings.this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
//                builder1.setMessage(Html.fromHtml("<font color='#FF0000'>Do you want to logout from account ?</font>"));
//                builder1.setPositiveButton(Html.fromHtml("Logout"), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        auth.signOut();
//                        finish();
//                    }
//                });
//                builder1.setNegativeButton(Html.fromHtml("Cancel"), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder1.create();
//                dialog.show();
//            }
//        });
//    }
//
//    private Boolean validatePassword(String pass) {
//        String val = editNewPassword.getEditText().getText().toString().trim();
//        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#_$!%*+=?&])[A-Za-z\\d@#_$=+!%*?&]{8,}$");
//        Matcher matcher = pattern.matcher(val);
//        editNewPassword.requestFocus();
//
//        if (val.isEmpty()) {
//            editNewPassword.setError("Password cannot be empty!");
//            return false;
//        } else if (!matcher.matches()) {
//            editNewPassword.setError("Password is too weak!");
//            return false;
//        } else if (val.equals(pass)) {
//            editNewPassword.setError("Password not updated!");
//            return false;
//        } else {
//            editNewPassword.setError(null);
//            return true;
//        }
//    }
//
//    private void forgetPassword(){
//        txt_forget_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //code....... otp
//                Toast.makeText(getApplicationContext(), "not available now", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

}