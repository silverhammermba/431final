// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PointInTime.java

package apltk.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PointInTime
{

    public PointInTime()
    {
        nanoTime = 0L;
        currentTimeMillis = 0L;
        nanoTime = System.nanoTime();
        currentTimeMillis = System.currentTimeMillis();
    }

    public String getStartTime()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static long getElapsedTime(PointInTime start, PointInTime end)
    {
        return end.nanoTime - start.nanoTime;
    }

    public void setTimes(long nanoTime, long currentTimeMillis)
    {
        this.nanoTime = nanoTime;
        this.currentTimeMillis = currentTimeMillis;
    }

    private long nanoTime;
    private long currentTimeMillis;
}
