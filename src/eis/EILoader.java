// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EILoader.java

package eis;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;
import java.util.jar.*;

// Referenced classes of package eis:
//            EnvironmentInterfaceStandard

public class EILoader
{

    public EILoader()
    {
    }

    public static EnvironmentInterfaceStandard fromJarFile(File file)
        throws IOException
    {
        if(!file.exists())
            throw new IOException((new StringBuilder("\"")).append(file.getAbsolutePath()).append("\" does not exist.").toString());
        if(!file.getName().endsWith(".jar"))
            throw new IOException((new StringBuilder("\"")).append(file.getAbsolutePath()).append("\" is not a jar-file.").toString());
        JarFile jarFile = new JarFile(file);
        Manifest manifest = jarFile.getManifest();
        String mainClass = manifest.getMainAttributes().getValue("Main-Class");
        if(mainClass == null || mainClass.equals(""))
            throw new IOException((new StringBuilder()).append(file).append("does not specify a main-class").toString());
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
            throw new IOException("Error, could not add URL to system classloader", t);
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
            throw new IOException((new StringBuilder("Class \"")).append(mainClass).append("\" could not be loaded from \"").append(file).append("\"").toString(), e);
        }
        Constructor c = null;
        EnvironmentInterfaceStandard ei = null;
        try
        {
            c = envInterfaceClass.getConstructor(new Class[0]);
            ei = (EnvironmentInterfaceStandard)c.newInstance(new Object[0]);
        }
        catch(Exception e)
        {
            System.out.println(e);
            throw new IOException((new StringBuilder("Class \"")).append(mainClass).append("\" could not be loaded from \"").append(file).append("\"").toString(), e);
        }
        if(!version.equals(ei.requiredVersion()))
            throw new IOException((new StringBuilder("Loaded environment interface version does not match the required one \"")).append(version).append("\" vs. \"").append(ei.requiredVersion()).append("\"").toString());
        else
            return ei;
    }

    public static EnvironmentInterfaceStandard fromClassName(String className)
        throws IOException
    {
        ClassLoader loader = eis/EnvironmentInterfaceStandard.getClassLoader();
        Class envInterfaceClass = null;
        try
        {
            envInterfaceClass = loader.loadClass(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new IOException((new StringBuilder("Class \"")).append(className).append("\" could not be loaded").toString(), e);
        }
        Constructor c = null;
        EnvironmentInterfaceStandard ei = null;
        try
        {
            c = envInterfaceClass.getConstructor(new Class[0]);
            ei = (EnvironmentInterfaceStandard)c.newInstance(new Object[0]);
        }
        catch(Exception e)
        {
            System.out.println(e);
            throw new IOException((new StringBuilder("Class \"")).append(className).append("\" could not be loaded").toString(), e);
        }
        if(!version.equals(ei.requiredVersion()))
            throw new IOException((new StringBuilder("Loaded environment interface version does not match the required one \"")).append(version).append("\" vs. \"").append(ei.requiredVersion()).append("\"").toString());
        else
            return ei;
    }

    public static void main(String args[])
        throws IOException
    {
        EnvironmentInterfaceStandard environmentinterfacestandard;
        if(args.length == 0)
            System.out.println("You have to provide a filename.");
        else
            environmentinterfacestandard = fromJarFile(new File(args[0]));
    }

    private static String version = "0.3";

}
