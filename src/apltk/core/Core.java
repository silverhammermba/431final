// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Core.java

package apltk.core;

import apltk.interpreter.*;
import apltk.interpreter.data.*;
import apltk.tool.*;
import eis.EILoader;
import eis.EnvironmentInterfaceStandard;
import eis.iilang.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.Element;

// Referenced classes of package apltk.core:
//            ParseException, CoreException, Scenario, PointInTime, 
//            StepResult, ScenarioParser, CoreState

public class Core extends Thread
{

    private static void debugPrintln(String str)
    {
        if(debug)
            System.out.println((new StringBuilder()).append("Core: ").append(str).toString());
    }

    public Core(File specFile)
        throws CoreException
    {
        super("APLDTKCore");
        basePath = "";
        scenarios = null;
        tools = null;
        interpreters = null;
        environments = null;
        state = null;
        timeSums = null;
        try
        {
            scenarios = ScenarioParser.parse(specFile);
        }
        catch(ParseException e)
        {
            throw new CoreException((new StringBuilder()).append(e.getMessage()).append(" (").append(e.getPath()).append(")").toString());
        }
        basePath = (new StringBuilder()).append(specFile.getAbsoluteFile().getParentFile().getAbsolutePath()).append(System.getProperty("file.separator")).toString();
        debugPrintln((new StringBuilder()).append("Base path is: ").append(basePath).toString());
    }

    public void run()
    {
        int scenarioCounter;
        Iterator i$;
        scenarioCounter = 0;
        i$ = scenarios.iterator();
_L2:
        Scenario scenario;
        if(!i$.hasNext())
            break; /* Loop/switch isn't completed */
        scenario = (Scenario)i$.next();
        scenarioCounter++;
        run(scenario);
        if(true) goto _L2; else goto _L1
        InterpreterException e;
        e;
        System.out.println((new StringBuilder()).append("Failed to execute scenario #").append(scenarioCounter).toString());
        System.out.println(e.getMessage());
          goto _L1
        e;
        System.out.println((new StringBuilder()).append("Failed to execute scenario #").append(scenarioCounter).toString());
        System.out.println(e.getMessage());
_L1:
    }

