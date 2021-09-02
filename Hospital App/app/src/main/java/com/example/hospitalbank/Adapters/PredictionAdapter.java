package com.example.hospitalbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionHolder> {


    PredictionInterface predictionInterfaceListener;
    ArrayList<Donors> donorsArrayList;
    Context context;

    public PredictionAdapter(ArrayList<Donors> donorsArrayList, Context context, PredictionInterface listener) {
        this.donorsArrayList = donorsArrayList;
        this.context = context;
        this.predictionInterfaceListener = listener;
    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_predict_item, null);

        return new PredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, int position) {

        Donors donor = donorsArrayList.get(position);
        holder.txtName.setText(donor.getUserName() + "");
        holder.txtBloodType.setText(donor.getBloodType() + "");
//        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.slide_in_left));
        if (!donor.getProfilePicture().equals("No picutre now")) {
            Picasso.get().load(donor.getProfilePicture()).into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return donorsArrayList.size();
    }

    class PredictionHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView txtName;
        TextView txtBloodType;
        TextView btnPredict;
        RelativeLayout relativeLayout;

        public PredictionHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.userProImage);
            txtName = itemView.findViewById(R.id.prediction_item_txtName);
            txtBloodType = itemView.findViewById(R.id.prediction_item_txtBloodType);
            btnPredict = itemView.findViewById(R.id.Prediction_btnPredict);
            relativeLayout=itemView.findViewById(R.id.custome_prediction_relativeLayout);
            btnPredict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int adapterPosition = getAdapterPosition();
                        Donors donors = donorsArrayList.get(adapterPosition);
                        predictionInterfaceListener.onClick(donors);
                    }
                }
            });

        }
    }
}
