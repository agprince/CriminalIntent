package com.agprincefu.andriod.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

public class PictureUtils {
    private static final String TAG = "agtest";

    public static Bitmap getScaledBitmap(String path, Activity activity){

        Point point = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(point);

        int width = point.x;
        int height = point.y;
        Log.d(TAG, "getScaledBitmap: width: "+width+" height : "+height);

        return getScaledBitmap(path,width,height);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if(srcHeight>destHeight||srcWidth>destWidth){
            int scaleHeight = (int) (srcHeight/destHeight);
            int scaleWidth = (int) (srcWidth/destWidth);
            inSampleSize = scaleHeight>scaleHeight?scaleHeight:scaleWidth;
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;


        return BitmapFactory.decodeFile(path,options);
    }

}
