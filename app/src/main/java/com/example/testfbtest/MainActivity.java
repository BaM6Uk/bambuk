package com.example.testfbtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button signout;
    Button launchbtn;
    TextView profile_usernamedb;
    TextView profile_emaildb;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser authuser;
    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signout = findViewById(R.id.signout);
        launchbtn = findViewById(R.id.launchbtn);
        profile_usernamedb = findViewById(R.id.profile_usernamedb);
        profile_emaildb = findViewById(R.id.profile_emaildb);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        authuser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference("Users");

        if (authuser == null){
            Toast.makeText(MainActivity.this, "Не залогинен!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showProfile(authuser);
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignUpScreen.class));
            }
        });

        launchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LaunchScreen.class));
            }
        });
    }

    private void showProfile(FirebaseUser authuser) {
        String userID = authuser.getUid();

        mDataBase = FirebaseDatabase.getInstance().getReference("Users");
        mDataBase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDB readUserDB = snapshot.getValue(UserDB.class);
                if (readUserDB != null) {
                    String profile_usernamet = readUserDB.dbusername;
                    String profile_emailt = readUserDB.dbemail;

                    profile_usernamedb.setText(profile_usernamet);
                    profile_emaildb.setText(profile_emailt);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}