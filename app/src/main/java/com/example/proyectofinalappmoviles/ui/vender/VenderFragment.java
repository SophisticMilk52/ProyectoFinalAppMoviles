package com.example.proyectofinalappmoviles.ui.vender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.Category;
import com.example.proyectofinalappmoviles.model.Publication;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class VenderFragment extends Fragment {

    private VenderViewModel venderViewModel;
    private EditText priceText;
    private EditText titleText;
    private EditText descriptionText;
    private Spinner categoryList;
    private Button sellBtn;
    private ImageView sellImage;
    private static final int CAMERA_CALLBACK_ID = 11;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        venderViewModel =
                ViewModelProviders.of(this).get(VenderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vender, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        priceText=view.findViewById(R.id.sellPrice);
        titleText=view.findViewById(R.id.sell_title);
        descriptionText=view.findViewById(R.id.sell_description);
        categoryList=(Spinner)view.findViewById(R.id.sellCategory);
        sellBtn=view.findViewById(R.id.sellBtn);
        sellImage=view.findViewById(R.id.sell_image);

        ArrayList<Category> categorias=new ArrayList<>();

        db.getReference().child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item:dataSnapshot.getChildren()
                     ) {
                    categorias.add(item.getValue(Category.class));

                }
                ArrayAdapter<Category> adapter=new ArrayAdapter<Category>(view.getContext(),android.R.layout.simple_spinner_item,categorias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sellImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CALLBACK_ID);
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (auth.getCurrentUser() != null) {
                    String uid = UUID.randomUUID().toString();
                    Publication pub = new Publication(
                            uid,
                            titleText.getText().toString(),
                            descriptionText.getText().toString(),
                            Integer.parseInt(priceText.getText().toString()),
                            ((Category) categoryList.getSelectedItem()).getName(),
                            auth.getCurrentUser().getUid()
                    );

                    db.getReference().child("publications").child(pub.getUid()).setValue(pub);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Drawable drawable = sellImage.getDrawable();
                    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    storage.getReference().child("publicationPhotos").child(pub.getUid()).putBytes(stream.toByteArray());

                    HomeFragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "No se puede publicar sin acceder a una cuenta", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if( requestCode == CAMERA_CALLBACK_ID ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            sellImage.setImageBitmap(photo);
        }
    }
}