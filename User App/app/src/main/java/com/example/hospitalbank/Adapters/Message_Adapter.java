package com.example.hospitalbank.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalbank.Model.Message;
import com.example.hospitalbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Message_Adapter extends FirebaseRecyclerAdapter<Message, Message_Adapter.MViewHolder> {
    Context context;
    DatabaseReference mReference;

    public Message_Adapter(@NonNull FirebaseRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_dsign, parent, false);
        return new MViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position, @NonNull Message message) {
        holder.txt_message_body.setText(message.getBoody());
        holder.txt_message_date.setText(message.getMessage_date());
        holder.txt_message_time.setText(message.getMessage_time());

        //delete message
        holder.messageLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReference = FirebaseDatabase.getInstance().getReference("Chats").child(message.getMessage_id());
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Delete Message ?")
                        .setTitle("")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mReference.removeValue();
                                Toast.makeText(view.getContext(), "Done", Toast.LENGTH_SHORT).show();
                            }

                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
                return false;
            }
        });
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {

        TextView txt_message_body;
        TextView txt_message_time;
        TextView txt_message_date;
        LinearLayout messageLinearLayout;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_message_body = itemView.findViewById(R.id.message_body);
            txt_message_time = itemView.findViewById(R.id.message_txt_time);
            txt_message_date = itemView.findViewById(R.id.message_txt_date);
            messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
        }
    }
}
