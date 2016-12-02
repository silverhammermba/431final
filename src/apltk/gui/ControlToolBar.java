// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ControlToolBar.java

package apltk.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.*;

public class ControlToolBar extends JToolBar
{

    private JButton makeImageButton(String iconFile, String toolTip)
    {
        JButton button = null;
        URL loc = getClass().getResource(iconFile);
        try
        {
            ImageIcon icon = new ImageIcon(loc);
            button = new JButton(icon);
        }
        catch(NullPointerException e)
        {
            System.out.println((new StringBuilder()).append("Could not load icon ").append(iconFile).append(" (").append(loc).append(")").toString());
            button = new JButton("X");
        }
        button.setToolTipText(toolTip);
        button.setSize(new Dimension(24, 24));
        button.setBorderPainted(false);
        return button;
    }

    public ControlToolBar(ActionListener al)
    {
        startButton = null;
        pauseButton = null;
        JButton button = null;
        button = makeImageButton("start.png", "start the thing");
        button.setActionCommand("start");
        button.setEnabled(false);
        button.addActionListener(al);
        startButton = button;
        add(button);
        button = makeImageButton("pause.png", "pause the thing");
        button.setActionCommand("pause");
        button.setEnabled(false);
        button.addActionListener(al);
        pauseButton = button;
        add(button);
        setFloatable(false);
    }

    public void setButtonsEnabled(boolean bool)
    {
        startButton.setEnabled(bool);
        pauseButton.setEnabled(bool);
    }

    private JButton startButton;
    private JButton pauseButton;
}
