// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Filter.java

package eis.eis2java.translation;


public class Filter
{
    public static final class Type extends Enum
    {

        public static Type[] values()
        {
            Type atype[];
            int i;
            Type atype1[];
            System.arraycopy(atype = ENUM$VALUES, 0, atype1 = new Type[i = atype.length], 0, i);
            return atype1;
        }

        public static Type valueOf(String s)
        {
            return (Type)Enum.valueOf(eis/eis2java/translation/Filter$Type, s);
        }

        public static final Type ALWAYS;
        public static final Type ONCE;
        public static final Type ON_CHANGE;
        public static final Type ON_CHANGE_NEG;
        private static final Type ENUM$VALUES[];

        static 
        {
            ALWAYS = new Type("ALWAYS", 0);
            ONCE = new Type("ONCE", 1);
            ON_CHANGE = new Type("ON_CHANGE", 2);
            ON_CHANGE_NEG = new Type("ON_CHANGE_NEG", 3);
            ENUM$VALUES = (new Type[] {
                ALWAYS, ONCE, ON_CHANGE, ON_CHANGE_NEG
            });
        }

        private Type(String s, int i)
        {
            super(s, i);
        }
    }


    public Filter()
    {
    }
}
