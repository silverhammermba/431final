// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translator.java

package eis.eis2java.translation;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eis.iilang.*;
import java.util.*;

// Referenced classes of package eis.eis2java.translation:
//            Java2Parameter, Parameter2Java

public class Translator
{
    private static class BooleanTranslator
        implements Java2Parameter, Parameter2Java
    {

        public Parameter[] translate(Boolean b)
            throws TranslationException
        {
            return (new Parameter[] {
                new TruthValue(b.booleanValue())
            });
        }

        public Class translatesFrom()
        {
            return java/lang/Boolean;
        }

        public Boolean translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof TruthValue))
                throw new TranslationException((new StringBuilder("Expected a Truthvalue parameter but got ")).append(parameter).toString());
            else
                return Boolean.valueOf(((TruthValue)parameter).getBooleanValue());
        }

        public Class translatesTo()
        {
            return java/lang/Boolean;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        public volatile Parameter[] translate(Object obj)
            throws TranslationException
        {
            return translate((Boolean)obj);
        }

        private BooleanTranslator()
        {
        }

        BooleanTranslator(BooleanTranslator booleantranslator)
        {
            this();
        }
    }

    private static class CharTranslator
        implements Java2Parameter, Parameter2Java
    {

        public Character translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Identifier))
                throw new TranslationException((new StringBuilder("Expected an Identifier parameter but got ")).append(parameter).toString());
            Identifier id = (Identifier)parameter;
            String value = id.getValue();
            if(value.length() > 1)
                throw new TranslationException((new StringBuilder("A single character was expected instead a string of length ")).append(value.length()).append(" was given. Contents: ").append(value).toString());
            else
                return Character.valueOf(value.charAt(0));
        }

        public Class translatesTo()
        {
            return java/lang/Character;
        }

        public Parameter[] translate(Character value)
            throws TranslationException
        {
            return (new Parameter[] {
                new Identifier(String.valueOf(value))
            });
        }

        public Class translatesFrom()
        {
            return java/lang/Character;
        }

        public volatile Parameter[] translate(Object obj)
            throws TranslationException
        {
            return translate((Character)obj);
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private CharTranslator()
        {
        }

        CharTranslator(CharTranslator chartranslator)
        {
            this();
        }
    }

    private static class CollectionTranslator
        implements Java2Parameter
    {

        public Parameter[] translate(AbstractCollection value)
            throws TranslationException
        {
            Parameter parameters[] = new Parameter[value.size()];
            int i = 0;
            Iterator it = value.iterator();
            Translator translator = Translator.getInstance();
            while(it.hasNext()) 
            {
                Parameter translation[] = translator.translate2Parameter(it.next());
                if(translation.length == 1)
                    parameters[i] = translation[0];
                else
                    parameters[i] = new ParameterList(translation);
                i++;
            }
            return (new Parameter[] {
                new ParameterList(parameters)
            });
        }

        public Class translatesFrom()
        {
            Class cls = java/util/AbstractCollection;
            return cls;
        }

        public volatile Parameter[] translate(Object obj)
            throws TranslationException
        {
            return translate((AbstractCollection)obj);
        }

        private CollectionTranslator()
        {
        }

        CollectionTranslator(CollectionTranslator collectiontranslator)
        {
            this();
        }
    }

    private static class DoubleTranslator
        implements Parameter2Java
    {

        public Double translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return Double.valueOf(((Numeral)parameter).getValue().doubleValue());
        }

        public Class translatesTo()
        {
            return java/lang/Double;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private DoubleTranslator()
        {
        }

        DoubleTranslator(DoubleTranslator doubletranslator)
        {
            this();
        }
    }

    private static class FloatTranslator
        implements Parameter2Java
    {

        public Float translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return Float.valueOf(((Numeral)parameter).getValue().floatValue());
        }

        public Class translatesTo()
        {
            return java/lang/Float;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private FloatTranslator()
        {
        }

        FloatTranslator(FloatTranslator floattranslator)
        {
            this();
        }
    }

    private static class IntegerTranslator
        implements Parameter2Java
    {

        public Integer translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return Integer.valueOf(((Numeral)parameter).getValue().intValue());
        }

        public Class translatesTo()
        {
            return java/lang/Integer;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private IntegerTranslator()
        {
        }

        IntegerTranslator(IntegerTranslator integertranslator)
        {
            this();
        }
    }

    private static class LongTranslator
        implements Parameter2Java
    {

        public Long translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return Long.valueOf(((Numeral)parameter).getValue().longValue());
        }

        public Class translatesTo()
        {
            return java/lang/Long;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private LongTranslator()
        {
        }

        LongTranslator(LongTranslator longtranslator)
        {
            this();
        }
    }

    private static class NumberTranslator
        implements Java2Parameter, Parameter2Java
    {

        public Parameter[] translate(Number n)
            throws TranslationException
        {
            return (new Parameter[] {
                new Numeral(n)
            });
        }

        public Class translatesFrom()
        {
            return java/lang/Number;
        }

        public Number translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return ((Numeral)parameter).getValue();
        }

        public Class translatesTo()
        {
            return java/lang/Number;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        public volatile Parameter[] translate(Object obj)
            throws TranslationException
        {
            return translate((Number)obj);
        }

        private NumberTranslator()
        {
        }

        NumberTranslator(NumberTranslator numbertranslator)
        {
            this();
        }
    }

    private static class ShortTranslator
        implements Parameter2Java
    {

        public Short translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Numeral))
                throw new TranslationException((new StringBuilder("Expected a numeral parameter but got ")).append(parameter).toString());
            else
                return Short.valueOf(((Numeral)parameter).getValue().shortValue());
        }

        public Class translatesTo()
        {
            return java/lang/Short;
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private ShortTranslator()
        {
        }

        ShortTranslator(ShortTranslator shorttranslator)
        {
            this();
        }
    }

    private static class StringTranslator
        implements Java2Parameter, Parameter2Java
    {

        public String translate(Parameter parameter)
            throws TranslationException
        {
            if(!(parameter instanceof Identifier))
            {
                throw new TranslationException((new StringBuilder("Expected an Identifier parameter but got ")).append(parameter).toString());
            } else
            {
                Identifier id = (Identifier)parameter;
                return id.getValue();
            }
        }

        public Class translatesTo()
        {
            return java/lang/String;
        }

        public Parameter[] translate(String value)
            throws TranslationException
        {
            return (new Parameter[] {
                new Identifier(value)
            });
        }

        public Class translatesFrom()
        {
            return java/lang/String;
        }

        public volatile Parameter[] translate(Object obj)
            throws TranslationException
        {
            return translate((String)obj);
        }

        public volatile Object translate(Parameter parameter)
            throws TranslationException
        {
            return translate(parameter);
        }

        private StringTranslator()
        {
        }

        StringTranslator(StringTranslator stringtranslator)
        {
            this();
        }
    }


    public static Translator getInstance()
    {
        if(singleton == null)
            singleton = new Translator();
        return singleton;
    }

    private Translator()
    {
        NumberTranslator numberTranslator = new NumberTranslator(null);
        registerJava2ParameterTranslator(numberTranslator);
        registerParameter2JavaTranslator(numberTranslator);
        BooleanTranslator booleanTranslator = new BooleanTranslator(null);
        registerJava2ParameterTranslator(booleanTranslator);
        registerParameter2JavaTranslator(booleanTranslator);
        CharTranslator charTranslator = new CharTranslator(null);
        registerJava2ParameterTranslator(charTranslator);
        registerParameter2JavaTranslator(charTranslator);
        StringTranslator stringTranslator = new StringTranslator(null);
        registerJava2ParameterTranslator(stringTranslator);
        registerParameter2JavaTranslator(stringTranslator);
        CollectionTranslator collectionTranslator = new CollectionTranslator(null);
        registerJava2ParameterTranslator(collectionTranslator);
        registerParameter2JavaTranslator(new IntegerTranslator(null));
        registerParameter2JavaTranslator(new LongTranslator(null));
        registerParameter2JavaTranslator(new ShortTranslator(null));
        registerParameter2JavaTranslator(new DoubleTranslator(null));
        registerParameter2JavaTranslator(new FloatTranslator(null));
    }

    public void registerJava2ParameterTranslator(Java2Parameter translator)
    {
        java2ParameterTranslators.put(translator.translatesFrom(), translator);
    }

    public void registerParameter2JavaTranslator(Parameter2Java translator)
    {
        parameter2JavaTranslators.put(translator.translatesTo(), translator);
    }

    public Parameter[] translate2Parameter(Object o)
        throws TranslationException
    {
        Java2Parameter rawTranslator = null;
        Class clazz = o.getClass();
        if(clazz.isPrimitive())
            clazz = getWrapper(clazz);
        for(; clazz != null && rawTranslator == null; clazz = clazz.getSuperclass())
            rawTranslator = (Java2Parameter)java2ParameterTranslators.get(clazz);

        if(rawTranslator == null)
        {
            throw new NoTranslatorException(o.getClass());
        } else
        {
            Java2Parameter translator = rawTranslator;
            return translator.translate(o);
        }
    }

    public Object translate2Java(Parameter parameter, Class parameterClass)
        throws TranslationException, NoTranslatorException
    {
        Class clazz = parameterClass;
        if(clazz.isPrimitive())
            clazz = getWrapper(clazz);
        Parameter2Java rawTranslator = (Parameter2Java)parameter2JavaTranslators.get(clazz);
        if(rawTranslator == null)
        {
            throw new NoTranslatorException(clazz);
        } else
        {
            Parameter2Java translator = rawTranslator;
            return translator.translate(parameter);
        }
    }

    private Class getWrapper(Class clazz)
    {
        if(Integer.TYPE.equals(clazz))
            return java/lang/Integer;
        if(Boolean.TYPE.equals(clazz))
            return java/lang/Boolean;
        if(Long.TYPE.equals(clazz))
            return java/lang/Long;
        if(Float.TYPE.equals(clazz))
            return java/lang/Float;
        if(Character.TYPE.equals(clazz))
            return java/lang/Character;
        if(Double.TYPE.equals(clazz))
            return java/lang/Double;
        if(Byte.TYPE.equals(clazz))
            return java/lang/Byte;
        if(Short.TYPE.equals(clazz))
            return java/lang/Short;
        if(Void.TYPE.equals(clazz))
            return java/lang/Void;
        else
            return null;
    }

    private static Translator singleton;
    private final HashMap java2ParameterTranslators = new HashMap();
    private final HashMap parameter2JavaTranslators = new HashMap();
}
