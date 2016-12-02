// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AppWindow.java

package apltk.gui;

import apltk.core.Core;
import apltk.core.CoreException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

// Referenced classes of package apltk.gui:
//            ControlToolBar, SpecFileFilter

public class AppWindow
    implements ActionListener, ComponentListener
{

    public AppWindow()
    {
        frame = null;
        location = null;
        specFile = null;
        core = null;
        controlToolBar = null;
        frame = new JFrame("APLTK");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileOpenItem = new JMenuItem("Open...");
        fileOpenItem.addActionListener(this);
        fileMenu.add(fileOpenItem);
        JMenuItem fileQuitItem = new JMenuItem("Quit");
        fileQuitItem.addActionListener(this);
        fileMenu.add(fileQuitItem);
        menuBar.add(fileMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpAboutItem = new JMenuItem("About...");
        helpAboutItem.addActionListener(this);
        helpMenu.add(helpAboutItem);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        controlToolBar = new ControlToolBar(this);
        frame.add(controlToolBar, "North");
        frame.addComponentListener(this);
        frame.setSize(200, 200);
        frame.setVisible(true);
        location = frame.getLocation();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Open..."))
        {
            JFileChooser fc = new JFileChooser((new File(".")).getAbsolutePath());
            FileFilter filter = new SpecFileFilter();
            fc.setFileFilter(filter);
            fc.setDialogType(0);
            fc.setDialogTitle("Please select a specification file");
            int returnVal = fc.showOpenDialog(null);
            if(returnVal == 0)
            {
                if(core != null)
                {
                    int opt = JOptionPane.showConfirmDialog(frame, "Do you really want to shut down the current excution?", "", 0);
                    if(opt == 1)
                        return;
                }
                loadSpecification(fc.getSelectedFile());
            }
        } else
        if(e.getActionCommand().equals("Quit"))
            System.exit(0);
        else
        if(!e.getActionCommand().equals("About..."))
            if(e.getActionCommand().equals("start"))
                core.startCycle();
            else
            if(e.getActionCommand().equals("pause"))
                core.pauseCycle();
            else
                throw new AssertionError((new StringBuilder()).append("Unknown command ").append(e.getActionCommand()).toString());
    }

    public void loadSpecification(File file)
    {
        try
        {
            core = new Core(file);
        }
        catch(CoreException e1)
        {
            JOptionPane.showMessageDialog(frame, (new StringBuilder()).append("Could not parse specification-file: ").append(e1.getMessage()).toString(), "Parse error", 0);
            return;
        }
        specFile = file;
        controlToolBar.setButtonsEnabled(true);
        core.start();
    }

    public void componentHidden(ComponentEvent componentevent)
    {
    }

    public void componentMoved(ComponentEvent e)
    {
        Point newPos = frame.getLocation();
        Point diff = new Point(newPos.x - location.x, newPos.y - location.y);
        location = newPos;
        if(core != null)
            core.moveAllToolWindows(diff.x, diff.y);
    }

    public void componentResized(ComponentEvent componentevent)
    {
    }

    public void componentShown(ComponentEvent componentevent)
    {
    }

    private JFrame frame;
    private Point location;
    private File specFile;
    private Core core;
    private ControlToolBar controlToolBar;
}
