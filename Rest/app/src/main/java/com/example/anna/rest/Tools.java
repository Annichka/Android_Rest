package com.example.anna.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anna on 07/22/17.
 */
public class Tools extends AppCompatActivity {

    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Intent nt = getIntent();
        String waiter = nt.getStringExtra("waiter");
        setTitle(waiter);

        logout = (Button) findViewById(R.id.tools_logout);
        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intn = new Intent(getApplicationContext(), Login.class);
                startActivity(intn);
            }
        }));

    }
}
