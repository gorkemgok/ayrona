package com.ayronasystems.core.algo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gorkemgok on 17/03/16.
 */
@Target (ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fn {

    String name ();

    int inputCount () default 1;

    int paramCount () default 1;

    int depth () default 1;

    int begOffset () default 1;

    boolean sameSizeInput() default true;

}
