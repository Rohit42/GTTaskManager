package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

//        mToolBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        mLoginEmail = (TextInputLayout) findViewById(R.id.loginEmail);
        mLoginPassword = (TextInputLayout) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email,password);
                }
            }
        });


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(LoginPage.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    mLoginProgress.dismiss();
                    Toast.makeText(LoginPage.this,"Sorry could not login",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
