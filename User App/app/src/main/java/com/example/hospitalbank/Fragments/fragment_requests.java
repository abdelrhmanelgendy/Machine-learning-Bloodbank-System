package com.example.hospitalbank.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hospitalbank.Adapters.RequestAdapter;
import com.example.hospitalbank.Classes.Get_Date_Time;
import com.example.hospitalbank.Classes.InternetConnection;
import com.example.hospitalbank.Classes.RandomData;
import com.example.hospitalbank.Classes.RegionLanguage;
import com.example.hospitalbank.CodeImage;
import com.example.hospitalbank.MainActivity;
import com.example.hospitalbank.Model.Donors;
import com.example.hospitalbank.Model.RequestModel;
import com.example.hospitalbank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_requests extends Fragment {


    CardView mBtnA, mBtnB, mBtnAB, mBtnO, mBtnPlus, mBtnMinus;
    ImageButton mBtnIncrease, mBtnDecrease;

    EditText mEditCounter;

    TextView mTxtA, mTxtB, mTxtAB, mTxtO, mTxtPlus, mTxtMinus, txtBloodTypeError, txtBloodNoError;
    String selectedBlood = "";
    String mark = "";
    private static int counter = 0;
    SwipeRefreshLayout refreshLayout;
    //==============================================//
    Handler mHandler = new Handler();
    Runnable runnable;
    int DELAY = 500;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    private String mMessageDate;
    private String mMessageTime;
    private String mMessageId;

    private RecyclerView mrecyclerView;
    TextView mtxtConnection;

    ImageView mSend, btndelete;

    //    Spinner mspinnerBloodType;
    Button btnManualRecieve;
    Button btnCodeRecieve;
    EditText MessageBody;
//    private TextInputLayout bloodbags;

    RequestAdapter adapter;
    ArrayList<RequestModel> mMessagesArray;

    LinearLayout mlinearLayout;
    ConstraintLayout requestContraint;

    private ConnectivityManager connectivityManager;
    InternetConnection connection = new InternetConnection();
    private String RANDOM_MESSAGE_BODY = "Please go to the nearest blood bank near you to donate ";
    private String CURRENT_VALUE;

    public String getCURRENT_VALUE() {
        return CURRENT_VALUE;
    }

    public void setCURRENT_VALUE(String CURRENT_VALUE) {
        this.CURRENT_VALUE = CURRENT_VALUE;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }

        initializing(view);

        mMessagesArray = new ArrayList<>();
        //CURRENT_VALUE="0";

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = connection.isconnected(connectivityManager);
        if (!isConnected) {
            mtxtConnection.setVisibility(View.VISIBLE);
        } else {
            mtxtConnection.setVisibility(View.GONE);
        }

        getRequests();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bloodBags = mEditCounter.getText().toString();
                boolean isConnected = connection.isconnected(connectivityManager);

                String received_amount = "0";
                if (!isConnected) {
                    mtxtConnection.setVisibility(View.VISIBLE);
                } else {
                    mtxtConnection.setVisibility(View.GONE);
                    if (MessageBody.getText().toString().length() <= 5) {
                        MessageBody.setError("Subject of request is very short!");
                        return;
                    } else {
                        MessageBody.setError(null);
                    }
                    if (mEditCounter.getText().toString().equals("0")) {
                        txtBloodNoError.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        txtBloodNoError.setVisibility(View.GONE);
                    }
                    if (selectedBlood.isEmpty() || mark.isEmpty()) {
                        txtBloodTypeError.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        txtBloodTypeError.setVisibility(View.GONE);
                    }


                    String message_Body = MessageBody.getText().toString();

                    if (message_Body.length() > 1) {
                        mMessageDate = Get_Date_Time.getdate();
                        mMessageTime = Get_Date_Time.gettime();

                        mMessageId = RandomData.getRandom();
                        mDatabase = FirebaseDatabase.getInstance();
                        mReference = mDatabase.getReference("Request").child("request");

                        RequestModel myMessage = new RequestModel(message_Body, mMessageDate, mMessageTime, mMessageId, "Hospital", selectedBlood + mark, bloodBags, received_amount);
                        mReference.setValue(myMessage);

                        sendRequestToGmail_(message_Body, selectedBlood+mark);
                        MessageBody.setText("");
                        clearChoose();
                        getRequests();
                        btnManualRecieve.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnManualRecieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manualRequest(view);
            }
        });

        btnCodeRecieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QR_Receiving(view);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected) {
                    mtxtConnection.setVisibility(View.VISIBLE);
                } else {
                    mtxtConnection.setVisibility(View.GONE);
                    deleteRequest();
                }
            }
        });

        return view;
    }

    private void sendRequestToGmail_(String message_body, String bloodType) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Send Message to donors?")
                    .setTitle("")
                    .setPositiveButton("Send Emails", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<String> emailArrayList = new ArrayList<>();
                            String subject = "Blood Bank emergency needs";

                            mDatabase = FirebaseDatabase.getInstance();
                            mReference = mDatabase.getReference("Users");
                            mReference.addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Donors donor = dataSnapshot.getValue(Donors.class);

                                            if (donor.getBloodType().equals(bloodType)) {
                                                emailArrayList.add(donor.getEmail());
                                            }


                                        }

                                        emailArrayList.forEach(i -> Log.d("MYTAG1", "onDataChange: " + i));
                                        Intent sendEmail = new Intent(Intent.ACTION_SEND);
                                        String[] emails = new String[emailArrayList.size()];

                                        for (int i = 0; i < emailArrayList.size(); i++) {
                                            String single_mail = emailArrayList.get(i).toString();
                                            emails[i] = single_mail;
                                        }

