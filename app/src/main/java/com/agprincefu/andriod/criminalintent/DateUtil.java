package com.agprincefu.andriod.criminalintent;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by agprincefu on 2018/3/27.
 */

public class DateUtil {

    public static String dateSimple(Date date){
        String simpleDate = null;

        simpleDate = DateFormat.format("yyyy--MM--dd",date).toString();

        return simpleDate;
    }
    public static String timeSimple(Date date){
        String simpleTime = null;

        simpleTime = DateFormat.format("HH:mm:ss",date).toString();
        return simpleTime;
    }
}
