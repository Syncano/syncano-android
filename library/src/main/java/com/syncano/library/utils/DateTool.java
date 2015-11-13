package com.syncano.library.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* Example date
 * 2015-05-20T19:42:09.597885Z
 *
 * Not using date formatters because it works much faster when not using them.
 * Using special format NanosDate because nanoseconds are important for Syncano,
 * especially when comparing dates, or requesting objects updated since some date.
 */
public class DateTool {

    /**
     * UTC timeZone
     */
    private final static TimeZone sUtc = TimeZone.getTimeZone("UTC");

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
        if (stringDate.length() != 27) {
            return null;
        }
        Calendar sCal = Calendar.getInstance(sUtc);
        int year = Integer.parseInt(stringDate.substring(0, 4));
        int month = Integer.parseInt(stringDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(stringDate.substring(8, 10));
        int hourOfDay = Integer.parseInt(stringDate.substring(11, 13));
        int minute = Integer.parseInt(stringDate.substring(14, 16));
        int second = Integer.parseInt(stringDate.substring(17, 19));
        sCal.set(year, month, day, hourOfDay, minute, second);
        int millis = Integer.parseInt(stringDate.substring(20, 23));
        int nanos = Integer.parseInt(stringDate.substring(23, 26));
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
        Calendar sCal = Calendar.getInstance(sUtc);
        sCal.setTime(date);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%04d", sCal.get(Calendar.YEAR)));
        sb.append('-');
        sb.append(String.format("%02d", sCal.get(Calendar.MONTH) + 1));
        sb.append('-');
        sb.append(String.format("%02d", sCal.get(Calendar.DAY_OF_MONTH)));
        sb.append('T');
        sb.append(String.format("%02d", sCal.get(Calendar.HOUR_OF_DAY)));
        sb.append(':');
        sb.append(String.format("%02d", sCal.get(Calendar.MINUTE)));
        sb.append(':');
        sb.append(String.format("%02d", sCal.get(Calendar.SECOND)));
        sb.append('.');
        sb.append(String.format("%03d", sCal.get(Calendar.MILLISECOND)));
        int nanos = 0;
        if (date instanceof NanosDate) {
            nanos = ((NanosDate) date).getNanos();
        }
        sb.append(String.format("%03d", nanos));
        sb.append('Z');
        return sb.toString();
    }
}
