package com.example.anna.rest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import DataBaseConnection.DataBase;
import manager.OrderManager;
import order.bean.Order;
import order.dao.OrderDao;


public class testNotif extends Service {

    public static final long CHECK_INTERVAL = 10 * 1000; // 10 seconds

    // onCreate - amovigo magidebi romlebsac konkretuli
    // mimtani emsaxureba.  --- magidebis amogeba araa sawiro,
    // sheidzleba vamowmo am mimtanis gaxsnili orderebi.
    // mere  kovel x wutshi vamowmo bazashi
    // aris tu ara rame cvlileba.

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Timer
        // OK-s rom davawvebi bolo statuss meored vamowmeb magidis.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Timer timer = new Timer();
        SharedPreferences shared = this.getSharedPreferences("Waiter Data", Context.MODE_PRIVATE);
        int WaiterId = shared.getInt("waiterid", -1);

        TimerTask timerTask;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // CHECK DATABASE FOR NOTIFICATIONS
                showNotification();
            }
        };
        timer.schedule(timerTask, 0, 10000);
        return 0;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("NewApi")
    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, testNotif.class), 0);
        Resources r = getResources();

        SharedPreferences shared = this.getSharedPreferences("Waiter Data", Context.MODE_PRIVATE);
        int waiterId = shared.getInt("waiterid", -1);
        OrderDao oDao = null;
        try {
            oDao = (new OrderManager(DataBase.db).getOrderDao());
            List<Order> ords = oDao.ToBeNotified(waiterId);

            for(int i= 0; i < ords.size(); i++) {
                Notification notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle("შეკვეთა დამზადდა")
                        .setContentText(ords.get(i).getNotification())
                        .setContentIntent(pi)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}


