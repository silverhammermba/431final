// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   App.java

package apltk;

import apltk.core.Core;
import apltk.core.CoreException;
import apltk.gui.AppWindow;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

// Referenced classes of package apltk:
//            SpecFileFilter

public class App
{

    private App()
    {
    }

    public static void main(String args[])
    {
        AppWindow window = new AppWindow();
        if(args.length != 0)
            if(args.length == 1)
                window.loadSpecification(new File(args[0]));
            else
            if(args.length <= 1);
    }

    public static void oldmain(String args[])
    {
        File specFile = null;
        if(args.length == 0)
        {
            JFileChooser fc = new JFileChooser((new File(".")).getAbsolutePath());
            FileFilter filter = new SpecFileFilter();
            fc.setFileFilter(filter);
            fc.setDialogType(0);
            fc.setDialogTitle("Please select a specification file");
            int returnVal = fc.showOpenDialog(null);
            if(returnVal == 0)
                specFile = fc.getSelectedFile();
            else
                System.exit(0);
        } else
        if(args.length == 1)
        {
            specFile = new File(args[0]);
        } else
        {
            System.out.println("Too many parameters!");
            System.exit(0);
        }
        try
        {
            (new Core(specFile)).run();
        }
        catch(CoreException e)
        {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
