package com.example.anna.rest;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import order.bean.MomzadebaT;
import order.bean.OrdersProd;


public class DialogMomzadebaT extends Dialog {

    public interface eventListener {
        public void getUpdatedData(String value);
    }

    private eventListener myListener;

    public DialogMomzadebaT(Context context, ArrayList<MomzadebaT> momzList, final OrdersProd prod, eventListener listen) {
        super(context);
        setContentView(R.layout.dialog_momzt);
        this.myListener = listen;

        LinearLayout ll = (LinearLayout) findViewById(R.id.momzt_layout);
        final EditText inp = new EditText(context);
        inp.setText(prod.getMomzadebaT());

        Button bok = new Button(context);
        bok.setText("შენახვა");
        bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalText = inp.getText().toString();
                myListener.getUpdatedData(finalText);
                dismiss();
            }
        });
        if(momzList.size() > 0)
            for(int i=0; i<momzList.size(); i++) {
                final CheckBox box = new CheckBox(context);
                box.setTag(momzList.get(i).getId());
                box.setText(momzList.get(i).getMomzadebaT());
                final String st = momzList.get(i).getMomzadebaT();
                if(prod.getMomzadebaT() != null && prod.getMomzadebaT().contains(momzList.get(i).getMomzadebaT()))
                    box.setChecked(true);
                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String txt = inp.getText().toString();
                        String boxText = st;
                        if(isChecked) {
                            inp.setText(txt + boxText + ", ");
                        } else {
                            if(txt.contains(boxText)) {
                                String newTxt = txt.replace(boxText + ", " , "");
                                inp.setText(newTxt);
                            }
                        }
                    }
                });
                ll.addView(box);
            }
        ll.addView(inp);
        ll.addView(bok);
    }
}
