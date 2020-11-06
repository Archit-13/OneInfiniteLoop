package com.example.booksies.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booksies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    //UI references
    private EditText mEmail,mPassword;
    private Button btnSignIn,btnRegister;

    public boolean permissionsAccepted;
    private static final int PERMISSIONS_RC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI refs
        mEmail = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.create_user);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        permissionsAccepted = false;
        requestPermissions();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= mEmail.getText().toString();
                String pass = mPassword.getText().toString();
//                email = "sazimi@ualberta.ca";pass="123456";
                if(!email.equals("") && !pass.equals("")){
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        toastMessage("Successfully signed in as User: " + user.getEmail().toString());
                                        Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
                else{
                    toastMessage("You didn't fill in the all the fields!");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });


    }

    private void toastMessage(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void requestPermissions() {
        boolean coarseLocationNeeded = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean fineLocationNeeded = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean cameraNeeded = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
        boolean storageNeeded = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;

        ArrayList<String> permissions = new ArrayList<>();

        if (coarseLocationNeeded){
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (fineLocationNeeded){
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (cameraNeeded){
            permissions.add(Manifest.permission.CAMERA);
        }
        if (storageNeeded){
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissions.size() > 0){
            ActivityCompat.requestPermissions(this,
                    permissions.toArray(new String[permissions.size()]),
                    PERMISSIONS_RC);
        }
        else{
            permissionsAccepted = true;
        }
    }

    private void updateUI(FirebaseUser currentUser) {
 
    }
}