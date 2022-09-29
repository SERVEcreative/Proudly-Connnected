package com.servecreative.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.servecreative.myapplication.sendbird.CallService;
import com.servecreative.myapplication.sendbird.utils.AuthenticationUtils;
import com.servecreative.myapplication.sendbird.utils.PrefUtils;

import java.util.Objects;

public class login extends AppCompatActivity {
    FirebaseAuth auth;
    EditText mailbox, passwordbox;
    Button loginbtn, createbtn, btnSendBird;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        mailbox = findViewById(R.id.mailbox);
        passwordbox = findViewById(R.id.passwordbox);
        loginbtn = findViewById(R.id.loginbtn);
        createbtn = findViewById(R.id.createbtn);
        btnSendBird = findViewById(R.id.btnSendBird);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait.....");


        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, NewSignup.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email, password;
                email = mailbox.getText().toString();
                password = passwordbox.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Enter Mail And Password", Toast.LENGTH_SHORT).show();
                    return;
                }




                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            AuthenticationUtils.authenticate(login.this, uid, "", isSuccess -> {
                                if (isSuccess) {
                                    setResult(RESULT_OK, null);
                                    Toast.makeText(login.this, " Authenticated", Toast.LENGTH_SHORT).show();
                                    } else {
                                    Toast.makeText(login.this, "not authenticated", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                startActivity(new Intent(login.this, dashboardscreen.class));
                        }
                        else
                            {
                            Toast.makeText(login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });

    }
}