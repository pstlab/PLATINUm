package it.uniroma3.epsl2.framework.utils.compat;

import java.lang.annotation.Annotation;

public class AnnotationUtils
{
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getDeclaredAnnotation(Class<?> clazz,
            Class<T> annotationClass)
    {
        Annotation[] all = clazz.getDeclaredAnnotations();
        for (Annotation a : all)
        {
            if (a.annotationType()==annotationClass)
                return (T)a;
        }
        return null;
    }

}
