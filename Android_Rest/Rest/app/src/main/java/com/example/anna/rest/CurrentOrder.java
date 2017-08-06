package com.example.anna.rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import DataBaseConnection.DataBase;
import manager.OrderManager;
import manager.ProdManager;
import manager.ProdTGManager;
import order.bean.OrdersProd;
import order.bean.Prod;
import order.bean.ProdTG;
import order.dao.OrderDao;
import order.dao.ProdDao;
import order.dao.ProdTGDao;

public class CurrentOrder extends AppCompatActivity implements View.OnClickListener {

    private static final int DATE_DELIMITER_POSITION = 10;
    private static final double MONEY_ROUND = 100.0;

    private OrderAdapter adapt;
    private ArrayList<OrdersProd> currOrdP = new ArrayList<>();
    private ArrayList<OrdersProd> addingNow = new ArrayList<>();
    private Intent intn;
    private int tableId, activeOrdId, momsProc;
    private OrderDao oDao;
    private ProdDao pDao;
    private TextView sumT;
    private Button finishB, mainMenuB, saveB, FinLast;
    private SearchView search;
    private ListView result;
    private int waiterId;
    private String tableName;

    private String waiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        intn = getIntent();
        waiter = intn.getStringExtra("waiter");
        waiterId = intn.getIntExtra("waiterId", -1);
        tableId = intn.getIntExtra("tableId", -1);
        activeOrdId = intn.getIntExtra("activeOrderId", -1);
        tableName = intn.getStringExtra("tableName");

        sumT = (TextView) findViewById(R.id.existorder_sum);
        finishB = (Button) findViewById(R.id.existorder_brefresh);
        finishB.setOnClickListener((View.OnClickListener) this);
        FinLast = (Button) findViewById(R.id.existorder_bFinish);
        FinLast.setOnClickListener((View.OnClickListener) this);
        mainMenuB = (Button) findViewById(R.id.existorder_bMainMenu);
        mainMenuB.setOnClickListener((View.OnClickListener) this);
        saveB = (Button) findViewById(R.id.existorder_bSave);
        saveB.setOnClickListener((View.OnClickListener) this);

        //setTitle("მაგიდა " + tableId);
        setTitle(tableName);
        /**
         * აქ ვიღებ ბაზიდან პროდუქტებს, რომ გადავცე ArrayAdapter-ს. */
        takeOrdersProd();

        try {
            OrderDao ordD = (new OrderManager(DataBase.db).getOrderDao());
            momsProc = ordD.getMomsProc(activeOrdId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        callAdapter();
        searchProd();
    }

    private void callAdapter() {
        adapt = new OrderAdapter(this, R.layout.enable_list_item, currOrdP, addingNow){
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                countSum();
            }
        };

        ListView w = (ListView) findViewById(R.id.existorder_lvorder);
        w.setAdapter(adapt);
        if(!adapt.isEmpty())
            countSum();
    }

