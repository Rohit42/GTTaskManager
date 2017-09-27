package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button createBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(this);

        mDisplayName = (TextInputLayout) findViewById(R.id.regDisplayName);
        mEmail = (TextInputLayout) findViewById(R.id.regDisplayEmail);
        mPassword = (TextInputLayout) findViewById(R.id.regDisplayPassword);
        createBtn = (Button) findViewById(R.id.regCreateButton);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    mRegProgress.setTitle("Logging In");
                    mRegProgress.setMessage("Please wait while we check your credentials");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email, password);
                }

                }
            }
        );
    }
    private void register_user(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mRegProgress.dismiss();
                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                }
                else{
                    mRegProgress.dismiss();
                    Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
