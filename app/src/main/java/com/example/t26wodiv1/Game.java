package com.example.t26wodiv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;
import static android.R.attr.defaultHeight;
import static android.R.attr.switchMinWidth;

/**
 * Created by paul on 2/5/17.
 */

public class Game extends FragmentActivity {
    private static final int REQUEST_PHOTO_1=1;
    private static final int REQUEST_PHOTO_2=2;
    private static final int REQUEST_PHOTO_3=3;
    private static final int REQUEST_PHOTO_4=4;

    String photoPathSequence;
    String photoPaths;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private Button stop_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        imageView1= (ImageView) findViewById(R.id.imageView1);
        imageView2= (ImageView) findViewById(R.id.imageView2);
        imageView3= (ImageView) findViewById(R.id.imageView3);
        imageView4= (ImageView) findViewById(R.id.imageView4);
        stop_button= (Button) findViewById(R.id.stop_game);


        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua5));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua6));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua7));
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.shuzi_hua8));

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dispatchTakePictureIntent(REQUEST_PHOTO_1);

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_PHOTO_2);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_PHOTO_3);
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_PHOTO_4);
            }
        });
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
