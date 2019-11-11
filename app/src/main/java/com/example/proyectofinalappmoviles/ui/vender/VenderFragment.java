package com.example.proyectofinalappmoviles.ui.vender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectofinalappmoviles.R;

public class VenderFragment extends Fragment {

    private VenderViewModel venderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        venderViewModel =
                ViewModelProviders.of(this).get(VenderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vender, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_tools);
        venderViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }
}