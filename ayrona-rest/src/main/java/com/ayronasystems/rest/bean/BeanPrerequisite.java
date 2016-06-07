package com.ayronasystems.rest.bean;

/**
 * Created by gorkemgok on 06/06/16.
 */
public interface BeanPrerequisite<T> {

    PrerequisiteBean check(T t);
}
