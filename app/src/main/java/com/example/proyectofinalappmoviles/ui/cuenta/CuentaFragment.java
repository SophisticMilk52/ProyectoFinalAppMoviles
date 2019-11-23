package com.example.proyectofinalappmoviles.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.ui.LoginFragment.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;

public class CuentaFragment extends Fragment {

    private CuentaViewModel cuentaViewModel;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cuentaViewModel =ViewModelProviders.of(this).get(CuentaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cuenta, container, false);

        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() == null) {

            LoginFragment fragment = new LoginFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(container.getId(), fragment);
            fragmentTransaction.commit();
        }





        return root;
    }
}