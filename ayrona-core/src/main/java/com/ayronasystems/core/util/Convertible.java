package com.ayronasystems.core.util;

/**
 * Created by gorkemgok on 17/09/16.
 */
public interface Convertible<T> {

    T to();

    void from(T t);
}
