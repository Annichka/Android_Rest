package com.example.anna.rest;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import order.bean.Prod;

/**
 * Created by Anna on 07/22/17.
 */
public class SearchAdapter extends ArrayAdapter<Prod> implements Filterable {

    private ArrayList<Prod> foundProd;
    private Button add;
    private int activeOrdId;

    public interface myEventListener {
        public void myEvent(int orderid, String prodid, double prodprice);
    }

    private myEventListener myListener;

    public SearchAdapter(Context context, int resource, ArrayList<Prod> allProd, int activeOrdId,  myEventListener myEvent) {
        super(context, resource);
        this.foundProd = allProd;
        this.activeOrdId = activeOrdId;
        this.myListener = myEvent;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Prod p = foundProd.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_search, null);
        }

        TextView foodName = (TextView) convertView.findViewById(R.id.list_search_tvFoodName);
        TextView price = (TextView) convertView.findViewById(R.id.list_search_tvPrice);
        add = (Button) convertView.findViewById(R.id.list_search_badd);

        add.setTag(position);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Prod p = foundProd.get(pos);
                Log.d("ACTIVE ORD", activeOrdId + "");
                Log.d("PRODID", p.getProdId());
                Log.d("PROD < PRICE", p.getProdPrice()+ "");
                myListener.myEvent(activeOrdId, p.getProdId(), p.getProdPrice());
            }
        });
        if(p != null) {
            foodName.setText(p.getProdEng());
            price.setText(p.getProdPrice() + " ლ");
        }
        else {
            foodName.setText("ERROR");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return foundProd.size();
    }
}
