package com.example.hospitalbank.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hospitalbank.Adapters.Message_Adapter;
import com.example.hospitalbank.Classes.Get_Date_Time;
import com.example.hospitalbank.Model.Message;
import com.example.hospitalbank.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class fragment_chat extends Fragment {

    DatabaseReference mReference;
    Message_Adapter mAdapter;
    FirebaseAuth mAuth;
    FirebaseRecyclerOptions<Message> options;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLinear;
    EditText mEdtTextMessage;

    SwipeRefreshLayout refreshLayout;

    private String mMessageBody;
    private String mMessageDate;
    private String mMessageTime;
    private String mMessageId;
    private String mMessageSendr;
    ImageView btn_send;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.keepSynced(true);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.recycle_view);
        mEdtTextMessage = view.findViewById(R.id.edt_txt_send_message);
        btn_send = view.findViewById(R.id.btn_send_message);

        getMesages();

        mLinear = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinear);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_message(view);
            }
        });

        refreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 0, 0));
        refreshLayout.setColorSchemeColors(Color.WHITE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMesages();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void Send_message(View view) {
        try {
            mMessageBody = mEdtTextMessage.getText().toString();
            mMessageDate = Get_Date_Time.getdate();
            mMessageTime = Get_Date_Time.gettime();
            mMessageId = String.valueOf(System.currentTimeMillis());
            mMessageSendr = "Blood Bank";

            if (mMessageBody.length() < 1) {
                mEdtTextMessage.setError("write message!");
                return;
            }

            Message message = new Message(mMessageBody, mMessageDate, mMessageTime, mMessageId, mMessageSendr);
            mReference.child("Chats").child(mMessageId).setValue(message);
            mEdtTextMessage.setText(null);
            mEdtTextMessage.requestFocus();
            getMesages();
        } catch (Exception E) {
            return;
        }
    }

    private void getMesages() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Chats");
        options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();

        mAdapter = new Message_Adapter(options, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mAdapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}