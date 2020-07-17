package com.eduonix.projectbackend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static SimpleDateFormat hourMin
            =new SimpleDateFormat("HH:mm");

    public static String formatDate(Date date) {
        return hourMin.format(date);
    }
}
