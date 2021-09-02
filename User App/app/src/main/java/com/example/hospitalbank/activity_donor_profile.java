package com.example.hospitalbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalbank.Classes.RegionLanguage;
import com.example.hospitalbank.Classes.calculateLastDonation;
import com.example.hospitalbank.Model.Donors;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class activity_donor_profile extends AppCompatActivity {

    ImageView mImageProfile, mImageCall, mImageMessage;
    TextView mTxtName, mTxtDonationNo, mtxtBloodType;
    TextInputLayout mEditEmail, mEditAddress, mEditAge, mEditRemainingTime, mEditLastDonation;
    String lastDate;
    String mPhoneNumber, mAge, mName, mBloodType, mMonth, mYear, mDay, mAddress, mEmail, mProfilePicture, mDonationNumber,mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        passData();
        initialVar();
        makeCall();
        sendMessage();
        setImageProfile();
        setDataInFields();
        getCountOfDonations();
    }


    private void passData() {
        mName = getIntent().getStringExtra("name");
        mAddress = getIntent().getStringExtra("address");
        mBloodType = getIntent().getStringExtra("Btype");
        mAge = getIntent().getStringExtra("age");
        mDay = getIntent().getStringExtra("day");
        mMonth = getIntent().getStringExtra("month");
        mYear = getIntent().getStringExtra("year");
        mEmail = getIntent().getStringExtra("email");
        mPhoneNumber = getIntent().getStringExtra("phone");
        mProfilePicture = getIntent().getStringExtra("photo");
        mId = getIntent().getStringExtra("id");
    }

    private void setDataInFields() {
        mtxtBloodType.setText(mBloodType);
        mTxtName.setText(mName);

        mEditEmail.getEditText().setText(mEmail);
        mEditAge.getEditText().setText(mAge);
        mEditAddress.getEditText().setText(mAddress);

        if (mDay.equals("none") || mMonth.equals("none") || mYear.equals("none")) {
            lastDate = "—";
        } else {
            lastDate = mDay + "-" + mMonth + "-" + mYear;
        }
        mEditLastDonation.getEditText().setText(lastDate);

        Long numberOfDaysLastDonation = new calculateLastDonation().getDaysBetweenDates(lastDate);
        if (!numberOfDaysLastDonation.toString().contains("-")) {
            mEditRemainingTime.getEditText().setText(numberOfDaysLastDonation + " days");
        } else {
            mEditRemainingTime.getEditText().setText("0 days");
        }
    }

    private void setImageProfile() {
        //image donors
        if (!mProfilePicture.equals("No picutre now")) {

                    Picasso.get().load(mProfilePicture).into(mImageProfile);

        } else {
            mImageProfile.setImageResource(R.drawable.ic_profile);
        }
    }

    private void sendMessage() {
        mImageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + mPhoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                try {
                    startActivity(intent);
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeCall() {
        Uri uri = Uri.parse("tel:" + mPhoneNumber);
        mImageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                try {
                    startActivity(intent);
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialVar() {
        mEditAddress = findViewById(R.id.edtxt_address);
        mEditAge = findViewById(R.id.edtxt_age);
        mEditEmail = findViewById(R.id.edtxt_email);
        mEditRemainingTime = findViewById(R.id.edtxt_remainingTime);
        mEditLastDonation = findViewById(R.id.edtxt_lastDonation);

        mTxtName = findViewById(R.id.txt_name);
        mtxtBloodType = findViewById(R.id.txtBloodType);
        mTxtDonationNo = findViewById(R.id.txtDonationNo);

        mImageProfile = findViewById(R.id.image_profile);
        mImageCall = findViewById(R.id.image_call);
        mImageMessage = findViewById(R.id.image_email);

        new RegionLanguage(this);
    }

    private void getCountOfDonations() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);

        reference.child("Donations").child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mTxtDonationNo.setText(snapshot.getChildrenCount() + "x");
                }else{
                    mTxtDonationNo.setText(0 + "x");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}