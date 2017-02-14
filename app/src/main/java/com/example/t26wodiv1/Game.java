package com.example.t26wodiv1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.R.attr.data;
import static android.R.attr.defaultHeight;
import static android.R.attr.switchMinWidth;
import static com.example.t26wodiv1.DBHelper.TABLE_COLUMN_1;
import static com.example.t26wodiv1.DBHelper.TABLE_COLUMN_2;
import static com.example.t26wodiv1.DBHelper.TABLE_NAME;

/**
 * Created by paul on 2/5/17.
 */

public class Game extends Activity {
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private StaggeredHomeAdapter mAdapter;
    private int RANDOM_PAIR=(int)(0+Math.random()*12);//Make sure it's 12 instead of 11;
    private static int RANDOM_USER=0;
    private static  String MAN_WORD=null;
    private static  String SPY_WORD=null;
    DBHelper mydb=new DBHelper(this);//Do I need to initial it explicitly?
    private SQLiteDatabase dbReader;
    private int user_count=4;
    private Button query_button;
    private Button finish_button;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        query_button= (Button) findViewById(R.id.game_btn_query);
        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Game.this,"玩家   "+(RANDOM_USER+1)+"   是卧底！！",Toast.LENGTH_SHORT).show();
            }
        });
        finish_button= (Button) findViewById(R.id.game_btn_finish);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle  extras=getIntent().getExtras();
        user_count=extras.getInt("user_count");
        MAN_WORD=extras.getString("man_word");
        SPY_WORD=extras.getString("spy_word");
        //如果是“笨蛋”则说明默认未改变，应从数据库查询数据
        //同时，如果因为MainActivity的那个Edittext不可见，而导致“笨蛋”为“”（即空），也要从数据库查询
        if ((SPY_WORD.equals("笨蛋"))||(SPY_WORD.equals(""))){
            queryDatabaseForAWord();
        }
        shapeUsersInterface();
        RANDOM_USER=(int)(Math.random()*user_count);
        mRecyclerView= (RecyclerView) findViewById(R.id.id_recyclerview);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mAdapter=new StaggeredHomeAdapter(this,mDatas);
        mRecyclerView.setAdapter(mAdapter);




//        queryDatabaseForAWord();
        initEvent();
    }
    private void initEvent(){
        mAdapter.setOnItemClickListener(new StaggeredHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position==RANDOM_USER){
                    Toast.makeText(Game.this,"玩家 "+(position+1)+"的词语是："+SPY_WORD,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Game.this,"玩家 "+(position+1)+"的词语是："+MAN_WORD,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (position==RANDOM_USER){
                    Toast.makeText(Game.this,"玩家"+(position+1)+"    是卧底，游戏结束",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Game.this,"玩家"+(position+1)+"    不是卧底，游戏继续",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryDatabaseForAWord() {
        dbReader=mydb.getReadableDatabase();
        Cursor cursor=dbReader.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToPosition(RANDOM_PAIR);
        Log.d("GameA",">>>>>>> RANDOM_PAIR is:  "+RANDOM_PAIR);
        //// TODO: 2/9/17 定位完成？大概吧，；总之开始取数据吧！
        MAN_WORD=cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_1));
        SPY_WORD=cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_2));
        cursor.close();
        dbReader.close();
    }
    protected void shapeUsersInterface(){
        mDatas=new ArrayList<>();
        for (int i=1;i<=user_count;i++){
            mDatas.add(""+i);
        }
    }








}
