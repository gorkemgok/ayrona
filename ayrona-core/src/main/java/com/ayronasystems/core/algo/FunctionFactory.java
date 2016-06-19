package com.ayronasystems.core.algo;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionFactory {

    private static Logger log = LoggerFactory.getLogger (FunctionFactory.class);

    private static Map<String, Function> functionMap = new HashMap<String, Function> ();

    public static void scanFunctions(){
        Reflections reflections = new Reflections ("com.ayronasystems.core.algo");
        Set<Class<?>> subTypes = reflections.getTypesAnnotatedWith (Fn.class);
        for ( Class<?> clazz : subTypes ){
            String name = clazz.getAnnotation (Fn.class).name ();
            try {
                Function fn = (Function) clazz.getConstructor (null).newInstance ();
                functionMap.put (clazz.getAnnotation (Fn.class).name (), fn);
                log.info ("Created function {}", name);
            } catch ( Exception e ) {
                log.error ("Cant create function "+name, e);
            }
        }
    }

    public static Function getInstance (String name){
        return functionMap.get (name);
    }
}
