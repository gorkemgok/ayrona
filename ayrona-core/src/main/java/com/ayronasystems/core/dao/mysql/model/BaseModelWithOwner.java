package com.ayronasystems.core.dao.mysql.model;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * Created by gorkemgok on 30/12/15.
 */
@MappedSuperclass
public class BaseModelWithOwner extends BaseModel {

    private UserModel owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    public UserModel getOwner () {
        return owner;
    }

    public void setOwner (UserModel owner) {
        this.owner = owner;
    }
}
