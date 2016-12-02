// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AppWindow.java

package apltk.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class SpecFileFilter extends FileFilter
{

    SpecFileFilter()
    {
    }

    public boolean accept(File pathname)
    {
        if(pathname.isDirectory())
            return true;
        return pathname.getName().endsWith(".xml");
    }

    public String getDescription()
    {
        return "XML-Files";
    }
}
