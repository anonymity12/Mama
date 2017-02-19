package com.example.t26wodiv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by paul on 2/9/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="myDatabaseName.db";
    public static final String TABLE_NAME="wordList";
    public static final String TABLE_COLUMN_1="word1";
    public static final String TABLE_COLUMN_2="word2";
    public static final String[] COLUMN_ODD={"牛奶","纸巾","水盆","芥末","洗发露","锦寒","国征","梁山伯和祝应台","火锅","电动车","粉丝"};//11 default words
    public static final String[] COLUMN_EVEN={"豆浆","手帕","水桶","辣椒","护发素","瑞瑞","华新","罗密欧和朱丽叶","冒菜","摩托车","米线"};
    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(id integer primary key, "+TABLE_COLUMN_1+" text,"+TABLE_COLUMN_2+" text)");
        for (int i=0;i<=10;i++){
            insertDefaultWords(i,db);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private boolean insertDefaultWords(int i,SQLiteDatabase db){
        //我觉得这样先getWritable最后再close的一个循环需要后期优化；
//        已优化

        ContentValues contentValues=new ContentValues();
        contentValues.put(TABLE_COLUMN_1,COLUMN_ODD[i]);
        contentValues.put(TABLE_COLUMN_2,COLUMN_EVEN[i]);
        db.insertOrThrow(TABLE_NAME,null,contentValues);

        return true;
    }
}
