package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.batchjob.BatchJob;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

/**
 * Created by gorkemgok on 04/06/16.
 */
@Entity("batch_job")
@Indexes ({
            @Index(value = "status", fields = @Field("status")),
            @Index(value = "startDate", fields = @Field("startDate"))
        })
public class BatchJobModel {

    @Id
    private ObjectId id;

    private Date startDate;

    private BatchJob.Status status;

    private BatchJob.Type type;

    private int progress;

    public String getId () {
        return id.toString ();
    }

    public Date getStartDate () {
        return startDate;
    }

    public void setStartDate (Date startDate) {
        this.startDate = startDate;
    }

    public BatchJob.Status getStatus () {
        return status;
    }

    public void setStatus (BatchJob.Status status) {
        this.status = status;
    }

    public BatchJob.Type getType () {
        return type;
    }

    public void setType (BatchJob.Type type) {
        this.type = type;
    }

    public int getProgress () {
        return progress;
    }

    public void setProgress (int progress) {
        this.progress = progress;
    }
}
