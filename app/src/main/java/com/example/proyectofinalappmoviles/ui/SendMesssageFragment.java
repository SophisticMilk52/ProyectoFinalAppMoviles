package com.example.proyectofinalappmoviles.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.Message;
import com.example.proyectofinalappmoviles.model.User;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;


public class SendMesssageFragment extends Fragment {

    FirebaseDatabase db;
    private String senderId;
    private String recieverId;
    private TextView sender;
    private TextView reciever;
    private EditText message;
    private ImageButton sendMessageBtn;


    public SendMesssageFragment(String sender, String reciever) {
        senderId = sender;
        recieverId = reciever;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_messsage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseDatabase.getInstance();

        sender = view.findViewById(R.id.senderUsername);
        reciever = view.findViewById(R.id.recieverUsername);
        message = view.findViewById(R.id.editTextMessage);
        sendMessageBtn = view.findViewById(R.id.sendMessageBtn);

        Log.e("sender", senderId);
        Log.e("reciever", recieverId);

        db.getReference().child("users").child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                sender.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.getReference().child("users").child(recieverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                reciever.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = UUID.randomUUID().toString();
                Message sent = new Message(uid, senderId, recieverId, message.getText().toString(), false);
                String uid2 = UUID.randomUUID().toString();
                Message recieved = new Message(uid, senderId, recieverId, message.getText().toString(), true);

                db.getReference().child("inbox").child(senderId).child(uid).setValue(sent);
                db.getReference().child("inbox").child(recieverId).child(uid2).setValue(recieved);

                db.getReference().child("notifications").child(senderId).child(uid).setValue(sent);
                db.getReference().child("notifications").child(recieverId).child(uid2).setValue(recieved);

                MessagesFragment fragment = new MessagesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();

            }
        });


    }
}
