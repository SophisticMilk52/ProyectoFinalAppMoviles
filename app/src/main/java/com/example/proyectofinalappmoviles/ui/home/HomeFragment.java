package com.example.proyectofinalappmoviles.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.adapter.PublicationAdapter;
import com.example.proyectofinalappmoviles.model.Publication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView publications;
    FirebaseDatabase db;
    PublicationAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db=FirebaseDatabase.getInstance();
        publications=getView().findViewById(R.id.publications_lv);
        adapter=new PublicationAdapter();
        publications.setAdapter(adapter);
        ArrayList<Publication> pubs=new ArrayList<>();
        db.getReference().child("publications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()
                ) {
                    pubs.add(data.getValue(Publication.class));
                    Toast.makeText(getContext(), ""+(pubs.get(pubs.size()-1)).getTitle(), Toast.LENGTH_SHORT).show();
                }
                adapter.setPubs(pubs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        publications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), ""+pubs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

}
}