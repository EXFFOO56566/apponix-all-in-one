package com.tochy.browser;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDiffUtils {
    public static String getDifferenceInDays(String ogDate) {


        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String[] s1 = ogDate.split("-");
        String thisdate = s1[2].split("T")[0];
        String thismonth = s1[1];
        String thisyear = s1[0];


        String currentdate = "" + month + "/" + date + "/" + year;
        String finaldate = "" + thismonth + "/" + thisdate + "/" + thisyear;


        try {
            //Dates to compare


            Log.d("TAG", "time:  current " + currentdate);
            Log.d("TAG", "time:  api  " + finaldate);
            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(currentdate);
            date2 = dates.parse(finaldate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            Log.e("TAG", "HERE: " + dayDifference);


            if (dayDifference.equals("0")) {
                return "Today";
            } else if (Integer.parseInt(dayDifference) > 31) {
                return finaldate;
            } else {
                return dayDifference + " Days ago";
            }


        } catch (Exception exception) {
            Log.e("TAG", "exception " + exception);
            return finaldate;
        }

    }

}
