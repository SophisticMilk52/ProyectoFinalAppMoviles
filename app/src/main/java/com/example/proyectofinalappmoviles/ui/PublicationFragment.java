package com.example.proyectofinalappmoviles.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.proyectofinalappmoviles.MainActivity;
import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.Publication;
import com.example.proyectofinalappmoviles.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

public class PublicationFragment extends Fragment {


    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    private TextView title;
    private TextView category;
    private TextView price;
    private TextView description;
    private TextView seller;
    private Button contactBtn;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info_publicacion, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.nombre_publicacion_txt);
        category = view.findViewById(R.id.tipo_articulo_txt);
        price = view.findViewById(R.id.precio_articulo_txt);
        description = view.findViewById(R.id.contenido_publicacion_txt);
        seller = view.findViewById(R.id.autor_txt);
        contactBtn = view.findViewById(R.id.contactSellerBtn);
        image = view.findViewById(R.id.publicacion_img);


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        String extra = getArguments().getString("publication");
        Gson gson = new Gson();
        Publication pub = gson.fromJson(extra, Publication.class);

        title.setText(pub.getTitle());
        category.setText(pub.getCategory());
        price.setText(pub.getPrice() + "");
        description.setText(pub.getDescription());

        storage.getReference().child("publicationPhotos").child(pub.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(image).load(uri.toString()).into(image);
        });


        db.getReference().child("users").child(pub.getSellerId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seller.setText(dataSnapshot.getValue(User.class).getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = pub.getSellerId();

                SellerFragment fragment = new SellerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();

            }
        });
    }


}
