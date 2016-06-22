package com.ayronasystems.data.integration.ataonline;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATADataTCPListener implements Runnable {

    private static Logger log = LoggerFactory.getLogger (ATADataTCPListener.class);

    private ATAJsonConverter ataJsonConverter;

    private Socket clientSocket;

    private String host;

    private int port;

    private volatile boolean isStop = false;

    private ATAMarketDataPayloadListener payloadListener;

    public ATADataTCPListener (String host, int port, ATAMarketDataPayloadListener payloadListener) {
        this.host = host;
        this.port = port;
        this.payloadListener = payloadListener;
        this.ataJsonConverter = new ATAJsonConverter ();
    }

    public void connect() throws IOException {
        log.info ("Connecting to host:{} port:{}", host, port);
        clientSocket = new Socket (host, port);
        log.info ("Connected to host:{} port:{}", host, port);

    }

    public void run () {
        String dataJson;
        try {
            connect ();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (clientSocket.getInputStream ()));
            while ( !isStop && (dataJson = bufferedReader.readLine ()) != null) {
                long start = System.currentTimeMillis ();
                Optional<ATAMarketDataPayload> marketDataPayloadOptional =
                        ataJsonConverter.convertFromJson (dataJson);
                if (marketDataPayloadOptional.isPresent ()){
                    ATAMarketDataPayload marketDataPayload = marketDataPayloadOptional.get ();
                    payloadListener.newPayload (marketDataPayload);
                }
                long end = System.currentTimeMillis ();
            }
        }catch ( Exception e ){
            log.error ("Error while listening ATA tcp data connection.",e);
        }finally {
           disconnect ();
        }
    }

    public void stop(){
        isStop = true;
    }

    public void disconnect(){
        if (clientSocket != null){
            try {
                clientSocket.close();
                log.info ("Disconnected to host:{} port:{}", host, port);
            } catch ( IOException e ) {
                log.error ("Error while disconnecting ATA tcp data connection.",e);
            }
        }
    }
}
