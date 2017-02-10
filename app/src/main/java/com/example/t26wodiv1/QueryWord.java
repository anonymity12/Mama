package com.example.t26wodiv1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by paul on 2/9/17.
 */

public class QueryWord extends Dialog {
    private final View keys[]=new View[4];
    private View keypad;
    public QueryWord(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        findViews();
        setListeners();
    }

    private void findViews() {
        keypad=findViewById(R.id.keypad);
        keys[0]=findViewById(R.id.keypad1);
        keys[1]=findViewById(R.id.keypad2);
        keys[2]=findViewById(R.id.keypad3);
        keys[3]=findViewById(R.id.keypad4);
    }
    private void setListeners(){
        for (int i=0;i<keys.length;i++){
            final int t=i+1;
            keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Dialog show time!!!
                    //Alert: I never use the 'getOwnerActivity' maybe there will be bug here!
                    new AlertDialog.Builder(getOwnerActivity()).setTitle("你的词是： ")
                            .setIconAttribute(android.R.attr.alertDialogIcon)
                            .setMessage("你的单词，在数据库查询中")
                            .setPositiveButton("知道了", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    }).show();
                }
            });
        }
    }
}
