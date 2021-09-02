package com.example.hospitalbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalbank.Model.History;
import com.example.hospitalbank.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MViewHolder> {

    ArrayList<History> mArrayList;
    Context context;

    public HistoryAdapter(ArrayList<History> mArrayList, Context context) {
        this.mArrayList = mArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custm_rv_histoy, parent, false);
        return new MViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {

        holder.txt_dateTime.setText(mArrayList.get(position).getDate() + " : " + mArrayList.get(position).getTime());
        holder.txt_email.setText(mArrayList.get(position).getEmail());
        holder.txt_name.setText(mArrayList.get(position).getUsername());
        holder.txt_bloodType.setText(mArrayList.get(position).getBloodType());
        holder.cardHistory.setAnimation(AnimationUtils.loadAnimation(context, R.anim.layout_animation_left_to_right));
        holder.linearLayoutHistory.setAnimation(AnimationUtils.loadAnimation(context, R.anim.layout_animation_left_to_right));

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class MViewHolder extends RecyclerView.ViewHolder {

        TextView txt_dateTime;
        TextView txt_email;
        TextView txt_name;
        TextView txt_bloodType;
        CardView cardHistory;
        LinearLayout linearLayoutHistory;


        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_dateTime = itemView.findViewById(R.id.txt_dateTime);
            txt_email = itemView.findViewById(R.id.txt_email);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_bloodType = itemView.findViewById(R.id.txt_blood);
            cardHistory = itemView.findViewById(R.id.cardHistory);
            linearLayoutHistory = itemView.findViewById(R.id.linearLayoutHistory);
        }
    }
}
