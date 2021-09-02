package com.example.hospitalbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalbank.Classes.calculateLastDonation;
import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.R;
import com.example.hospitalbank.activity_donor_profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Donors_Adapter extends FirebaseRecyclerAdapter<Donors,Donors_Adapter.MViewHolder> {
    Context context;
    public Donors_Adapter(@NonNull FirebaseRecyclerOptions<Donors> options,Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custm_rv_donors, parent, false);
        return new MViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position,@NonNull Donors donors) {
        
        holder.txtUsername.setText(donors.getUserName());
        holder.txtBlood.setText(donors.getBloodType());
        holder.cardDonors.setAnimation(AnimationUtils.loadAnimation(context, R.anim.layout_animation_left_to_right));


        String lastDate = donors.getLastDonationDay() + "-" + donors.getLastDonationMonth() + "-" +
                donors.getLastDonationYear();

        Long numberOfDaysLastDonation = new calculateLastDonation().getDaysBetweenDates(lastDate);

        if (numberOfDaysLastDonation <= 90 && numberOfDaysLastDonation > 0) {
            holder.txtStatus.setCardBackgroundColor(Color.RED);
        } else {
            holder.txtStatus.setCardBackgroundColor(Color.rgb(84, 209, 89));
        }

        //image donors
        if (!donors.getProfilePicture().equals("No picutre now")) {

                    Picasso.get().load(donors.getProfilePicture()).into(holder.donorProImage);

        } else {
            holder.donorProImage.setImageResource(R.drawable.ic_profile);
        }

        transferDataToFragment(holder, position,donors);
    }

    private void transferDataToFragment(MViewHolder holder, int position,Donors donors) {
        holder.cardDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                Intent intent = new Intent(v.getContext(), activity_donor_profile.class);
                intent.putExtra("name", donors.getUserName());
                intent.putExtra("address", donors.getAddress());
                intent.putExtra("Btype", donors.getBloodType());
                intent.putExtra("age", donors.getAge());
                intent.putExtra("day", donors.getLastDonationDay());
                intent.putExtra("month", donors.getLastDonationMonth());
                intent.putExtra("year", donors.getLastDonationYear());
                intent.putExtra("email", donors.getEmail());
                intent.putExtra("phone", donors.getPhoneNumber());
                intent.putExtra("photo", donors.getProfilePicture());
                intent.putExtra("id", donors.getUserID());


                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

//    @Override
//    public int getItemCount() {
//        return donors;
//    }


    public static class MViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername;
        ImageView donorProImage;
        TextView txtBlood;
        CardView txtStatus, cardDonors;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            donorProImage = itemView.findViewById(R.id.donorProImage);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtBlood = itemView.findViewById(R.id.txtBlood);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            cardDonors = itemView.findViewById(R.id.cardDonors);
        }
    }
}
