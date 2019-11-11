package com.example.proyectofinalappmoviles.ui.calificanos;

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

public class CalificanosFragment extends Fragment {

    private CalificanosViewModel calificanosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calificanosViewModel =
                ViewModelProviders.of(this).get(CalificanosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calificanos, container, false);

        /*
        final TextView textView = root.findViewById(R.id.text_send);
        calificanosViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


         */
        return root;
    }
}