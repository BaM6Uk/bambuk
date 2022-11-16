package com.example.testfbtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpScreen extends AppCompatActivity {

    EditText email;
    EditText name;
    EditText pass;
    Button regbtn;
    Button loginbtn;
    ProgressBar regprogressbar;

    AlertDialog.Builder builder;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        regbtn = findViewById(R.id.regbtn);
        loginbtn = findViewById(R.id.loginbtn);
        regprogressbar = findViewById(R.id.regprogressbar);

        builder = new AlertDialog.Builder(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mDataBase = db.getReference("Users");

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()) {
                    email.setError("Введите почту");
                    builder.setTitle("Регистрация")
                            .setMessage("Введите почту.")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Некорректный Email");
                    builder.setTitle("Регистрация")
                            .setMessage("Некорректный Email.")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (name.getText().toString().isEmpty()) {
                    name.setError("Введите имя пользователя");
                    builder.setTitle("Регистрация")
                            .setMessage("Введите имя пользователя.")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (pass.getText().toString().isEmpty()) {
                    pass.setError("Введите пароль");
                    builder.setTitle("Регистрация")
                            .setMessage("Введите пароль.")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                regprogressbar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser authuser = mAuth.getCurrentUser();

                        String dbemail = email.getText().toString();
                        String dbusername = name.getText().toString();
                        String dbpassword = pass.getText().toString();

                        UserDB newUser = new UserDB(dbemail, dbusername, dbpassword);
                        mDataBase.child(authuser.getUid()).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignUpScreen.this, "Работает", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpScreen.this, MainActivity.class));
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String usedemail = "The email address is already in use by another account.";
                        String bademail = "The email address is badly formatted.";
                        if (e.getMessage().equals(usedemail)) {
                            builder.setTitle("Регистрация")
                                    .setMessage("Такой Email уже зарегистрирован.")
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                        if (e.getMessage().equals(bademail)) {
                            builder.setTitle("Регистрация")
                                    .setMessage("Некорректный Email.")
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                    }
                });
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    builder.setTitle("Авторизация")
                            .setMessage("Введите почту!")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (pass.getText().toString().isEmpty()) {
                    builder.setTitle("Авторизация")
                            .setMessage("Введите пароль!")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                regprogressbar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUpScreen.this, "Работает", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpScreen.this, MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String nopass = "The password is invalid or the user does not have a password.";
                        if (e.getMessage().equals(nopass)) {
                            builder.setTitle("Авторизация")
                                    .setMessage("Пароль неверен или такой пользователя не зарегестрирован.")
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(SignUpScreen.this, "Залогинен!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpScreen.this, MainActivity.class));
        } else {
            Toast.makeText(SignUpScreen.this, "Не залогинен!", Toast.LENGTH_SHORT).show();
        }
    }
}
