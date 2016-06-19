package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.definition.Direction;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class OrderPayloadTest {

    @Test
    public void toJson () throws Exception {
        Optional<String> payloadStringOptional = OrderPayload.createInstance (Order.Type.OPEN, Direction.LONG, new Date(), 96.7, "testAccountNo", 2).toJson ();
        assertTrue (payloadStringOptional.isPresent ());
        String payloadString = payloadStringOptional.get();
        assertFalse (payloadString.isEmpty ());
        System.out.print (payloadString);
    }

}