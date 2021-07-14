package com.example.roommatehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private EditText etUsername, etPassword, etEmail;
    private Button btnRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etDescription);
        etPassword = findViewById(R.id.etMembers);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnSubmit);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the ParseUser
                ParseUser user = new ParseUser();
                // Error handling
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                if(username.equals("") || password.equals("") || email.equals("")){
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }else if(!email.endsWith("@gmail.com")){
                    Toast.makeText(RegisterActivity.this, "Must be a gmail!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Set core properties
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                // Set custom properties ie user.put("phone", "650-253-0000");
                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            Log.i(TAG, "Registering user succeeded!");
                            goMainActivity();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(RegisterActivity.this, "Error signing up: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Intent to Main Activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}