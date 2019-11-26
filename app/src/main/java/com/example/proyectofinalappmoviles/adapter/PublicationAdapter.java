package com.example.proyectofinalappmoviles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.Publication;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PublicationAdapter extends BaseAdapter {

    ArrayList<Publication> pubs;

    public PublicationAdapter(){pubs=new ArrayList<>();}

    @Override
    public int getCount() {
        return pubs.size();
    }

    @Override
    public Object getItem(int i) {
        return pubs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AppCompatActivity ref = (AppCompatActivity) viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View rowView = inflater.inflate(R.layout.item_articulo, null);
        ImageView rowImage = rowView.findViewById(R.id.articulo_img_et);
        TextView rowName = rowView.findViewById(R.id.titulo_articulo_et);
        TextView rowPrice = rowView.findViewById(R.id.precio_articulo_et);

        rowName.setText(pubs.get(i).getTitle());
        rowPrice.setText(pubs.get(i).getPrice()+"");
        loadImageFromInternet(i,rowImage);




        return rowView;

    }

    private void loadImageFromInternet(int i,ImageView rowImage) {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        storage.getReference().child("publicationPhotos")
                .child(pubs.get(i).getUid())
                .getDownloadUrl().addOnSuccessListener(uri->{
            String url=uri.toString();
            Glide.with(rowImage).load(url).into(rowImage);
        });

    }

    public void setPubs(ArrayList<Publication> pubs){
        this.pubs=pubs;
        notifyDataSetChanged();
    }


}
