package com.ayronasystems.core.dao.mysql;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * Created by gorkemgok on 22/02/15.
 */
@MappedSuperclass
public class TimestampEntity {
    private Date createdDate = new Date();
    private Date updatedDate = new Date();

    @XmlTransient
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdate", nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @XmlTransient
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedate", nullable = false)
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void onCreate(){
        updatedDate = createdDate = new Date();
    }

    public  void onUpdate(){
        updatedDate = new Date();
    }
}
