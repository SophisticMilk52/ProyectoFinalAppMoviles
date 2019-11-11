package com.example.proyectofinalappmoviles.ui.preguntas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectofinalappmoviles.R;

public class PreguntasFragment extends Fragment {

    private PreguntasViewModel preguntasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preguntasViewModel =
                ViewModelProviders.of(this).get(PreguntasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_preguntas, container, false);//Se cambia el fragment_categorias por fragment_preguntas
        /*
        final TextView textView = root.findViewById(R.id.text_preguntas);//Se cambia text_gallery por text_preguntas que sera el Id dentro de fragment_categorias
        preguntasViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }


}
