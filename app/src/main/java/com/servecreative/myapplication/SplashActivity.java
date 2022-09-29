package com.servecreative.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.servecreative.myapplication.sendbird.utils.AuthenticationUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AuthenticationUtils.authenticate(this, "vicky", "", isSuccess -> {
            if (isSuccess) {
                setResult(RESULT_OK, null);
                Toast.makeText(this, "authenticated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "not authenticated", Toast.LENGTH_SHORT).show();

            }
        });
    }
}