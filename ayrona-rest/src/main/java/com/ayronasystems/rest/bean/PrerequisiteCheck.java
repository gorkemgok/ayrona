package com.ayronasystems.rest.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 06/06/16.
 */
public class PrerequisiteCheck {

    public enum ShouldBe{
        NOT_EMPTY("is empty"),
        SELECTED("is not selected"),
        ALPHANUMERIC("is not alphanumeric"),
        ONLY_NUMBER("is not only number"),
        DATE("is not date"),
        EMAIL("is not email");

        private String means;

        ShouldBe (String means) {
            this.means = means;
        }
    }

    public static final PrerequisiteCheck OK = new PrerequisiteCheck (true);

    private boolean isOk;

    private Map<String, ShouldBe> shouldBe = new HashMap<String, ShouldBe> ();

    public PrerequisiteCheck (boolean isOk) {
        this.isOk = isOk;
    }

    public PrerequisiteCheck () {
        isOk = true;
    }

    public void shouldBe(String field, ShouldBe shouldBe){
        isOk = false;
        this.shouldBe.put (field, shouldBe);
    }

    public boolean isOk () {
        return isOk;
    }

    public List<PrerequisiteBean> toBean(){
        List<PrerequisiteBean> beanList = new ArrayList<PrerequisiteBean> ();
        for (Map.Entry<String, ShouldBe> entry : shouldBe.entrySet ()){
            beanList.add (new PrerequisiteBean (entry.getKey (), entry.getValue ().toString ()));
        }
        return beanList;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder ();
        for (Map.Entry<String, ShouldBe> entry : shouldBe.entrySet ()){
            sb.append (entry.getKey ()).append (" ").append (entry.getValue ().means).append(",");
        }
        return sb.toString ();
    }
}
