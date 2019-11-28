package com.example.proyectofinalappmoviles.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SellerFragment extends Fragment {

    FirebaseDatabase db;
    FirebaseStorage storage;
    private ImageView image;
    private TextView headerName;
    private TextView headerUser;
    private TextView name;
    private TextView user;
    private TextView study;
    private TextView semester;
    private TextView phone;
    private TextView email;
    private String sellerId;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_infocuenta, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        image = view.findViewById(R.id.seller_usuario_img);
        headerName = view.findViewById(R.id.seller_name_header);
        headerUser = view.findViewById(R.id.seller_user_header);
        name = view.findViewById(R.id.seller_name);
        user = view.findViewById(R.id.seller_username);
        study = view.findViewById(R.id.seller_study);
        semester = view.findViewById(R.id.seller_semester);
        phone = view.findViewById(R.id.seller_phone);
        email = view.findViewById(R.id.seller_email);


    }

    @Override
    public void onResume() {
        super.onResume();
        String extra = getArguments().getString("uid");
        db.getReference().child("users").child(extra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User seller = dataSnapshot.getValue(User.class);
                sellerId = seller.getUid();

                storage.getReference().child("userFotos").child(seller.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(image).load(uri.toString()).into(image);
                });

                headerName.setText(seller.getName());
                headerUser.setText(seller.getUsername());
                name.setText(seller.getName());
                user.setText(seller.getUsername());
                //Anadir carrera
                //Anadir semestre
                phone.setText(seller.getPhone());
                email.setText(seller.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