    /* პროდუქტის მოძებნა პროდუქტების მთლიან ბაზაში. */
    private void searchProd() {
        result = (ListView) findViewById(R.id.existorder_lvsearch);
        result.setVisibility(View.GONE);
        search = (SearchView) findViewById(R.id.existorder_search);
        search.setQueryHint("მწვადი, პიცა...");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            private ArrayList<Prod> getFoundProds(String prod) {
                ArrayList<Prod> foundProd = new ArrayList<Prod>();
                try {
                    pDao = (new ProdManager(DataBase.db).getProdDao());
                    foundProd = pDao.findProdByName(prod);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return foundProd;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<order.bean.Prod> foundProd = new ArrayList<>();
                if (!newText.equals("")) {
                    foundProd = getFoundProds(newText);
                }

                if (foundProd.size() > 0) {
                    result.setVisibility(View.VISIBLE);
                    SearchAdapter arrp = getSearchItems(foundProd, false);

                    result.setAdapter(arrp);
                } else {
                    result.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    /**
     *  Click on Finish Button.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.existorder_brefresh) {
            finish();
            startActivity(getIntent());
        }
        else  if (v.getId() == R.id.existorder_bFinish) {
            ///// FINISH -is CODE   - NOT NEEDED ANY MORE
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("გნებავთ მაგიდის დასრულება?");
            alert.setButton("დიახ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    OrderDao oDao = null;
                    try {
                        oDao = (new OrderManager(DataBase.db).getOrderDao());
                        if (activeOrdId == -1)
                            activeOrdId = oDao.getActiveOrder(oDao.getOrdersMagidaByTableId(tableId));
                        oDao.addAllProd(addingNow, activeOrdId);
                        oDao.finishOrder(activeOrdId, countSum());  // TODO: aq bolos active-is 0-ad gaxdomac unda wavushalo.
                        Intent nt = new Intent(getApplicationContext(), HallActivity.class);
                        nt.putExtra("waiter", waiter);
                        nt.putExtra("waiterId", waiterId);
                        startActivity(nt);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            alert.show();
        }
        //MainMenuB Activity
        else if (v.getId() == R.id.existorder_bMainMenu){
            ArrayList<ProdTG> tgList;
            MainMenuDialog alert;
            try {
                ProdTGDao tgDao = (new ProdTGManager(DataBase.db).getProdTGDao());
                pDao = (new ProdManager(DataBase.db).getProdDao());
                tgList = tgDao.getMainMenuTG();

                alert = new MainMenuDialog(this, tgList, activeOrdId, new MainMenuDialog.newProdListener() {
                    @Override
                    public void getNewProdInfo(String prodid, double prodprice) {
                        addSearchProd(prodid, prodprice, true);
                    }
                });
                alert.show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(v.getId() == R.id.existorder_bSave) {
            oDao.addAllProd(addingNow, activeOrdId);
            addingNow.clear();
        }
    }

    private SearchAdapter getSearchItems(ArrayList<Prod> searchProds, final boolean increase) {
        SearchAdapter arrp = new SearchAdapter(getBaseContext(),
                R.layout.list_search, searchProds, activeOrdId, (new SearchAdapter.myEventListener() {
            @Override
            public void myEvent(int orderId, String prodId, double prodPrice) {  //Prod Found Event
                Log.d("PRODUCT ID", prodId);
                addSearchProd(prodId, prodPrice, increase);
            }
        }));
       return arrp;
    }

    private void addSearchProd(String prodId, double prodPrice, boolean editable) {
        OrderDao oDao = null;
        try {
            oDao = (new OrderManager(DataBase.db).getOrderDao());
            Prod p = new Prod();
            p.setProdId(prodId);
            p.setProdPrice(prodPrice);
            result.setVisibility(View.GONE);
            search.setQuery("", false);
            currOrdP.clear();
            currOrdP = oDao.getOrdersProdByOrderId(activeOrdId);

            OrdersProd nw = prodToOrdersProd(activeOrdId, p);
            nw.setEnable(true);
            nw.setOrdersProdStatusId(1); // statusID 1 = შეკვეთა მიღებულია

          if(editable) {
                int pos = contains(addingNow, nw);
                if (pos >= 0)
                    addingNow.get(pos).setQuantity(addingNow.get(pos).getQuantity() + 1);
                else
                    addingNow.add(0, nw);
            } else
                addingNow.add(0, nw);

            callAdapter();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int contains(ArrayList<OrdersProd> data, OrdersProd elem) {
        for(int i=0; i < data.size(); i++)
            if(data.get(i).getProdId() == elem.getProdId())
                return i;
        return -1;
    }

    private void takeOrdersProd() {
        try {
            oDao = (new OrderManager(DataBase.db).getOrderDao());
            currOrdP = oDao.getOrdersProdByOrderId(activeOrdId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double countSum() {
        double sum = 0;
        for(int i=0; i< currOrdP.size(); i++) {
            sum += currOrdP.get(i).getSum();
        }
        double momsTanxa = sum * momsProc/MONEY_ROUND;
        double Sul = momsTanxa + sum;

        String sumTxt = "ჯამი: " + Math.round(sum*MONEY_ROUND)/MONEY_ROUND + " + " + Math.round(momsTanxa*MONEY_ROUND)/MONEY_ROUND +
                " (" + momsProc + "%) = "+ Math.round(Sul * MONEY_ROUND) / MONEY_ROUND + "\n";
       // String latestDate = oDao.getLatestDate(activeOrdId);
     ///   if(latestDate.length() > 0)
        //    latestDate = "ბოლო შეკვეთა: " + latestDate.substring(DATE_DELIMITER_POSITION).substring(0, 6);

        sumT.setText(sumTxt);// + latestDate);
        return Sul;
    }

    /*
     * მაგიდიდან როცა უკან ბრუნდება, მაგიდის შეკვეთა ინახება ბაზაში.
     */
    @Override
    public void onBackPressed() {
       // oDao.addAllProd(addingNow, activeOrdId);
        Intent nt = new Intent(getApplicationContext(), HallActivity.class);
        nt.putExtra("waiter", waiter);
        nt.putExtra("waiterId", waiterId);
        startActivity(nt);
    }

    private OrdersProd prodToOrdersProd(int orderId, Prod p) {
        OrdersProd ordP = new OrdersProd();
        ordP.setQuantity(1);
        ordP.setProdId(p.getProdId());
        ordP.setPrice(p.getProdPrice());
        ordP.setOrdersId(orderId);
        return ordP;
    }
}


/*
    1. dasrulebidan gadaxda amovigo      +
    2. dziritadi menus damateba -button  +
    3. magida gamochndes  +
    4. momsaxurebis %  +
    5. raodenobis chawera +
    6. magidebi - gvaris mixedvit. +
    7. damatebuli orderis Disable. +
 */

/**
 მთავარში - გავზარდო  +
 ზემოთ ჩავამატო ახალი შეკვეთა  +
 ProdTG - tabebi mtavar meniushi +
 momzadebaT - suratis click-ze.  +
 FasiStandart(= Fasi) - ordersProd.  +
 moms % gamochndes  +
 */

/*
* bolo shekvetis dro. +
* suratze rac iwereba saxelze gadmovitano. +
*/

/*
* axali orderis shekmnis Lock.
* refresh button.  +
* momzadebis dro, -> V - notification - sawmeli damzadda.  +
* ordersProd -momzadebulia tu ara (statusi)
* */

/*
* ორდერის 'უკუღმა' დალაგება. (პირველი პროდუქტი ბოლოში) --- > OrderDao, getOrdersProdByOrderId --- > array.add(0, prod)
* */






