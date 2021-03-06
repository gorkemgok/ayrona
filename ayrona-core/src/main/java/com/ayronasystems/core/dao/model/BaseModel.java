package com.ayronasystems.core.dao.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

import java.util.Date;

/**
 * Created by gorkemgok on 05/06/16.
 */
@Entity
public class BaseModel {

    @Id
    private ObjectId id;

    private Date createDate;

    private Date updateDate;

    public String getId () {
        return id.toString ();
    }

    public void setId (ObjectId id) {
        this.id = id;
    }

    public Date getCreateDate () {
        return createDate;
    }

    public Date getUpdateDate () {
        return updateDate;
    }

    @PrePersist
    public void prePersist(){
        Date date = new Date();
        createDate = date;
        updateDate = date;
    }
}
