package com.agprincefu.andriod.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        return null;
    }

}
