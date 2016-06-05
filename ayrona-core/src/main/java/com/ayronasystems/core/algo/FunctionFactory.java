package com.ayronasystems.core.algo;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionFactory {

    private static Map<String, Function> functionMap = new HashMap<String, Function> ();

    public static void scanFunctions(){
        Reflections reflections = new Reflections ("com.ayronasystems.core.algo");
        Set<Class<?>> subTypes = reflections.getTypesAnnotatedWith (Fn.class);
        for ( Class<?> clazz : subTypes ){
            try {
                Function fn = (Function) clazz.getConstructor (null).newInstance ();
                functionMap.put (clazz.getAnnotation (Fn.class).name (), fn);
            } catch ( Exception e ) {
            }
        }
    }

    public static Function getInstance (String name){
        return functionMap.get (name);
    }
}
