package com.example.proyectofinalappmoviles.ui.LoginFragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.ui.SignInFragment;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private Button loginBtn;
    private Button signInBtn;
    private EditText emailText;
    private EditText passworText;

    FirebaseAuth auth;




    public LoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBtn=view.findViewById(R.id.loginBtn);
        signInBtn=view.findViewById(R.id.registro_btn);
        emailText=view.findViewById(R.id.username_et);
        passworText=view.findViewById(R.id.password_et);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=emailText.getText().toString();
                String password=emailText.getText().toString();

                auth.signInWithEmailAndPassword(username,password);

                HomeFragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignInFragment fragment = new SignInFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();

            }
        });

    }
}
