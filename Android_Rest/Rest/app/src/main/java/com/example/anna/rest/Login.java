package com.example.anna.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import DataBaseConnection.DataBase;
import manager.WaiterManager;
import room.bean.Waiter;
import room.dao.WaiterDao;

import android.os.StrictMode;

import java.sql.SQLException;

public class Login extends Activity implements View.OnClickListener {

    private Button btnLogin;
    private EditText txtUser;
    private ProgressBar pb;
    private DataBase db;
    private Intent intn;
    private String waiter;
    private int waiterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnLogin = (Button) findViewById(R.id.login_btn_login);
        txtUser = (EditText)findViewById(R.id.login_txt_Pass);
        pb = (ProgressBar)findViewById(R.id.login_pb);
        txtUser.setHint("შეიყვანეთ თქვენი ID");
        txtUser.setImeActionLabel("Login", KeyEvent.KEYCODE_ENTER);
        btnLogin.setOnClickListener((View.OnClickListener) this);

        waiter = "";
        InitUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                WaiterDao wd= null;
                try {
                    String s = txtUser.getText().toString();
                    if(!s.equals("")){
                        wd = (new WaiterManager(db)).getWaiterDao();
                        Waiter wtr = wd.getWaiterByCard(Integer.parseInt(s));
                        if(wtr == null) {
                            txtUser.setError("ასეთი მომხმარებელი არ არსებობს");
                            return;
                        }
                        waiter = wtr.toString();
                        waiterId = wtr.getIdGvari();
                    } else {
                        txtUser.setError("გთხოვთ შეიყვანოთ მომხმარებელი");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(!waiter.equals("")) {
                    intn = new Intent(this, HallActivity.class);
                    intn.putExtra("waiter", waiter);
                    intn.putExtra("waiterId", waiterId);
                    shareWaiterId(waiterId);
                    startActivity(intn);
                    break;
                }
        }
    }

    private void InitUser() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    db = new DataBase();
                    DataBase.db=db;
                }
                catch (final Exception ex){
                    Log.d("Login", ex.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(ex.getMessage().contains("UserNotFound"))
                                txtUser.setError("მომხმარებელი ვერ მოიძებნა");
                            else
                                Toast.makeText(Login.this, "ვერ მოხერხდა სერვერთან დაკავშირება", Toast.LENGTH_LONG).show();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                        }
                    });
                    return;
                }
            }
        });
        t.start();
    }

    private void shareWaiterId(int id) {
        SharedPreferences pref = this.getSharedPreferences("Waiter Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt("waiterid", id);
        ed.commit();
    }
}