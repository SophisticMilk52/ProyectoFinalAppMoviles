package com.example.proyectofinalappmoviles.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.User;
import com.example.proyectofinalappmoviles.ui.LoginFragment.LoginFragment;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class SignInFragment extends Fragment {

    private EditText signin_email;
    private EditText signin_name;
    private EditText signin_username;
    private EditText signin_mobile;
    private EditText signin_password;
    private EditText signin_repassword;
    private Button login_signin;
    private TextView alreadyUserTV;
    private ImageView userImage;

    private static final int CAMERA_CALLBACK_ID = 11;


    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        signin_email = view.findViewById(R.id.signin_email);
        signin_name = view.findViewById(R.id.signin_name);
        signin_username = view.findViewById(R.id.signin_username);
        signin_mobile = view.findViewById(R.id.signin_mobile);
        signin_password = view.findViewById(R.id.signin_password);
        signin_repassword = view.findViewById(R.id.signin_repassword);
        login_signin = view.findViewById(R.id.login_signin);
        alreadyUserTV = view.findViewById(R.id.alreadyUserTV);
        userImage=view.findViewById(R.id.signin_image);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CALLBACK_ID);
            }
        });


        login_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signin_email.getText().toString().trim().isEmpty()) {
                    Toast.makeText(v.getContext(), "El campo de email esta vacio", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!signin_password.getText().toString()
                        .equals(signin_repassword.getText().toString())) {
                    Toast.makeText(v.getContext(), "Las contraseñas NO coinciden", Toast.LENGTH_LONG).show();
                    return;
                }

                if (signin_password.getText().toString().trim().length() < 10) {
                    Toast.makeText(v.getContext(), "Las contraseñas debe tener mínimo 10 carácteres", Toast.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(signin_email.getText().toString().trim(), signin_password.getText().toString().trim())

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "" + signin_email.getText().toString().trim() + " : " + signin_password.getText().toString().trim(), Toast.LENGTH_LONG).show();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            Drawable drawable= userImage.getDrawable();
                            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(bitmap);
                            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                            drawable.draw(canvas);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                            storage.getReference().child("userFotos")
                                    .child(auth.getCurrentUser().getUid()).putBytes(stream.toByteArray());

                            User user=new User(
                                    auth.getCurrentUser().getUid(),
                                    signin_name.getText().toString(),
                                    signin_email.getText().toString(),
                                    signin_username.getText().toString(),
                                    signin_mobile.getText().toString(),
                                    signin_password.getText().toString());

                            db.getReference().child("users").child(auth.getCurrentUser().getUid()).setValue(user);

                            HomeFragment fragment = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                            fragmentTransaction.commit();
                        }
                        else{
                            Toast.makeText(getContext(), "Esta cuenta ya existe", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }




        });

        alreadyUserTV.setOnClickListener(
                (v) -> {
                    LoginFragment fragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.commit();
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if( requestCode == CAMERA_CALLBACK_ID ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userImage.setImageBitmap(photo);
        }
    }
}
