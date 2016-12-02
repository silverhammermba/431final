// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IILObjectVisitor.java

package eis.iilang;


// Referenced classes of package eis.iilang:
//            Action, DataContainer, Function, Identifier, 
//            IILElement, Numeral, Parameter, ParameterList, 
//            Percept, TruthValue

public interface IILObjectVisitor
{

    public abstract Object visit(Action action, Object obj);

    public abstract Object visit(DataContainer datacontainer, Object obj);

    public abstract Object visit(Function function, Object obj);

    public abstract Object visit(Identifier identifier, Object obj);

    public abstract Object visit(IILElement iilelement, Object obj);

    public abstract Object visit(Numeral numeral, Object obj);

    public abstract Object visit(Parameter parameter, Object obj);

    public abstract Object visit(ParameterList parameterlist, Object obj);

    public abstract Object visit(Percept percept, Object obj);

    public abstract Object visit(TruthValue truthvalue, Object obj);
}
