package com.example.proyectofinalappmoviles.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.adapter.MessageAdapter;
import com.example.proyectofinalappmoviles.model.Message;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MessagesFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase db;
    private ListView messages;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No hay mensajes pues no estas registrado", Toast.LENGTH_SHORT).show();
            HomeFragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.commit();
        } else {

            super.onViewCreated(view, savedInstanceState);
            auth = FirebaseAuth.getInstance();
            db = FirebaseDatabase.getInstance();
            messages = view.findViewById(R.id.messagesLv);
            MessageAdapter adapter = new MessageAdapter();
            messages.setAdapter(adapter);

            String uidUser = auth.getCurrentUser().getUid();

            db.getReference().child("inbox").child(uidUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Message> arrayList = new ArrayList<>();
                    for (DataSnapshot item : dataSnapshot.getChildren()
                    ) {
                        arrayList.add(item.getValue(Message.class));
                    }
                    adapter.setMessages(arrayList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Message message = adapter.getMessages().get(position);

                    SendMesssageFragment fragment = new SendMesssageFragment(auth.getCurrentUser().getUid(), message.getSenderId());
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();

                }
            });

        }




    }
}
