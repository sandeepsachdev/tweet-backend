package com.eduonix.projectbackend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    private static SimpleDateFormat format =new SimpleDateFormat("HH:mm");

    public static String formatDate(Date date) {
        return format.format(date);
    }
}
