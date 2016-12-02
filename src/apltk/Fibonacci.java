// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Fibonacci.java

package apltk;

import java.io.PrintStream;

public class Fibonacci
{

    public Fibonacci()
    {
    }

    private void calculate(int max)
    {
        long start = System.nanoTime();
        long fib[] = new long[max];
        fib[0] = fib[1] = 1L;
        for(int a = 2; a < max; a++)
            fib[a] = fib[a - 1] + fib[a - 2];

        System.out.println(fib[max - 1]);
        long time = System.nanoTime() - start;
        System.out.println((new StringBuilder()).append((double)time / 1000000D).append("ms").toString());
    }

    public static void main(String args[])
    {
        (new Fibonacci()).calculate(1000);
    }
}
