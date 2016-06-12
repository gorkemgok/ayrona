package com.ayronasystems.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gorkemgok on 23/01/16.
 */
@XmlRootElement
public class ErrorBean {

    public static final int ERR_UNAUTHORIZED = 403;

    public static final int STRATEGY_COMPILATION_ERROR = 4435;

    private int code;

    private String message;

    public ErrorBean (int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorBean () {
    }

    public int getCode () {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
