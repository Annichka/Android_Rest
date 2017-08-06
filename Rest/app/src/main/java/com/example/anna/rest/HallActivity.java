package com.example.anna.rest;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

import java.util.ArrayList;

import DataBaseConnection.DataBase;
import manager.HallManager;
import room.bean.Hall;
import room.dao.HallDao;

/**
 * Created by Anna on 07/15/17.
 */
public class HallActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(getBaseContext(), testNotif.class));

        Intent nt = getIntent();
        String waiter = nt.getStringExtra("waiter");
        int waiterId = nt.getIntExtra("waiterId", -1);
        setTitle(waiter);

        TabHost tabHost = getTabHost();
        tabHost.setHorizontalScrollBarEnabled(true);
        HallDao hd = null;
        ArrayList<Hall> halls = null;
        int hallNum = -1;

        tabHost.setBackgroundColor(Color.LTGRAY);
        try {
            hd = (new HallManager(DataBase.db).getHallDao());
            halls = (ArrayList<Hall>) hd.getHalls();
            hallNum = halls.size();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        for(int i=1; i <= hallNum; i++) {
            Intent nt2 = new Intent(this, Tables.class);
            nt2.putExtra("waiter", waiter);
            nt2.putExtra("waiterId", waiterId);
            nt2.putExtra("hall", i);
            tabHost.addTab(tabHost.newTabSpec("tab" + i).setIndicator(halls.get(i-1).getDarbazi()).setContent(
                    nt2));
        }
        Intent nt3 = new Intent(this, Tools.class);
        nt3.putExtra("waiter", waiter);
        nt3.putExtra("waiterId", waiterId);
        tabHost.addTab(tabHost.newTabSpec("tabExtra").setIndicator("Tools").setContent(nt3));
    }
}
