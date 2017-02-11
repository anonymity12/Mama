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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private static final int REQUEST_PHOTO_1=1;
    private static final int REQUEST_PHOTO_2=2;
    private static final int REQUEST_PHOTO_3=3;
    private static final int REQUEST_PHOTO_4=4;
    private final int RANDOM_PAIR=(int)(0+Math.random()*12);//Make sure it's 12 instead of 11;
    private final int RANDOM_USER=(int)(1+Math.random()*4);
    private static  String MAN_WORD=null;
    private static  String SPY_WORD=null;
    DBHelper mydb=new DBHelper(this);//Do I need to initial it explicitly?
    private SQLiteDatabase dbReader;


    String photoPathSequence;
    String photoPaths;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);





        queryDatabaseForWord();
    }

    private void queryDatabaseForWord() {
        dbReader=mydb.getReadableDatabase();
        Cursor cursor=dbReader.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToPosition(RANDOM_PAIR);
        //// TODO: 2/9/17 定位完成？大概吧，；总之开始取数据吧！
        MAN_WORD=cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_1));
        SPY_WORD=cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_2));
        cursor.close();
        dbReader.close();
    }





    private File createImageFile() throws IOException {
        //Create an image file name;
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG_"+timeStamp+"_";
        File storageDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        //save a file:path for use with ACTION_VIEW intents
        photoPaths=image.getAbsolutePath();
        Log.d("In Main",">>>>>>>>Created Image file!!"+photoPaths);

        return image;
    }
    private void dispatchTakePictureIntent(int requestPhotoNum){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            //Create the File where the photo should go
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //Continue only if the File was successfully created
            if (photoFile!=null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,requestPhotoNum);
            }
        }
    }



}
