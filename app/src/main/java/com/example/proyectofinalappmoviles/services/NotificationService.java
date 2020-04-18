package com.example.proyectofinalappmoviles.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.proyectofinalappmoviles.model.Message;
import com.example.proyectofinalappmoviles.model.User;
import com.example.proyectofinalappmoviles.util.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {

    FirebaseDatabase db;
    FirebaseAuth auth;

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        db.getReference().child("notifications")
                .child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot message : dataSnapshot.getChildren()) {
                            Message m = message.getValue(Message.class);
                            if (m.isRecieved()) {
                                i++;
                                int finalI = i;
                                db.getReference().child("users").child(m.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String username = dataSnapshot.getValue(User.class).getUsername();
                                        Log.e("username", username);
                                        String data = m.getMessage();
                                        Log.e("messasge", data);
                                        NotificationUtils.createNotification(finalI, "Mensaje de: " + username, data);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            if (auth.getCurrentUser() != null) {
                                db.getReference().child("notifications")
                                        .child(auth.getCurrentUser().getUid()).child(m.getUid()).setValue(null);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return START_STICKY;


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