//
                                        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails);
                                        sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                                        sendEmail.putExtra(Intent.EXTRA_TEXT, message_body + " BLood Type");
                                        sendEmail.setType("message/rfc822");
                                        startActivity(Intent.createChooser(sendEmail, "choose email sender APP"));

                                    }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        } catch (Exception E) {
            return;
        }
    }


    private void getRequests() {
        mReference = mDatabase.getReference("Request").child("request");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isConnected = connection.isconnected(connectivityManager);

                if (!isConnected) {
                    mtxtConnection.setVisibility(View.VISIBLE);
                } else {
                    mtxtConnection.setVisibility(View.GONE);

                    if (snapshot.getValue() != null) {
                        mlinearLayout.setVisibility(View.VISIBLE);
                        mMessagesArray.clear();
                        RequestModel message = snapshot.getValue(RequestModel.class);
                        mMessagesArray.add(message);

                        adapter = new RequestAdapter(mMessagesArray, getActivity());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        mrecyclerView.setLayoutManager(layoutManager);
                        mrecyclerView.setAdapter(adapter);
                        requestContraint.setVisibility(View.GONE);
                    } else {
                        mlinearLayout.setVisibility(View.GONE);
//                        requestContraint.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteRequest() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mReference = mDatabase.getReference("Request").child("request");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete Message ?")
                .setTitle("")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mReference.removeValue();
                        mMessagesArray.clear();
                        adapter.notifyDataSetChanged();
                        //         btnManualRecieve.setVisibility(View.GONE);
                        mlinearLayout.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    void automatic_delete() {
        mReference = mDatabase.getReference("Request").child("request");
        mReference.removeValue();
        mMessagesArray.clear();
        adapter.notifyDataSetChanged();
        btnManualRecieve.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Congratulation you Reached The Target", Toast.LENGTH_SHORT).show();

        mlinearLayout.setVisibility(View.GONE);
    }

    boolean REQUEST_DONE = false;
    int receivedAmount;

    void manualRequest(View view) {
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = connection.isconnected(connectivityManager);

        if (!isConnected) {
            mtxtConnection.setVisibility(View.VISIBLE);

        } else {
            mtxtConnection.setVisibility(View.GONE);
            mReference = mDatabase.getReference("Request").child("request");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        return;
                    }
                    RequestModel myMessage = snapshot.getValue(RequestModel.class);

                    int targetedAmount;
                    try {
                        receivedAmount = Integer.parseInt(myMessage.getBlood_recieved());
                        targetedAmount = Integer.parseInt(myMessage.getBlood_amount());
                    } catch (Exception e) {
                        return;
                    }
                    receivedAmount++;

                    if (receivedAmount == targetedAmount + 1) {
                        automatic_delete();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mReference = mDatabase.getReference("Request").child("request").child("blood_recieved");
            mReference.setValue(receivedAmount + "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mReference = mDatabase.getReference("Request").child("request");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    try {
                        RequestModel m = snapshot.getValue(RequestModel.class);
                        receivedAmount = Integer.parseInt(m.getBlood_recieved());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    String data;

    void QR_Receiving(View view) {
        boolean isConnected = connection.isconnected(connectivityManager);

        if (!isConnected) {
            mtxtConnection.setVisibility(View.VISIBLE);
        } else {
            mtxtConnection.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), CodeImage.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        mHandler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(runnable, DELAY);
                internetCheck();
            }
        }, DELAY);

        super.onResume();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Request").child("request");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    RequestModel model = snapshot.getValue(RequestModel.class);
                    mMessagesArray.clear();
                    mMessagesArray.add(model);
                    adapter = new RequestAdapter(mMessagesArray, getActivity());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    mrecyclerView.setLayoutManager(linearLayoutManager);
                    mrecyclerView.setAdapter(adapter);
                    mlinearLayout.setVisibility(View.VISIBLE);

                } else {
                    mMessagesArray.clear();
                    adapter = new RequestAdapter(mMessagesArray, getActivity());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    mrecyclerView.setLayoutManager(linearLayoutManager);
                    mrecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isconnected = connection.isconnected(connectivityManager);
    }



    void internetCheck() {
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isconnected = connection.isconnected(connectivityManager);

        if (!isconnected) {
            mtxtConnection.setVisibility(View.VISIBLE);
            requestContraint.setVisibility(View.VISIBLE);
        } else {

            mtxtConnection.setVisibility(View.GONE);
            mReference = mDatabase.getReference("Request").child("request");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isconnected = connection.isconnected(connectivityManager);
                    if (!isconnected) {
                        mtxtConnection.setVisibility(View.VISIBLE);
                    } else {

                        mtxtConnection.setVisibility(View.GONE);
                        if (snapshot.getValue() != null) {
                            mlinearLayout.setVisibility(View.VISIBLE);
                            mMessagesArray.clear();
                            RequestModel message = snapshot.getValue(RequestModel.class);
                            mMessagesArray.add(message);

                            adapter = new RequestAdapter(mMessagesArray, getActivity());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            mrecyclerView.setLayoutManager(layoutManager);
                            mrecyclerView.setAdapter(adapter);
                            requestContraint.setVisibility(View.GONE);
                            mtxtConnection.setVisibility(View.GONE);
                        } else {
                            requestContraint.setVisibility(View.GONE);
                            mlinearLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }


    //    <<<<<<<<<<<<<<<<,,,,,,,<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void initializing(View view) {

        mDatabase = FirebaseDatabase.getInstance();

        btndelete = view.findViewById(R.id.imgDELETE);
        btnManualRecieve = view.findViewById(R.id.request_btn_manual);
        btnCodeRecieve = view.findViewById(R.id.request_btn_Qr);
        mtxtConnection = view.findViewById(R.id.txt_activityrequest_connection);
        mrecyclerView = view.findViewById(R.id.Request_recyclerView);
        mSend = view.findViewById(R.id.request_send);
        MessageBody = view.findViewById(R.id.request_message_boody);

        mlinearLayout = view.findViewById(R.id.Request_linearAction);
        requestContraint = view.findViewById(R.id.request_constraint);

        mBtnA = view.findViewById(R.id.mBtnA);
        mBtnB = view.findViewById(R.id.mBtnB);
        mBtnAB = view.findViewById(R.id.mBtnAB);
        mBtnO = view.findViewById(R.id.mBtnO);

        mBtnIncrease = view.findViewById(R.id.mBtnIncrease);
        mBtnDecrease = view.findViewById(R.id.mBtnDecrease);

        mEditCounter = view.findViewById(R.id.mEditCounter);
        mBtnPlus = view.findViewById(R.id.mBtnPlus);
        mBtnMinus = view.findViewById(R.id.mBtnMinus);

        mTxtA = view.findViewById(R.id.txtA);
        mTxtB = view.findViewById(R.id.txtB);
        mTxtAB = view.findViewById(R.id.txtAB);
        mTxtO = view.findViewById(R.id.txtO);

        mTxtMinus = view.findViewById(R.id.txtMinus);
        mTxtPlus = view.findViewById(R.id.txtPlus);

        txtBloodTypeError = view.findViewById(R.id.txtBloodTypeError);
        txtBloodNoError = view.findViewById(R.id.txtBloodNoError);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(255, 0, 0));
        refreshLayout.setColorSchemeColors(Color.WHITE);

        mBtnIncrease.setEnabled(false);
        mBtnDecrease.setEnabled(false);
        mBtnPlus.setEnabled(false);
        mBtnMinus.setEnabled(false);


        new RegionLanguage(getActivity());
        buttonsEvent();
        doRefreshLayout();
    }

    private void doRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getRequests();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.linear_navigation, new fragment_requests())
                        .commit();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void buttonsEvent() {
        //<<<<<<<<<<<<<<<<<blood type>>>>>>>>>>>>>>>>

        mBtnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnA.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtA.setTextColor(Color.WHITE);
                selectedBlood = mTxtA.getText().toString();

                mBtnB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtB.setTextColor(Color.rgb(73, 73, 73));

                mBtnAB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtAB.setTextColor(Color.rgb(73, 73, 73));

                mBtnO.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtO.setTextColor(Color.rgb(73, 73, 73));

                mBtnPlus.setEnabled(true);
                mBtnMinus.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());

            }
        });

        mBtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnB.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtB.setTextColor(Color.WHITE);
                selectedBlood = mTxtB.getText().toString();

                mBtnA.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtA.setTextColor(Color.rgb(73, 73, 73));

                mBtnAB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtAB.setTextColor(Color.rgb(73, 73, 73));

                mBtnO.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtO.setTextColor(Color.rgb(73, 73, 73));

                mBtnPlus.setEnabled(true);
                mBtnMinus.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());
            }
        });

        mBtnAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnAB.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtAB.setTextColor(Color.WHITE);
                selectedBlood = mTxtAB.getText().toString();

                mBtnA.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtA.setTextColor(Color.rgb(73, 73, 73));

                mBtnB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtB.setTextColor(Color.rgb(73, 73, 73));

                mBtnO.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtO.setTextColor(Color.rgb(73, 73, 73));

                mBtnPlus.setEnabled(true);
                mBtnMinus.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());
            }
        });

        mBtnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnO.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtO.setTextColor(Color.WHITE);
                selectedBlood = mTxtO.getText().toString();

                mBtnA.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtA.setTextColor(Color.rgb(73, 73, 73));

                mBtnAB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtAB.setTextColor(Color.rgb(73, 73, 73));

                mBtnB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtB.setTextColor(Color.rgb(73, 73, 73));

                mBtnPlus.setEnabled(true);
                mBtnMinus.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());
            }
        });

        //<<<<<<<<<<<<<<<<<blood type +|->>>>>>>>>>>>>>>>
        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnPlus.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtPlus.setTextColor(Color.WHITE);
                mark = mTxtPlus.getText().toString();

                mBtnMinus.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtMinus.setTextColor(Color.rgb(73, 73, 73));

                mBtnIncrease.setEnabled(true);
                mBtnDecrease.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());

            }
        });
        mBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnMinus.setCardBackgroundColor(Color.rgb(255, 0, 0));//red
                mTxtMinus.setTextColor(Color.WHITE);
                mark = mTxtMinus.getText().toString();

                mBtnPlus.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
                mTxtPlus.setTextColor(Color.rgb(73, 73, 73));

                mBtnIncrease.setEnabled(true);
                mBtnDecrease.setEnabled(true);
                setRequestText(selectedBlood, mark, mEditCounter.getText().toString());

            }
        });

        //<<<<<<<<<<<<<<<<<counter>>>>>>>>>>>>>>>>
        mBtnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = counter + 1;
                if (mEditCounter.getText().toString().isEmpty()) {
                    mEditCounter.setText(String.valueOf(0));
                } else {
                    mEditCounter.setText(String.valueOf(counter));
                    setRequestText(selectedBlood, mark, mEditCounter.getText().toString());
                }
            }
        });
        mBtnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditCounter.getText().toString().isEmpty()) {
                    mEditCounter.setText(String.valueOf(0));
                } else {
                    if (counter < 0) {
                        counter = 0;
                        mEditCounter.setText(String.valueOf(counter));
                    }
                    if (counter > 0) {
                        counter = counter - 1;
                        mEditCounter.setText(String.valueOf(counter));
                        setRequestText(selectedBlood, mark, mEditCounter.getText().toString());
                    }
                }
            }
        });
    }

    private void setRequestText(String selectedBlood, String mark, String count) {
        MessageBody.getText().clear();
        MessageBody.setText("Please go to the nearest blood bank near you, we need (" + count + ") bags from (" + selectedBlood + mark + ")");
    }

    private void clearChoose() {
        mBtnA.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtA.setTextColor(Color.rgb(73, 73, 73));

        mBtnB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtB.setTextColor(Color.rgb(73, 73, 73));

        mBtnAB.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtAB.setTextColor(Color.rgb(73, 73, 73));

        mBtnO.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtO.setTextColor(Color.rgb(73, 73, 73));

        mBtnPlus.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtPlus.setTextColor(Color.rgb(73, 73, 73));

        mBtnMinus.setCardBackgroundColor(Color.rgb(221, 220, 220));//gray
        mTxtMinus.setTextColor(Color.rgb(73, 73, 73));

        mEditCounter.setText("0");
        MessageBody.setText("");
        selectedBlood = "";
        mark = "";
        counter = 0;

        mBtnIncrease.setEnabled(false);
        mBtnDecrease.setEnabled(false);
        mBtnMinus.setEnabled(false);
        mBtnPlus.setEnabled(false);

    }

}