    private void run(Scenario scenario)
        throws ToolException, InterpreterException
    {
        debugPrintln("run scenario");
        tools = new LinkedList();
        for(int a = 0; a < scenario.toolFiles.size(); a++)
        {
            debugPrintln((new StringBuilder()).append("instantiate tool #").append(a).toString());
            Tool tool = null;
            try
            {
                tool = ToolLoader.fromJarFile((new StringBuilder()).append(basePath).append((String)scenario.toolFiles.get(a)).toString());
                tool.setBasePath(basePath);
            }
            catch(IOException e)
            {
                throw new ToolException(e.getMessage());
            }
            tool.setCore(this);
            tool.init((Element)scenario.toolParameters.get(a));
            tools.add(tool);
        }

        debugPrintln("tools instantiated");
        for(int run = 0; run < scenario.repeats; run++)
        {
            debugPrintln((new StringBuilder()).append("run #").append(run).toString());
            timeSums = new HashMap();
            interpreters = new LinkedList();
            for(int a = 0; a < scenario.interpreterFiles.size(); a++)
            {
                Interpreter interpreter = null;
                try
                {
                    interpreter = InterpreterLoader.fromJarFile((new StringBuilder()).append(basePath).append((String)scenario.interpreterFiles.get(a)).toString());
                    interpreter.setBasePath(basePath);
                }
                catch(IOException e)
                {
                    throw new InterpreterException(e.getMessage());
                }
                interpreter.init((Element)scenario.interpreterParameters.get(a));
                interpreters.add(interpreter);
            }

            environments = new LinkedList();
            for(int a = 0; a < scenario.environmentFiles.size(); a++)
            {
                EnvironmentInterfaceStandard environment = null;
                try
                {
                    environment = EILoader.fromJarFile(new File((new StringBuilder()).append(basePath).append((String)scenario.environmentFiles.get(a)).toString()));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                LinkedList params = new LinkedList();
                java.util.Map.Entry e;
                for(Iterator i$ = ((HashMap)scenario.environmentParameters.get(a)).entrySet().iterator(); i$.hasNext(); params.add(new Function((String)e.getKey(), new Parameter[] {
    new Identifier((String)e.getValue())
})))
                    e = (java.util.Map.Entry)i$.next();

                if(!$assertionsDisabled)
                    throw new AssertionError("fix");
                environments.add(environment);
            }

            Tool tool;
            for(Iterator i$ = tools.iterator(); i$.hasNext(); tool.addEnvironments(environments))
            {
                tool = (Tool)i$.next();
                tool.addInterpreters(interpreters);
            }

            if(!$assertionsDisabled && environments == null)
                throw new AssertionError();
            if(!scenario.start)
                state = CoreState.PAUSED;
            else
                state = CoreState.RUNNING;
            long step = 0L;
            do
            {
                if(state == CoreState.FINISHED)
                    break;
                if(state == CoreState.PAUSED)
                {
                    try
                    {
                        Thread.sleep(500L);
                    }
                    catch(InterruptedException e) { }
                } else
                {
                    if(!$assertionsDisabled && state != CoreState.RUNNING)
                        throw new AssertionError();
                    if(step % 100L == 0L)
                        debugPrintln((new StringBuilder()).append("step #").append(step).toString());
                    LinkedList stepResults = new LinkedList();
                    Iterator i$ = interpreters.iterator();
                    do
                    {
                        if(!i$.hasNext())
                            break;
                        Interpreter interpreter = (Interpreter)i$.next();
                        long interpreterTime = getTime(interpreter);
                        PointInTime timeStart = new PointInTime();
                        StepResult result = interpreter.step();
                        PointInTime timeEnd = new PointInTime();
                        long elapsedTime = PointInTime.getElapsedTime(timeStart, timeEnd);
                        updateTime(interpreter, elapsedTime);
                        result.step = step;
                        result.elapsedTime = elapsedTime;
                        result.interpreter = interpreter;
                        stepResults.add(result);
                        if(result.beliefUpdates != null)
                        {
                            for(Iterator i$ = result.beliefUpdates.values().iterator(); i$.hasNext();)
                            {
                                Collection bus = (Collection)i$.next();
                                Iterator i$ = bus.iterator();
                                while(i$.hasNext()) 
                                {
                                    BeliefUpdate bu = (BeliefUpdate)i$.next();
                                    bu.relativeTime = PointInTime.getElapsedTime(timeStart, bu.creationTime) + interpreterTime;
                                }
                            }

                        }
                        if(result.beliefQueries != null)
                        {
                            for(Iterator i$ = result.beliefQueries.values().iterator(); i$.hasNext();)
                            {
                                Collection bqs = (Collection)i$.next();
                                Iterator i$ = bqs.iterator();
                                while(i$.hasNext()) 
                                {
                                    BeliefQuery bq = (BeliefQuery)i$.next();
                                    bq.relativeTime = PointInTime.getElapsedTime(timeStart, bq.creationTime) + interpreterTime;
                                }
                            }

                        }
                        if(result.goalUpdates != null)
                        {
                            for(Iterator i$ = result.goalUpdates.values().iterator(); i$.hasNext();)
                            {
                                Collection gus = (Collection)i$.next();
                                Iterator i$ = gus.iterator();
                                while(i$.hasNext()) 
                                {
                                    GoalUpdate gu = (GoalUpdate)i$.next();
                                    gu.relativeTime = PointInTime.getElapsedTime(timeStart, gu.creationTime) + interpreterTime;
                                }
                            }

                        }
                        if(result.goalQueries != null)
                        {
                            Iterator i$ = result.goalQueries.values().iterator();
                            while(i$.hasNext()) 
                            {
                                Collection gqs = (Collection)i$.next();
                                Iterator i$ = gqs.iterator();
                                while(i$.hasNext()) 
                                {
                                    GoalQuery gq = (GoalQuery)i$.next();
                                    gq.relativeTime = PointInTime.getElapsedTime(timeStart, gq.creationTime) + interpreterTime;
                                }
                            }
                        }
                    } while(true);
                    Tool tool;
                    long timeStart;
                    long timeEnd;
                    for(i$ = tools.iterator(); i$.hasNext(); updateTime(tool, timeEnd - timeStart))
                    {
                        tool = (Tool)i$.next();
                        timeStart = System.nanoTime();
                        tool.processStepResults(stepResults);
                        timeEnd = System.nanoTime();
                    }

                    step++;
                    if(scenario.steps > 0 && step == (long)scenario.steps)
                        finishCycle();
                }
            } while(true);
            Interpreter interpreter;
            for(Iterator i$ = interpreters.iterator(); i$.hasNext(); interpreter.release())
                interpreter = (Interpreter)i$.next();

        }

        Tool tool;
        for(Iterator i$ = tools.iterator(); i$.hasNext(); tool.release())
            tool = (Tool)i$.next();

    }

    private void updateTime(Object obj, long time)
    {
        if(!$assertionsDisabled && timeSums == null)
            throw new AssertionError();
        Long t = (Long)timeSums.get(obj);
        if(t == null)
            t = new Long(0L);
        t = Long.valueOf(t.longValue() + time);
        timeSums.put(obj, t);
    }

    public long getTime(Object obj)
    {
        Long t = (Long)timeSums.get(obj);
        if(t == null)
            return -1L;
        else
            return t.longValue();
    }

    public String getTimeString(Object obj)
    {
        long t = getTime(obj);
        String ret = "";
        if(t != -1L)
        {
            Double val = new Double(t);
            String unit = "ns";
            if(val.doubleValue() > 1000D)
            {
                val = Double.valueOf(val.doubleValue() / 1000D);
                unit = "\u03BCs";
                if(val.doubleValue() > 1000D)
                {
                    val = Double.valueOf(val.doubleValue() / 1000D);
                    unit = "ms";
                    if(val.doubleValue() > 1000D)
                    {
                        val = Double.valueOf(val.doubleValue() / 1000D);
                        unit = "s";
                        if(val.doubleValue() > 60D)
                        {
                            val = Double.valueOf(val.doubleValue() / 60D);
                            unit = "min";
                            if(val.doubleValue() > 60D)
                            {
                                val = Double.valueOf(val.doubleValue() / 1000D);
                                unit = "h";
                            }
                        }
                    }
                }
            }
            String strVal = val.toString();
            int index = strVal.indexOf(".") + 3;
            if(index >= strVal.length())
                index = strVal.length() - 1;
            if(index != -1)
                strVal = strVal.substring(0, index);
            ret = (new StringBuilder()).append(strVal).append(unit).toString();
        }
        return ret;
    }

    public void pauseCycle()
    {
        if(!$assertionsDisabled)
            throw new AssertionError("fix");
        else
            return;
    }

    public void startCycle()
    {
        if(!$assertionsDisabled)
            throw new AssertionError("fix");
        else
            return;
    }

    public void finishCycle()
    {
        state = CoreState.FINISHED;
        debugPrintln("finished");
        Tool tool;
        for(Iterator i$ = tools.iterator(); i$.hasNext(); tool.handleFinished())
            tool = (Tool)i$.next();

    }

    public void sendToolSignal(Tool sender, String signal)
    {
        Tool t;
        for(Iterator i$ = tools.iterator(); i$.hasNext(); t.handleSignal(sender.getName(), signal))
            t = (Tool)i$.next();

    }

    public void moveAllToolWindows(int x, int y)
    {
        ToolWindow window;
        for(Iterator i$ = tools.iterator(); i$.hasNext(); window.moveLocation(x, y))
        {
            Tool t = (Tool)i$.next();
            window = t.getWindow();
        }

    }

    public String timeStatistics()
    {
        if(timeSums == null || timeSums.isEmpty())
            return "";
        long sum = 0L;
        for(Iterator i$ = timeSums.values().iterator(); i$.hasNext();)
        {
            Long l = (Long)i$.next();
            sum += l.longValue();
        }

        String ret = "Time statistics\n";
        Iterator i$ = timeSums.keySet().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Object e = i$.next();
            double percent = (100D * (double)getTime(e)) / (double)sum;
            if(e instanceof Tool)
            {
                ret = (new StringBuilder()).append(ret).append("  ").append(((Tool)e).getName()).append("\n").toString();
                ret = (new StringBuilder()).append(ret).append("    Time: ").append(getTimeString(e)).append(" ").append(percent).append("%").append("\n").toString();
            }
            if(e instanceof Interpreter)
            {
                ret = (new StringBuilder()).append(ret).append("  ").append(((Interpreter)e).getName()).append("\n").toString();
                ret = (new StringBuilder()).append(ret).append("    Time: ").append(getTimeString(e)).append(" ").append(percent).append("%").append("\n").toString();
            }
        } while(true);
        return ret;
    }

    private String basePath;
    private Collection scenarios;
    private Collection tools;
    private Collection interpreters;
    private Collection environments;
    private CoreState state;
    private Map timeSums;
    private static boolean debug = true;
    static final boolean $assertionsDisabled = !apltk/core/Core.desiredAssertionStatus();

}
