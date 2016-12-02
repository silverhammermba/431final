// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterpreterLoader.java

package apltk.interpreter;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.util.jar.*;

// Referenced classes of package apltk.interpreter:
//            Interpreter

public class InterpreterLoader
{

    private InterpreterLoader()
    {
    }

    public static Interpreter fromJarFile(String jarFileName)
        throws IOException
    {
        File file = new File(jarFileName);
        if(!file.exists())
            throw new IOException((new StringBuilder()).append("\"").append(file.getAbsolutePath()).append("\" does not exist.").toString());
        if(!file.getName().endsWith(".jar"))
            throw new IOException((new StringBuilder()).append("\"").append(file.getAbsolutePath()).append("\" is not a jar-file.").toString());
        JarFile jarFile = new JarFile(file);
        Manifest manifest = jarFile.getManifest();
        String mainClass = manifest.getMainAttributes().getValue("Main-Class");
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class sysclass = java/net/URLClassLoader;
        URL url = file.toURI().toURL();
        try
        {
            Method method = sysclass.getDeclaredMethod("addURL", new Class[] {
                java/net/URL
            });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] {
                url
            });
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
        URLClassLoader loader = new URLClassLoader(new URL[] {
            url
        });
        Class envInterfaceClass = null;
        try
        {
            envInterfaceClass = loader.loadClass(mainClass);
        }
        catch(ClassNotFoundException e)
        {
            throw new IOException((new StringBuilder()).append("Class \"").append(mainClass).append("\" could not be loaded from \"").append(file).append("\"").toString());
        }
        Constructor c = null;
        Interpreter interpreter = null;
        try
        {
            c = envInterfaceClass.getConstructor(new Class[0]);
            interpreter = (Interpreter)(Interpreter)c.newInstance(new Object[0]);
        }
        catch(Exception e)
        {
            System.out.println(e);
            throw new IOException((new StringBuilder()).append("Class \"").append(mainClass).append("\" could not be loaded from \"").append(file).append("\"").toString());
        }
        return interpreter;
    }
}
