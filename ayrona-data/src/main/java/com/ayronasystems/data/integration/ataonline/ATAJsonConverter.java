package com.ayronasystems.data.integration.ataonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATAJsonConverter {

    private static Logger log = LoggerFactory.getLogger (ATAMarketDataPayload.class);

    private ObjectMapper mapper;

    public ATAJsonConverter () {
        mapper = new ObjectMapper ();
    }

    public Optional<ATAMarketDataPayload> convertFromJson(String jsonString){
        try {
            ATAMarketDataPayload marketData = mapper.readValue (jsonString, ATAMarketDataPayload.class);
            return Optional.of (marketData);
        } catch ( IOException e ) {
            log.error ("Cant convert marketdata from json.", e);
            return Optional.absent ();
        }
    }

}
