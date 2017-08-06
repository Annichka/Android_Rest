package com.example.anna.rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DataBaseConnection.DataBase;
import manager.OrderManager;
import manager.TableManager;
import order.bean.OrdersMagida;
import order.dao.OrderDao;
import room.dao.TableDao;

public class Tables extends AppCompatActivity implements View.OnClickListener {

    private List<room.bean.Table> tableList;
    private List<thisTable> thisTableList = new ArrayList<>();
    private OrderDao oDao;
    private TableDao tabDao;
    private GridLayout gridL;
    private int hallId, waiterId;
    private String waiter;

    static class thisTable {
        Button btn;
        boolean free;
        int tableId;
        String tableName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);


        Intent nt = getIntent();
        hallId = nt.getIntExtra("hall", -1) - 1;
        waiterId = nt.getIntExtra("waiterId", -1);
        waiter = nt.getStringExtra("waiter");

        setTitle(waiter);

        gridL = (GridLayout) findViewById(R.id.mainpage_layout);
        gridL.setUseDefaultMargins(true);
        try {
            oDao = (new OrderManager(DataBase.db).getOrderDao());
            tabDao = (new TableManager(DataBase.db).getTableDao());
            tableList = tabDao.getTablesByHall(hallId, waiterId);         // wamovigo kvela magida
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        GridLayout gridL = (GridLayout) findViewById(R.id.mainpage_layout);

        setUpTables(gridL);
    }

    private void setUpTables(GridLayout gridL) {
        if(tableList != null)
        for(int i=0; i<tableList.size(); i++) {
            int activeOrderId = -1;

            List<OrdersMagida> ordTab = oDao.getOrdersMagidaByTableId(tableList.get(i).getTableId());
            activeOrderId = oDao.getActiveOrder(ordTab);

            thisTable table = new thisTable();
            table.free = false;
            table.btn = new Button(this);
            table.btn.setTextColor(Color.WHITE);
            table.btn.setTextSize(15);
            table.tableName = tableList.get(i).getTable();
            table.tableId = tableList.get(i).getTableId();
            String tablename =  table.tableName; //"მაგიდა " + table.tableId;  // tableList.get(i).magida() - saxeli
            table.btn.setText(tablename);
            if (activeOrderId != -1) {
                table.btn.setBackgroundResource(R.drawable.red_table);
            }
            else {
                table.btn.setBackgroundResource(R.drawable.green_table);
                table.free = true;
            }
            table.btn.setId(table.tableId);
            thisTableList.add(table);
            gridL.addView(table.btn);
            table.btn.setOnClickListener(this);
        }
    }

    private thisTable getTableById(int id) throws SQLException {
        for(int i = 0; i < thisTableList.size(); i++)
            if(thisTableList.get(i).tableId == id) {
                thisTable t = thisTableList.get(i);
                return t;
            }
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId(); // magidis ID emtxveva button-is ID-s.
        try {
            final thisTable currTbl = getTableById(id);
            final Intent intn = new Intent(getApplicationContext(), CurrentOrder.class);
            if (currTbl.free) {
                AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle("მაგიდა თავისუფალია");
                alert.setMessage("გნებავთ ახალი შეკვეთის შექმნა?");
                alert.setButton("დიახ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /* sheikmnas axali order */
                        int newOrdId = oDao.makeNewOrder(currTbl.tableId, waiterId);
                        intn.putExtra("activeOrderId", newOrdId);
                        intn.putExtra("tableId", currTbl.tableId);
                        intn.putExtra("hall", hallId);
                        intn.putExtra("waiter", waiter);
                        intn.putExtra("waiterId", waiterId);
                        intn.putExtra("tableName", currTbl.tableName);
                        startActivity(intn);
                    }
                });
                alert.show();
            } else {
                int activeOrderId = oDao.getActiveOrder(oDao.getOrdersMagidaByTableId(id));
                intn.putExtra("tableId", id);
                intn.putExtra("hall", hallId);
                intn.putExtra("waiter", waiter);
                intn.putExtra("waiterId", waiterId);
                intn.putExtra("activeOrderId", activeOrderId);
                intn.putExtra("tableName", currTbl.tableName);
                startActivity(intn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent nt  = new Intent(getApplicationContext(), HallActivity.class);
        nt.putExtra("hall", hallId);
        nt.putExtra("waiter", waiter);
        nt.putExtra("waiterId", waiterId);

        startActivity(nt);
    }
}