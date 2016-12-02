// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogicParser.java

package apltk.interpreter.data;

import java.util.*;

// Referenced classes of package apltk.interpreter.data:
//            DataException

public class LogicParser
{

    public LogicParser()
    {
    }

    public static Map parseAtom(String value)
        throws DataException
    {
        String predicate = null;
        Collection parameters = null;
        if(!value.endsWith("."))
            throw new DataException((new StringBuilder()).append("Expected \"").append(value).append("\" to end with a dot").toString());
        int dotIndex = value.indexOf(".");
        int parenStartIndex = value.indexOf("(");
        int parenEndIndex = value.indexOf(")");
        if(parenStartIndex != -1)
        {
            predicate = value.substring(0, parenStartIndex);
            if(parenEndIndex == -1)
                throw new DataException((new StringBuilder()).append("Expected \"").append(value).append("\" to have a closing parenthesis").toString());
            if(parenEndIndex == parenStartIndex + 1)
                throw new DataException((new StringBuilder()).append("Expected \"").append(value).append("\" to have parameters between parentheses").toString());
            String params = value.substring(parenStartIndex + 1, parenEndIndex);
            String pArray[] = params.split(",");
            parameters = new Vector();
            parameters.addAll(Arrays.asList(pArray));
        } else
        {
            if(parenEndIndex != -1)
                throw new DataException((new StringBuilder()).append("Expected \"").append(value).append("\" to have no closing parenthesis").toString());
            predicate = value.substring(0, dotIndex);
        }
        Map ret = new HashMap();
        ret.put(predicate, parameters);
        return ret;
    }
}
