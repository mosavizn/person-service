package com.isc.personservice.util;

class DateFormatterTemporalField
{
    protected static final int YEAR_FIElD = 1;
    protected static final int MONTH_FIElD = 2;
    protected static final int MONTH_STRING_FIElD = 3;
    protected static final int DAY_FIElD = 4;
    protected static final int DAY_STRING_FIElD = 5;
    protected static final int SPACE_FIElD = 6;
    protected static final int DASH_FIElD = 7;
    protected static final int SLASH_FIElD = 8;

    private int count = 0;
    private int type;

    protected DateFormatterTemporalField(int count, int type)
    {
        this.count = count;
        this.type = type;
    }

    protected int getCount()
    {
        return count;
    }

    protected int getType()
    {
        return type;
    }
}
