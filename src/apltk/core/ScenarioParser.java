// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioParser.java

package apltk.core;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

// Referenced classes of package apltk.core:
//            ParseException, Scenario

public class ScenarioParser
{

    private static void debugPrintln(Object obj)
    {
        if(debug)
            System.out.println((new StringBuilder()).append("ScenarioParser: ").append(obj).toString());
    }

    private ScenarioParser()
    {
    }

    public static LinkedList parse(File specFile)
        throws ParseException
    {
        LinkedList ret = new LinkedList();
        if(!specFile.exists())
            throw new ParseException(specFile.getAbsolutePath(), "file does not exist");
        Document doc = null;
        try
        {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            doc = documentbuilderfactory.newDocumentBuilder().parse(specFile);
        }
        catch(SAXException e)
        {
            throw new ParseException(specFile.getPath(), "error parsing");
        }
        catch(IOException e)
        {
            throw new ParseException(specFile.getPath(), "error parsing");
        }
        catch(ParserConfigurationException e)
        {
            throw new ParseException(specFile.getPath(), "error parsing");
        }
        Element root = doc.getDocumentElement();
        if(!root.getNodeName().equals("dtk"))
            throw new ParseException(specFile.getPath(), "root-element must be dtk");
        NodeList rootChildren = root.getChildNodes();
        for(int a = 0; a < rootChildren.getLength(); a++)
        {
            Node rootChild = rootChildren.item(a);
            if(rootChild.getNodeName().equals("#text") || rootChild.getNodeName().equals("#comment"))
                continue;
            if(!rootChild.getNodeName().equals("scenario"))
                throw new ParseException(specFile.getPath(), " expected <scenario>-tag");
            Scenario scenario = new Scenario();
            String val = ((Element)rootChild).getAttribute("start");
            if(val.equals("true") || val.equals("yes"))
                scenario.start = true;
            else
            if(val.equals("") || val.equals("false") || val.equals("no"))
                scenario.start = false;
            else
                throw new ParseException(specFile.getPath(), (new StringBuilder()).append(" ").append(val).append(" is an invalid value for start-attribue").toString());
            val = ((Element)rootChild).getAttribute("steps");
            if(!val.isEmpty())
                try
                {
                    Integer steps = new Integer(val);
                    scenario.steps = steps.intValue();
                }
                catch(NumberFormatException e)
                {
                    throw new ParseException(specFile.getPath(), (new StringBuilder()).append(" ").append(val).append(" is an invalid value for steps-attribue").toString());
                }
            NodeList rootChildChildren = rootChild.getChildNodes();
            for(int b = 0; b < rootChildChildren.getLength(); b++)
            {
                Node rootChildChild = rootChildChildren.item(b);
                if(rootChildChild.getNodeName().equals("#text"))
                    continue;
                if(rootChildChild.getNodeName().equals("interpreter"))
                {
                    debugPrintln("interpreter");
                    String file = rootChildChild.getAttributes().getNamedItem("file").getNodeValue();
                    scenario.interpreterFiles.add(file);
                    NodeList rootChildChildChildren = rootChildChild.getChildNodes();
                    Element parameters = null;
                    for(int c = 0; c < rootChildChildChildren.getLength(); c++)
                    {
                        Node rootChildChildChild = rootChildChildChildren.item(c);
                        String nodeName = rootChildChildChild.getNodeName();
                        if(nodeName.equals("#text"))
                            continue;
                        if(nodeName.equals("parameters"))
                        {
                            parameters = (Element)rootChildChildChild;
                            debugPrintln(parameters);
                        } else
                        {
                            throw new ParseException(specFile.getPath(), (new StringBuilder()).append(" expected <parameter>-tag but found <").append(nodeName).append(">").toString());
                        }
                    }

                    scenario.interpreterParameters.add(parameters);
                    continue;
                }
                if(rootChildChild.getNodeName().equals("environment"))
                {
                    debugPrintln("environment");
                    String file = rootChildChild.getAttributes().getNamedItem("file").getNodeValue();
                    scenario.environmentFiles.add(file);
                    NodeList rootChildChildChildren = rootChildChild.getChildNodes();
                    HashMap parameters = new HashMap();
                    for(int c = 0; c < rootChildChildChildren.getLength(); c++)
                    {
                        Node rootChildChildChild = rootChildChildChildren.item(c);
                        String nodeName = rootChildChildChild.getNodeName();
                        if(nodeName.equals("#text"))
                            continue;
                        if(nodeName.equals("parameters"))
                        {
                            parameters = parseParameters(rootChildChildChild);
                            debugPrintln(parameters);
                        } else
                        {
                            throw new ParseException(specFile.getPath(), (new StringBuilder()).append(" expected <parameter>-tag but found <").append(nodeName).append(">").toString());
                        }
                    }

                    scenario.environmentParameters.add(parameters);
                    continue;
                }
                if(rootChildChild.getNodeName().equals("tool"))
                {
                    debugPrintln("tool");
                    String file = rootChildChild.getAttributes().getNamedItem("file").getNodeValue();
                    scenario.toolFiles.add(file);
                    NodeList rootChildChildChildren = rootChildChild.getChildNodes();
                    Element parameters = null;
                    for(int c = 0; c < rootChildChildChildren.getLength(); c++)
                    {
                        Node rootChildChildChild = rootChildChildChildren.item(c);
                        String nodeName = rootChildChildChild.getNodeName();
                        if(nodeName.equals("#text"))
                            continue;
                        if(nodeName.equals("parameters"))
                        {
                            parameters = (Element)rootChildChildChild;
                            debugPrintln(parameters);
                        } else
                        {
                            throw new ParseException(specFile.getPath(), (new StringBuilder()).append(" expected <parameter>-tag but found <").append(nodeName).append(">").toString());
                        }
                    }

                    scenario.toolParameters.add(parameters);
                    continue;
                }
                if(!rootChildChild.getNodeName().equals("#comment"))
                    throw new ParseException(specFile.getPath(), (new StringBuilder()).append("unexpected <").append(rootChildChild.getNodeName()).append(">tag").toString());
            }

            ret.add(scenario);
        }

        return ret;
    }

    private static HashMap parseParameters(Node node)
    {
        HashMap ret = new HashMap();
        for(int a = 0; a < node.getAttributes().getLength(); a++)
        {
            Node attr = node.getAttributes().item(a);
            String key = attr.getNodeName();
            String value = attr.getNodeValue();
            ret.put(key, value);
        }

        return ret;
    }

    private static boolean debug = true;

}
