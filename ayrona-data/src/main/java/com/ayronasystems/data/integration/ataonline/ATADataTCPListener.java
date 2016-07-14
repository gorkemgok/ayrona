package com.ayronasystems.data.integration.ataonline;

import com.ayronasystems.core.configuration.HostPort;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by gorkemgok on 21/06/16.
 */
public class ATADataTCPListener implements Runnable {

    private static Logger log = LoggerFactory.getLogger (ATADataTCPListener.class);

    private ATAJsonConverter ataJsonConverter;

    private Socket clientSocket;

    List<HostPort> hostPortList;

    private volatile boolean isStop = false;

    private ATAMarketDataListener payloadListener;

    private int currentHPIndex = 0;

    private boolean retry = true;

    private int retryCount = 50;

    private int retryWaitInSeconds = 5;

    public ATADataTCPListener (List<HostPort> hostPortList, ATAMarketDataListener payloadListener) {
        this.hostPortList = hostPortList;
        this.payloadListener = payloadListener;
        this.ataJsonConverter = new ATAJsonConverter ();
    }

    public void connect(String host, int port) throws IOException {
        log.info ("Connecting to host:{} port:{}", host, port);
        clientSocket = new Socket (host, port);
        log.info ("Connected to host:{} port:{}", host, port);

    }

    public void run () {
        boolean retrying = false;
        int retryCount = 0;
        while (retry) {
            try {
                HostPort hostPort = hostPortList.get (currentHPIndex);
                connect (hostPort.getHost (), hostPort.getPort ());
                BufferedReader bufferedReader = new BufferedReader (
                        new InputStreamReader (clientSocket.getInputStream ()));
                log.info ("Started listening ata payload");
                retrying = false;
                retryCount = 0;
                String dataJson;
                while ( !isStop && (dataJson = bufferedReader.readLine ()) != null ) {
                    Optional<ATAMarketDataPayload> marketDataPayloadOptional =
                            ataJsonConverter.convertFromJson (dataJson);
                    if ( marketDataPayloadOptional.isPresent () ) {
                        ATAMarketDataPayload marketDataPayload = marketDataPayloadOptional.get ();
                        payloadListener.newPayload (marketDataPayload);
                    }
                }
            } catch ( Exception e ) {
                if (retrying){
                    log.error ("Error while retrying ATA tcp data connection: {}", e.getMessage ());
                }else{
                    log.error ("Error while listening ATA tcp data connection.", e);
                }
                if (retry && retryCount < this.retryCount){
                    retryCount++;
                    retrying = true;
                    switchHostPortIndex ();
                    log.info ("Retrying to connect in {} seconds...Attempt:{}", retryWaitInSeconds, retryCount);
                    try {
                        Thread.currentThread ().sleep (retryWaitInSeconds * 1000);
                    } catch ( InterruptedException e1 ) {
                        log.info ("Stopped retrying to connect at attempt {} because of interruption", retryCount);
                        break;
                    }
                }else{
                    log.info ("Stopped retrying to connect at attempt {}", retryCount);
                    break;
                }
            }
        }
        disconnect ();
    }

    public void switchHostPortIndex(){
        currentHPIndex++;
        if (currentHPIndex == hostPortList.size ()){
            currentHPIndex = 0;
        }
    }

    public void stop(){
        isStop = true;
    }

    public void disconnect(){
        if (clientSocket != null){
            try {
                clientSocket.close();
                log.info ("Disconnected Ata TCP");
            } catch ( IOException e ) {
                log.error ("Error while disconnecting ATA tcp data connection.",e);
            }
        }
    }

    public boolean isRetry () {
        return retry;
    }

    public void setRetry (boolean retry) {
        this.retry = retry;
    }

    public int getRetryCount () {
        return retryCount;
    }

    public void setRetryCount (int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryWaitInSeconds () {
        return retryWaitInSeconds;
    }

    public void setRetryWaitInSeconds (int retryWaitInSeconds) {
        this.retryWaitInSeconds = retryWaitInSeconds;
    }

}
