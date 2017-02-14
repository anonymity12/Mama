package com.example.t26wodiv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private EditText editText,man_word,spy_word;
    private int user_count=4;
    private Button custom_game_button;
    private LinearLayout linearLayout;
    private String st_man,st_spy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.edit_text_main);
        custom_game_button= (Button) findViewById(R.id.custom_game);
        linearLayout= (LinearLayout) findViewById(R.id.main_empty_layout);
        man_word= (EditText) findViewById(R.id.man_word);
        spy_word= (EditText) findViewById(R.id.spy_word);
        custom_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        Log.d("MainA",">>>>>>>>>>>>>>>I got the integer of EditText is :"+user_count);
    }
    public void onClick(View view){
        Intent i=new Intent(this,Game.class);
        Bundle digitalBundle=new Bundle();
        if (editText.getText()!=null){
            user_count=Integer.parseInt(editText.getText().toString());
        }
        if (linearLayout.isLaidOut()){
            st_man=man_word.getText().toString();
            Log.d("MainA",">>>>>>>>>>>the st_man is :"+st_man);
            st_spy=spy_word.getText().toString();
            digitalBundle.putString("man_word",st_man);
            digitalBundle.putString("spy_word",st_spy);
        }
        digitalBundle.putInt("user_count",user_count);
        i.putExtras(digitalBundle);

        startActivity(i);
    }
}
