package com.isc.personservice.util;

public enum DayOfWeekPersian
{
    /**
     * The singleton instance for the day of Yekshanbeh
     * This has the numeric value of {@code 1}.
     */
    Yekshanbeh,
    /**
     * The singleton instance for the day of Doshanbeh
     * This has the numeric value of {@code 2}.
     */
    Doshanbeh,
    /**
     * The singleton instance for the day of Seshhanbeh
     * This has the numeric value of {@code 3}.
     */
    Seshhanbeh,
    /**
     * The singleton instance for the day of Chaharshanbeh
     * This has the numeric value of {@code 4}.
     */
    Chaharshanbeh,
    /**
     * The singleton instance for the day of Panjshanbeh
     * This has the numeric value of {@code 5}.
     */
    Panjshanbeh,
    /**
     * The singleton instance for the day of Jomeh
     * This has the numeric value of {@code 6}.
     */
    Jomeh,
    /**
     * The singleton instance for the day of Shanbeh
     * This has the numeric value of {@code 7}.
     */
    Shanbeh;

    final static String[] PERSIAN_WEEKDAYS_EN = {"Yekshanbeh", "Doshanbeh", "Seshhanbeh", "Chaharshanbeh", "Panjshanbeh", "Jomeh", "Shanbeh"};
    final static String[] PERSIAN_WEEKDAYS_FA = {"یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه", "شنبه"};

    //-----------------------------------------------------------------------

    /**
     * Gets the days-of-week {@code int} value.
     * <p>
     * The values are numbered following the ISO-8601 standard,
     * from 1 (Yekshanbeh) to 7 (Shanbe).
     *
     * @return the days-of-week, from 1 (Yekshanbeh) to 12 (Shanbe)
     */

    public int getValue()
    {
        // return PERSIAN_WEEKDAYS_EN[ordinal()];
        return ordinal();
    }

    public static DayOfWeekPersian getEnum(int value)
    {
        for (DayOfWeekPersian dayOfWeekPersian : DayOfWeekPersian.values())
        {
            if (dayOfWeekPersian.getValue() == value)
                return dayOfWeekPersian;
        }
        return null;//For values out of enum scope
    }

    public String getStringInPersian()
    {
        return PERSIAN_WEEKDAYS_FA[getValue()];
    }

    public String getStringInEnglish()
    {
        return PERSIAN_WEEKDAYS_EN[getValue()];
    }
}
