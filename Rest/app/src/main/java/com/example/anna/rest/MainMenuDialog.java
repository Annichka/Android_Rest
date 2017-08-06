package com.example.anna.rest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

import DataBaseConnection.DataBase;
import manager.ProdManager;
import order.bean.Prod;
import order.bean.ProdTG;


public class MainMenuDialog extends Dialog {

    public static interface newProdListener {
        public void getNewProdInfo(String prodid, double prodprice);
    }

    private newProdListener getData;

    public MainMenuDialog(Context context, ArrayList<ProdTG> tgList, final int activeOrder, newProdListener myListener) {
        super(context);
        setContentView(R.layout.dialog_main_menu);

        getData = myListener;

        createTabs(tgList, activeOrder);
        addCancelButton();
    }

    private void createTabs(ArrayList<ProdTG> tgList, final int active) {
        try {
            TabHost tabs = (TabHost) this.findViewById(R.id.dialog_main_menu_tabhost);
            tabs.setup();

            tabs.setHorizontalScrollBarEnabled(true);
            tabs.setBackgroundColor(Color.LTGRAY);

            for(int i=0; i < tgList.size(); i++) {
                final ArrayList<Prod> ps = (new ProdManager(DataBase.db).getProdDao()).getMainMenuByTG(tgList.get(i).getIdprodtg());

                TabHost.TabSpec tabpage1 = tabs.newTabSpec("tab");

                tabpage1.setIndicator(tgList.get(i).getProdtg());
                tabpage1.setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        LinearLayout panel = new LinearLayout(getContext());
                        panel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        panel.setOrientation(LinearLayout.VERTICAL);

                        ListView ww = new ListView(getContext());
                        SearchAdapter ada = new SearchAdapter(getContext(), R.layout.list_search, ps, active,
                                (new SearchAdapter.myEventListener() {
                                    @Override
                                    public void myEvent(int orderId, String prodId, double prodPrice) {
                                        Log.d("PRODUCTIS IDDD??", prodId);
                                        getData.getNewProdInfo(prodId, prodPrice);
                                    }
                                }));
                        ww.setAdapter(ada);
                        panel.addView(ww);
                        return panel;
                    }
                });
                tabs.addTab(tabpage1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addCancelButton() {
        LinearLayout linlay = (LinearLayout) findViewById(R.id.dialog_main_menu_layout);
        linlay.setBackgroundColor(Color.GRAY);
        Button close = new Button(getContext());
        close.setText("დახურვა");
        close.setTextColor(Color.WHITE);
        close.setBackgroundResource(R.drawable.finish_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        linlay.addView(close);
    }
}
