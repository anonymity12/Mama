package com.example.t26wodiv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by paul on 2/8/17.
 */

public class ImageThumbnail {
    public  static int reckonThumbnail(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if ((oldHeight > newHeight&& oldWidth >newWidth)
                || (oldHeight<= newHeight &&oldWidth > newWidth)) {
            int be =(int) (oldWidth / (float) newWidth);
            if (be<= 1)
                be = 1;
            return be;
        } else if (oldHeight > newHeight&& oldWidth <=newWidth) {
            int be =(int) (oldHeight / (float) newHeight);
            if (be<= 1)
                be = 1;
            return be;
        }
        return 1;
    }
    public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth,(float) height / bmpHeght);

        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth,bmpHeght, matrix, true);
    }

    public static String savePhotoToLocal(Intent data, Bitmap btp){
        //如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
        File rootdir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
//            String imagerDir =rootdir.getPath() + WorkUpload.SDCARD_PATH;
//            File dirpath =createDir(imagerDir);
        String filename = "TT"+System.currentTimeMillis() + ".jpg";
        File tempFile = new File(rootdir, filename);
        String filePath =tempFile.getAbsolutePath();
        try {
            //将bitmap转为jpg文件保存
            FileOutputStream fileOut = new FileOutputStream(tempFile);
            btp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
