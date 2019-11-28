package com.example.proyectofinalappmoviles.ui.cuenta;

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
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.User;
import com.example.proyectofinalappmoviles.ui.LoginFragment.LoginFragment;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class CuentaFragment extends Fragment {

    private CuentaViewModel cuentaViewModel;
    FirebaseAuth auth;
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
    private Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cuentaViewModel =ViewModelProviders.of(this).get(CuentaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cuenta, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {

            LoginFragment fragment = new LoginFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.commit();
        } else {

            image = view.findViewById(R.id.account_usuario_img);
            headerName = view.findViewById(R.id.account_name_header);
            headerUser = view.findViewById(R.id.account_user_header);
            name = view.findViewById(R.id.account_name);
            user = view.findViewById(R.id.account_username);
            study = view.findViewById(R.id.account_study);
            semester = view.findViewById(R.id.account_semester);
            phone = view.findViewById(R.id.account_phone);
            email = view.findViewById(R.id.account_email);
            logout = view.findViewById(R.id.logout_btn);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();

                    HomeFragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                }
            });

            db.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User account = dataSnapshot.getValue(User.class);

                    storage.getReference().child("userFotos").child(account.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(image).load(uri.toString()).into(image);
                    });

                    headerName.setText(account.getName());
                    headerUser.setText(account.getUsername());
                    name.setText(account.getName());
                    user.setText(account.getUsername());
                    study.setText(account.getStudy());
                    semester.setText(account.getSemester());
                    phone.setText(account.getPhone());
                    email.setText(account.getEmail());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }
}