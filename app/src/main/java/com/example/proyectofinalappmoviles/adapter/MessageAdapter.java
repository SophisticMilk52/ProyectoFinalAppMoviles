package com.example.proyectofinalappmoviles.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.Message;
import com.example.proyectofinalappmoviles.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    FirebaseDatabase db;

    ArrayList<Message> messages = new ArrayList<>();

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        db = FirebaseDatabase.getInstance();

        AppCompatActivity ref = (AppCompatActivity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.item_message, null);
        TextView message = rowView.findViewById(R.id.messageItem);
        TextView sender = rowView.findViewById(R.id.messageSender);
        LinearLayout layout = rowView.findViewById(R.id.messageItemLayout);

        message.setText(messages.get(position).getMessage());
        if (messages.get(position).isRecieved()) {
            layout.setBackgroundColor(Color.GREEN);
        } else {
            layout.setBackgroundColor(Color.BLUE);
        }
        db.getReference().child("users").child(messages.get(position).getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sender.setText(dataSnapshot.getValue(User.class).getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rowView;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }
}
