package com.example.anna.rest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import DataBaseConnection.DataBase;
import manager.ProdManager;
import order.bean.MomzadebaT;
import order.bean.OrdersProd;
import order.bean.Prod;
import order.dao.ProdDao;

/**
 * Created by Anna on 07/22/17.
 */
public class OrderAdapter extends ArrayAdapter<OrdersProd> {

    private class Product {
        String prodName, dateCreate;
        double price, sum;
        int quantity, cookingTime;
    }

    private ArrayList<OrdersProd> ordProd;
    private ArrayList<OrdersProd> nwProd;
    public static Context cont;

    private View v;

    private ProdDao pDao;

    public OrderAdapter(Context context, int textViewResourceId, ArrayList<OrdersProd> ordProd, ArrayList<OrdersProd> newest) {
        super(context, 0, ordProd);
        this.cont = context;
        if(this.ordProd == null)
            this.ordProd = ordProd;
        this.nwProd = newest;
        this.ordProd.addAll(0, nwProd);
    }

    /*
    * Type variable determines which list item should be chosen[enable or disable]
    * */
    public View getView(int position, View convertView, ViewGroup parent){
        int type = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == 0) {
                v = inflater.inflate(R.layout.disable_list_item, parent, false);
            } else {
                v = inflater.inflate(R.layout.enable_list_item, parent, false);
            }
        } else {
            v = convertView;
        }

        final OrdersProd prod = ordProd.get(position);
        final Product p = new Product();
        Prod thisProd = new Prod();

        try {
            pDao = (new ProdManager(DataBase.db).getProdDao());
            thisProd = pDao.findProdById(prod.getProdId());
            String x = prod.getProdId();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(thisProd != null) {
            p.prodName = thisProd.getProdEng();
            p.quantity = prod.getQuantity();
            p.price = prod.getPrice();
            p.sum = p.quantity * p.price;
            p.cookingTime = thisProd.getCookingTime();
            p.dateCreate = prod.getDateCreate();
        } else {
            p.prodName = "ERROR";
        }


        if (prod != null) {
            TextView price, sum;
            price = (TextView) v.findViewById(R.id.list_item_tvPrice);
            sum = (TextView) v.findViewById(R.id.list_item_tvSum);

            if(type == 1) {
                enableItemInit(p, position);
            }
            else {
                disableItemInit(p, position);
            }

            price.setText("" +  Math.round(p.price * 100.0)/100.0 + " ლ");
            sum.setText("" + Math.round(p.sum * 100.0)/100.0 + " ლ");
        }
        return v;
    }

    private void enableItemInit(final Product p, int position) {
        final TextView quantity = (TextView) v.findViewById(R.id.list_item_etQuantity);
        ImageView edit = (ImageView) v.findViewById(R.id.list_item_edit);
        TextView cookingTime = (TextView) v.findViewById(R.id.list_item_tvEnCookingTime);

        onClickChange(quantity, position);
        onClickEdit(edit, position);
        addButton(v, position);
        removeButton(v, position);
        enableFoodName(v, position, p);
        quantity.setText(p.quantity + "");
        cookingTime.setText(p.cookingTime + " წთ");
        cookingTime.setTextColor(Color.GREEN);
    }

    private void disableItemInit(final Product p, int position) {
        TextView quant = (TextView) v.findViewById(R.id.list_item_tvQuantity);
        TextView cookingTime = (TextView) v.findViewById(R.id.list_item_tvDsbCookingTime);
        quant.setText(p.quantity + " ცალი");
        cookingTime.setText(countProdPrepareTime(p.dateCreate, p.cookingTime));
        cookingTime.setTextColor(Color.GREEN);
        disableFoodName(v, position, p);
    }

    private String countProdPrepareTime(String dateCrt, int cookTime) {
        String time = "";
        int firstDelim = dateCrt.indexOf(":");

       // int dateToNum = Integer.parseInt(dateCrt.substring(firstDelim-2, firstDelim))*60 +
         //       Integer.parseInt(dateCrt.substring(firstDelim+1, firstDelim+3)) + cookTime;

        int dateToNum = 40;

        int hours = dateToNum/60;
        int mins = dateToNum - hours*60;
        time = hours +":";
        if(mins <= 9)
            time += "0" + mins + " სთ";
        else
            time += mins + " სთ";
        return time;
    }


    private void customEditQuantity(View v) {
        final Dialog dialog = new Dialog(getContext());

        //setting custom layout to dialog
        dialog.setContentView(R.layout.dialog_quantity);

        //adding text dynamically
        final EditText txt = (EditText) dialog.findViewById(R.id.dialog_etQuant);
        Button ok = (Button) dialog.findViewById(R.id.dialog_bOk);
        ok.setTag((int) v.getTag());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inp = txt.getText().toString();
                if (inp.equals("")) {
                    txt.setError("შეიყვანეთ რაოდენობა");
                    return;
                }
                try {
                    int newQnt = Integer.parseInt(inp);
                    if (newQnt < 0) {
                        txt.setError("შეიყვანეთ ვალიდური რაოდენობა");
                        return;
                    }
                    int tag = (int) v.getTag();
                    OrdersProd prd = OrderAdapter.this.getItem(tag);
                    if (newQnt == 0) {
                        OrderAdapter.this.remove(prd);
                        nwProd.remove(prd);
                    } else
                        OrderAdapter.this.getItem(tag).setQuantity(newQnt);
                    notifyDataSetChanged();
                    dialog.dismiss();
                } catch (Exception e) {
                }
            }
        });
        dialog.show();
    }

    private void onClickEdit(ImageView edit, int position) {
        edit.setTag(position);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customEditQuantity(v);
            }
        });
    }

    private void onClickChange(TextView quantity, int position) {
        quantity.setTextColor(Color.BLUE);
        quantity.setTag(position);
        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customEditQuantity(v);
            }
        });
    }

    private void disableFoodName(View v, int position, Product p) {
        TextView dsbFoodName = (TextView) v.findViewById(R.id.list_item_tvDsbFoodName);
        ImageView dsbNote = (ImageView) v.findViewById(R.id.list_item_bDsbNote);

        dsbFoodName.setTag(position);
        dsbFoodName.setText(p.prodName);
        dsbFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                getNote(pos);
            }
        });

        dsbNote.setTag(position);
        dsbNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                getNote(pos);
            }
        });
    }

    private void enableFoodName(View v, int position, Product p) {
        TextView enFoodName = (TextView)v.findViewById(R.id.list_item_tvEnFoodName);
        ImageView bnote = (ImageView) v.findViewById(R.id.list_item_bnote);

        enFoodName.setText(p.prodName);
        enFoodName.setTag(position);
        enFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                addNote(pos);
            }
        });

        bnote.setTag(position);
        bnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                addNote(pos);
            }
        });
    }

    private void addNote(final int prodPos) {
        ArrayList<MomzadebaT> momzList = null;
        try {
            OrdersProd thisProd = ordProd.get(prodPos);
            momzList = (new ProdManager(DataBase.db).getProdDao()).getMomzadebaListByProd(thisProd.getProdId());

            DialogMomzadebaT dm = new DialogMomzadebaT(getContext(), momzList, thisProd,
                    (new DialogMomzadebaT.eventListener() {
                        @Override
                        public void getUpdatedData(String value) {
                            ordProd.get(prodPos).setMomzadebaT(value);
                        }
                    }));
            dm.show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getNote(int prodPos) {
        OrdersProd thisProd = ordProd.get(prodPos);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(thisProd.getMomzadebaT());
        builder.setPositiveButton("დახურვა", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void removeButton(View v, int position) {
        Button remove = (Button) v.findViewById(R.id.list_item_bremove);
        remove.setTag(position);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos2 = (int) v.getTag();
                OrdersProd prod1 = ordProd.get(pos2);
                if (prod1.getQuantity() == 1) {
                    OrderAdapter.this.remove(prod1);
                    nwProd.remove(prod1);
                } else {
                    OrderAdapter.this.getItem(pos2).setQuantity(prod1.getQuantity() - 1);
                }
                notifyDataSetChanged();
            }
        });
    }

    private void addButton(View v, int position) {
        Button add = (Button) v.findViewById(R.id.list_item_badd);
        add.setTag(position);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos1 = (int) v.getTag();
                OrdersProd prod1 = ordProd.get(pos1);
                OrderAdapter.this.getItem(pos1).setQuantity(prod1.getQuantity() + 1);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return ordProd.get(position).isEnable() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}

