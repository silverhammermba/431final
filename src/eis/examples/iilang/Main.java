// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main.java

package eis.examples.iilang;

import eis.iilang.*;
import java.io.PrintStream;

public class Main
{

    public Main()
    {
    }

    public static void main(String args[])
    {
        DataContainer dc = null;
        dc = new Action("moveTo", new Parameter[] {
            new Numeral(Integer.valueOf(2)), new Numeral(Integer.valueOf(3))
        });
        System.out.println((new StringBuilder(String.valueOf(dc.toProlog()))).append("\n").toString());
        System.out.println(dc.toXML());
        System.out.println("");
        dc = new Action("followPath", new Parameter[] {
            new ParameterList(new Parameter[] {
                new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(1)), new Numeral(Integer.valueOf(1))
                }), new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(2)), new Numeral(Integer.valueOf(1))
                }), new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(2)), new Numeral(Integer.valueOf(2))
                }), new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(3)), new Numeral(Integer.valueOf(2))
                }), new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(4)), new Numeral(Integer.valueOf(2))
                }), new Function("pos", new Parameter[] {
                    new Numeral(Integer.valueOf(4)), new Numeral(Integer.valueOf(3))
                })
            }), new Function("speed", new Parameter[] {
                new Numeral(Double.valueOf(10D))
            })
        });
        System.out.println((new StringBuilder(String.valueOf(dc.toProlog()))).append("\n").toString());
        System.out.println(dc.toXML());
        System.out.println("");
        dc = new Percept("sensors", new Parameter[] {
            new ParameterList(new Parameter[] {
                new Function("red", new Parameter[] {
                    new Identifier("ball")
                }), new Function("rubber", new Parameter[] {
                    new Identifier("ball")
                })
            })
        });
        System.out.println((new StringBuilder(String.valueOf(dc.toProlog()))).append("\n").toString());
        System.out.println(dc.toXML());
        System.out.println("");
        dc = new Percept("entities", new Parameter[] {
            new ParameterList(new Parameter[] {
                new Identifier("entity1"), new Identifier("entity2"), new Identifier("entity3")
            })
        });
        System.out.println((new StringBuilder(String.valueOf(dc.toProlog()))).append("\n").toString());
        System.out.println(dc.toXML());
        System.out.println("");
        dc = new Percept("entities", new Parameter[] {
            new ParameterList()
        });
        System.out.println((new StringBuilder(String.valueOf(dc.toProlog()))).append("\n").toString());
        System.out.println(dc.toXML());
        System.out.println("");
    }
}
