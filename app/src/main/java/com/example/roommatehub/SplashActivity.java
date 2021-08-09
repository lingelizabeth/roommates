package com.example.roommatehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

    Button btnGoToLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterActivity();
            }
        });

        // If already logged in, show the main activity
        // Allows sign-in to persist across app restart
        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }
    }

    // Intent to Login Activity
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // Intent to Register Activity
    private void goRegisterActivity() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    // Intent to Main Activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // Close application on back press
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        finish();
    }

}