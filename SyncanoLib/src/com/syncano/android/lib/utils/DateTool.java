package com.syncano.android.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* example dates
 * 2012-09-16T04:54:08.452056+00:00 32
 * 2012-10-22T07:10:00+00:00 25  */

public class DateTool {

    /**
     * Simple date format used to parse and format every date from and to API
     */
    private final static SimpleDateFormat sSdfShort = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'000+00:00\'");
    private final static SimpleDateFormat sSdfNanos = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.");
    /**
     * Calendar instance with GMT timezone
     */
    private final static Calendar sCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    static {
        TimeZone sUtc = TimeZone.getTimeZone("UTC");
        sSdfShort.setTimeZone(sUtc);
        sSdfNanos.setTimeZone(sUtc);
    }

    /**
     * Method to parse String to Date object
     *
     * @param stringDate String representing date that will be parsed
     * @return Date object created from stringDate
     */
    public static Date parseString(String stringDate) {
        if (stringDate == null || stringDate.length() == 0) {
            return null;
        }
        try {
            Date date = parse(stringDate);
            return date;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Method to parse String to Date
     *
     * @param stringDate Date in string
     * @return Date parsed from stringDate
     * @throws NumberFormatException if date is in invalid format
     */
    private static Date parse(String stringDate) throws NumberFormatException {
        if (stringDate.length() != 25 && stringDate.length() != 32 && stringDate.length() != 31)
            return null;
        int year = Integer.parseInt(stringDate.substring(0, 4));
        int month = Integer.parseInt(stringDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(stringDate.substring(8, 10));
        int hourOfDay = Integer.parseInt(stringDate.substring(11, 13));
        int minute = Integer.parseInt(stringDate.substring(14, 16));
        int second = Integer.parseInt(stringDate.substring(17, 19));
        sCal.set(year, month, day, hourOfDay, minute, second);
        int nanos = 0;
        int millis = 0;
        if (stringDate.length() == 32 || stringDate.length() == 31) {
            millis = Integer.parseInt(stringDate.substring(20, 23));
            nanos = Integer.parseInt(stringDate.substring(20, 26));
        }
        sCal.set(Calendar.MILLISECOND, millis);
        NanosDate nDate = new NanosDate(sCal.getTimeInMillis());
        nDate.setNanos(nanos);
        return nDate;
    }

    /**
     * Method to parse date from Date to String with date format specified in sSdfShort
     *
     * @param date Date object that should be parsed
     * @return Formated date in string
     */
    public static String parseDate(Date date) {
        if (date == null) return null;
        if (!(date instanceof NanosDate)) {
            return sSdfShort.format(date);
        }
        NanosDate nDate = (NanosDate) date;
        return sSdfNanos.format(nDate) + String.format("%06d", nDate.getNanos()) + "+00:00";
    }
}
