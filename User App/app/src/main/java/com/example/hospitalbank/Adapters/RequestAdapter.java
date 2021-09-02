package com.example.hospitalbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalbank.Model.RequestModel;
import com.example.hospitalbank.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    ArrayList<RequestModel> mArrayList;
    Context context;

    public RequestAdapter(ArrayList<RequestModel> mArrayList, Context context) {
        this.mArrayList = mArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_row_design, null);
        MViewHolder viewHolder = new MViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {

        holder.txt_message_body.setText(mArrayList.get(position).getBoody());
        holder.txt_message_date.setText(mArrayList.get(position).getMessage_date());
        holder.txt_message_time.setText(mArrayList.get(position).getMessage_time());


        holder.txt_request_amount.setText(mArrayList.get(position).getBlood_amount());
        holder.txt_recieved_amount.setText(mArrayList.get(position).getBlood_recieved());

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class MViewHolder extends RecyclerView.ViewHolder {

        TextView txt_message_body;
        TextView txt_message_time;
        TextView txt_message_date;
        TextView txt_request_amount;
        TextView txt_recieved_amount;

        public MViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            txt_message_body = itemView.findViewById(R.id.request_message_body);
            txt_message_time = itemView.findViewById(R.id.request_message_txt_time);
            txt_message_date = itemView.findViewById(R.id.request_message_txt_date);
            txt_request_amount = itemView.findViewById(R.id.request_requested_amount);
            txt_recieved_amount = itemView.findViewById(R.id.request_recieved_amount);
        }
    }
}
