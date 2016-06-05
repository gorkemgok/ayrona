package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.dao.mysql.TimestampEntity;

import javax.persistence.*;

/**
 * Created by gorkemg on 14.07.2015.
 */
@MappedSuperclass
public class BaseModel extends TimestampEntity {
    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
