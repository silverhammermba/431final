// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IILVisitor.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            Action, DataContainer, Function, Identifier, 
//            IILElement, Numeral, Parameter, ParameterList, 
//            Percept, TruthValue

public interface IILVisitor
{

    public abstract Object visit(Action action);

    public abstract Object visit(DataContainer datacontainer);

    public abstract Object visit(Function function);

    public abstract Object visit(Identifier identifier);

    public abstract Object visit(IILElement iilelement);

    public abstract Object visit(Numeral numeral);

    public abstract Object visit(Parameter parameter);

    public abstract Object visit(ParameterList parameterlist);

    public abstract Object visit(Percept percept);

    public abstract Object visit(TruthValue truthvalue);
}
