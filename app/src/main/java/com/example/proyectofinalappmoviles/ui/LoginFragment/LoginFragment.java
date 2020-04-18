package com.example.proyectofinalappmoviles.ui.LoginFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectofinalappmoviles.MainActivity;
import com.example.proyectofinalappmoviles.R;
import com.example.proyectofinalappmoviles.model.User;
import com.example.proyectofinalappmoviles.services.NotificationService;
import com.example.proyectofinalappmoviles.ui.SignInFragment;
import com.example.proyectofinalappmoviles.ui.home.HomeFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class LoginFragment extends Fragment {

    private Button loginBtn;
    private Button signInBtn;
    private EditText emailText;
    private EditText passworText;
    private LoginButton loginButton;
    FirebaseDatabase db;

    FirebaseAuth auth;
    CallbackManager callbackManager;
    FirebaseStorage storage;
    private ImageView imagenLogo;




    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();




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
        imagenLogo = view.findViewById(R.id.imageViewLogo);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {


                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("permissions", loginResult.getAccessToken().getPermissions().toString());
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("facebook", "signInWithCredential:success");
                                    FirebaseUser user = auth.getCurrentUser();

                                    String username = user.getDisplayName();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String semester = "Usuario de Facebook";
                                    String study = "Usuario de Facebook";
                                    String phone = user.getPhoneNumber();
                                    String password = "null";
                                    String name = username;
                                    String url = user.getPhotoUrl().toString();

                                    User userToDB = new User(uid, name, email, username, phone, password, study, semester);
                                    db.getReference().child("users").child(uid).setValue(userToDB);


                                    Log.e("urlImagenFacebook", url);
                                    Drawable drawable = imagenLogo.getDrawable();
                                    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    Canvas canvas = new Canvas(bitmap);
                                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                    drawable.draw(canvas);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    storage.getReference().child("userFotos").child(uid).putBytes(stream.toByteArray());


                                    HomeFragment fragment = new HomeFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                                    fragmentTransaction.commit();



                                } else {
                                    Log.w("facebook", "signInWithCredential:failure", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getContext(), "FacebookCancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        Toast.makeText(getContext(), "FacebookException", Toast.LENGTH_SHORT).show();
                        Log.e("facebook", exception.getMessage());
                    }
                });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=emailText.getText().toString();
                String password = passworText.getText().toString();

                auth.signOut();
                auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ((MainActivity) getActivity()).setServiceIntent(new Intent(getActivity(), NotificationService.class));
                            getActivity().startService(((MainActivity) getActivity()).getServiceIntent());

                            HomeFragment fragment = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
