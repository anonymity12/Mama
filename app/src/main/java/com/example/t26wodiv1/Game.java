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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class Game extends Activity implements View.OnClickListener{
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
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private Button stop_button;
    private Button query_button;
    private String tmpString;
    private Button user1,user2,user3,user4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        imageView1= (ImageView) findViewById(R.id.imageView1);
        imageView2= (ImageView) findViewById(R.id.imageView2);
        imageView3= (ImageView) findViewById(R.id.imageView3);
        imageView4= (ImageView) findViewById(R.id.imageView4);
        stop_button= (Button) findViewById(R.id.stop_game);
        user1= (Button) findViewById(R.id.user1);
        user2= (Button) findViewById(R.id.user2);
        user3= (Button) findViewById(R.id.user3);
        user4= (Button) findViewById(R.id.user4);
        user1.setOnClickListener(this);
        user2.setOnClickListener(this);
        user3.setOnClickListener(this);
        user4.setOnClickListener(this);
        stop_button.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);


        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua5));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua6));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua7));
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua8));


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Log.d("GameA",">>>>>>>>>onResult!!OK!!");
            Bitmap cameraBitmap=BitmapFactory.decodeFile(photoPaths);
            if (cameraBitmap!=null){
                int scale=ImageThumbnail.reckonThumbnail(cameraBitmap.getWidth(),cameraBitmap.getHeight(),500,500);
                Bitmap bitmap=ImageThumbnail.PicZoom(cameraBitmap,cameraBitmap.getWidth()/scale,cameraBitmap.getHeight()/scale);
                cameraBitmap.recycle();
                Log.d("GameA",">>>>>>>>>>>>setting imageX bitmap");
                switch (requestCode){
                    case REQUEST_PHOTO_1:
                        imageView1.setImageBitmap(bitmap);
                        break;
                    case REQUEST_PHOTO_2:
                        imageView2.setImageBitmap(bitmap);
                        break;
                    case REQUEST_PHOTO_3:
                        imageView3.setImageBitmap(bitmap);
                        break;
                    case REQUEST_PHOTO_4:
                        imageView4.setImageBitmap(bitmap);
                        user1.setVisibility(View.VISIBLE);
                        user2.setVisibility(View.VISIBLE);
                        user3.setVisibility(View.VISIBLE);
                        user4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stop_game:
                this.finish();

            case R.id.imageView1:
                dispatchTakePictureIntent(REQUEST_PHOTO_1);
                break;
            case R.id.imageView2:
                dispatchTakePictureIntent(REQUEST_PHOTO_2);
                break;
            case R.id.imageView3:
                dispatchTakePictureIntent(REQUEST_PHOTO_3);
                break;
            case R.id.imageView4:
                dispatchTakePictureIntent(REQUEST_PHOTO_4);
                break;

            case R.id.user1:
                if (1==RANDOM_USER){
                    tmpString = SPY_WORD;
                }else {
                    tmpString=MAN_WORD;
                }
                new AlertDialog.Builder(Game.this).setTitle("your word is :")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage(tmpString)
                        .setPositiveButton("Got it !!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.user2:
                if (2==RANDOM_USER){
                    tmpString = SPY_WORD;
                }else {
                    tmpString=MAN_WORD;
                }
                new AlertDialog.Builder(Game.this).setTitle("your word is :")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage(tmpString)
                        .setPositiveButton("Got it !!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.user3:
                if (3==RANDOM_USER){
                    tmpString = SPY_WORD;
                }else {
                    tmpString=MAN_WORD;
                }
                new AlertDialog.Builder(Game.this).setTitle("your word is :")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage(tmpString)
                        .setPositiveButton("Got it !!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.user4:
                if (4==RANDOM_USER){
                    tmpString = SPY_WORD;
                }else {
                    tmpString=MAN_WORD;
                }
                Log.d("GameA",">>>>>>>>>>>Button4 Clicked and should have dialog show ");
                new AlertDialog.Builder(Game.this).setTitle("your word is :")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage(tmpString)
                        .setPositiveButton("Got it !!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;


        }
    }


    /*
    * Following code cite from google docs online
    * I copied and modified it and make use of it for myself ,the
    * code above the the modified version.
    * */
/*    private void setPic(){
        //Get the dimension of the view
        int targetW=imageView.getWidth();
        int targetH=imageView.getHeight();

        //Get the dimension of the bitmap
        BitmapFactory.Options bmOptions=new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        int photoW=bmOptions.outWidth;
        int photoH=bmOptions.outHeight;

        //Determine how much to scale down the image
        int scaleFactor=Math.min(photoW/targetW,photoH/targetH);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds=false;
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inPurgeable=true;

        Bitmap bitmap=BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        imageView.setImageBitmap(bitmap);
    }*/
}
