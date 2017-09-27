package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartupPage extends AppCompatActivity {

    private Button mNeedAccount;
    private Button mExistingAcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        mNeedAccount = (Button) findViewById(R.id.newAccountButton);
        mExistingAcount = (Button) findViewById(R.id.existingAccount);
        mExistingAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(StartupPage.this,LoginPage.class);
                startActivity(login);

            }
        });
        mNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(StartupPage.this,RegisterActivity.class);
                startActivity(registration);
            }
        });
    }
}
