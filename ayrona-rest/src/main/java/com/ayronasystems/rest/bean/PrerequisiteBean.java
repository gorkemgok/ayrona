package com.ayronasystems.rest.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class PrerequisiteBean {

    public enum ShouldBe{
        NOT_EMPTY,
        ALPHANUMERIC,
        NUMBER,
        DATE,
        EMAIL
    }

    public static final PrerequisiteBean OK = new PrerequisiteBean (true);

    private boolean isOk;

    private Map<String, ShouldBe> shouldBe = new HashMap<String, ShouldBe> ();

    public PrerequisiteBean (boolean isOk) {
        this.isOk = isOk;
    }

    public PrerequisiteBean () {
        isOk = true;
    }

    public void shouldBe(String field, ShouldBe shouldBe){
        isOk = false;
        this.shouldBe.put (field, shouldBe);
    }

    public boolean isOk () {
        return isOk;
    }
}
