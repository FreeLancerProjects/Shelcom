package com.appzone.shelcom.share;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeAgo {
    private  final int SECONDS = 1000;
    private  final int MINUTE = SECONDS*60;
    private  final int HOUR = MINUTE*60;
    private  final int DAY = HOUR*24;
    private  final int WEEK = DAY*7;
    private  SimpleDateFormat dateFormat;

    private static TimeAgo instance = null;

    private TimeAgo() {
    }

    public static synchronized TimeAgo newInstance()
    {
        if (instance == null)
        {
            instance = new TimeAgo();
        }
        return instance;
    }

    public  String getTime(long time){

        long now = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
        long diff = Math.abs(now-time);
        Log.e("diff",diff+"__");
        if (diff<=SECONDS)
        {
            return "just now";
        }else if (diff < 2*MINUTE)
        {
            return "a minute ago";
        }else if (diff < 50*MINUTE)
        {
            return diff/MINUTE+" "+"min ago";
        }else if (diff < 90*MINUTE)
        {
            return "an hour ago";
        }else if (diff< 24*HOUR)
        {
            return diff/HOUR +" "+"hr ago";
        }else if (diff < 48*HOUR)
        {
            return "yesterday";
        }else if (diff < 7*DAY)
        {
            return  diff/DAY+" "+"d ago";
        }else if (diff < 2*WEEK)
        {
            return diff/WEEK+" "+"w ago";
        }else if (diff <3.5*WEEK)
        {
            return diff/WEEK+" "+"w ago";
        }else
            {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                return dateFormat.format(new Date(time));
            }
    }
}
