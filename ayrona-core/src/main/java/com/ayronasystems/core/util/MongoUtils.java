package com.ayronasystems.core.util;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 05/06/16.
 */
public class MongoUtils {

    public static List<ObjectId> convertToObjectIds(List<String> ids){
        List<ObjectId> objectIdList = new ArrayList<ObjectId> (ids.size ());
        for (String accountId : ids){
            objectIdList.add (new ObjectId (accountId));
        }
        return objectIdList;
    }

}
