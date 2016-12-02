// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ToolWindow.java

package apltk.tool;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;

public abstract class ToolWindow
{

    public ToolWindow()
    {
        frame = null;
    }

    public Rectangle getBounds()
    {
        return frame.getBounds();
    }

    public void setLocation(int x, int y)
    {
        frame.setLocation(x, y);
    }

    public void moveLocation(int x, int y)
    {
        Point location = frame.getLocation();
        frame.setLocation(location.x + x, location.y + y);
    }

    protected JFrame frame;
}
