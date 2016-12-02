// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParseException.java

package apltk.core;


public class ParseException extends Exception
{

    public ParseException(String path, String message)
    {
        super(message);
        this.path = "";
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    private String path;
}
