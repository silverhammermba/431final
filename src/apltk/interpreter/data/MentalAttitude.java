// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MentalAttitude.java

package apltk.interpreter.data;

import apltk.core.PointInTime;

public class MentalAttitude
{

    public MentalAttitude()
    {
        creationTime = new PointInTime();
        relativeTime = 0L;
    }

    public PointInTime creationTime;
    public long relativeTime;
}
