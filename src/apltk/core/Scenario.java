// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Scenario.java

package apltk.core;

import java.util.LinkedList;

public class Scenario
{

    public Scenario()
    {
        interpreterFiles = new LinkedList();
        interpreterParameters = new LinkedList();
        environmentFiles = new LinkedList();
        environmentParameters = new LinkedList();
        toolFiles = new LinkedList();
        toolParameters = new LinkedList();
        start = false;
        steps = 0;
        repeats = 1;
    }

    public LinkedList interpreterFiles;
    public LinkedList interpreterParameters;
    public LinkedList environmentFiles;
    public LinkedList environmentParameters;
    public LinkedList toolFiles;
    public LinkedList toolParameters;
    public boolean start;
    public int steps;
    public int repeats;
}
