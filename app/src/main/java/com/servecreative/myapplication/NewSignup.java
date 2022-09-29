package com.servecreative.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class NewSignup extends AppCompatActivity {
    FirebaseAuth auth;

    Button alreadybtn,createacount;
    EditText namebox,mailbox,passwordbox,phoneno;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
         phoneno=findViewById(R.id.signupphoneno);
        alreadybtn=findViewById(R.id.alreadybtn);
        auth= FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
        mailbox=findViewById(R.id.signupemailbox);
        namebox=findViewById(R.id.signupnamebox);
        passwordbox=findViewById(R.id.signuppasswordbox);
        createacount=findViewById(R.id.createsignupbtn);


        alreadybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewSignup.this,login.class));
            }
        });


        createacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,name,phone;
                password=passwordbox.getText().toString();
                name=namebox.getText().toString();
                email=mailbox.getText().toString();
                phone=phoneno.getText().toString();


                if (isEmailValid(email))
                {
                    User user=new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPhone(phone);
                   // user.setPassword(password);

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

                        if (task.isSuccessful())
                        {

                            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        user.setuId(uid);
                                        Toast.makeText(NewSignup.this, "Account created Verify Your Mail", Toast.LENGTH_SHORT).show();
                                        database.collection("users").document(uid).set(user).addOnSuccessListener(unused -> {
                                            startActivity(new Intent(NewSignup.this,login.class));


                                        });
                                    }
                                    else 
                                    {
                                        Toast.makeText(NewSignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                            // Toast.makeText(signupactivity.this, "sucess", Toast.LENGTH_SHORT).show(); //sucesss
                        }
                        else
                        {
                            Toast.makeText(NewSignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
                else
                {
                    Toast.makeText(NewSignup.this, "Enter correct mail", Toast.LENGTH_SHORT).show();
                }






            }
        });


    }
    public static boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }
}