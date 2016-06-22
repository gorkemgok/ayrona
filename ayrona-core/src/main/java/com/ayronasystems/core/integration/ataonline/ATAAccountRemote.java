package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.AccountRemote;
import com.ayronasystems.core.account.AccountRemoteResponse;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.TradeOperationResult;
import com.google.common.base.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class ATAAccountRemote implements AccountRemote {

    private static Configuration conf = Configuration.getInstance ();

    private static Logger log = LoggerFactory.getLogger (ATAAccountRemote.class);

    private String accountNo;

    private HttpClient httpClient;

    private String url;

    public ATAAccountRemote (String accountNo) {
        this.accountNo = accountNo;
        httpClient = HttpClientBuilder.create ().build ();
        url = "http://"+conf.getString (ConfKey.ATA_GTP_HOST)+"/algo/SendDerivativeTransaction?derivativeOrderJson=";
    }

    public AccountRemoteResponse openPosition (Position position) {
        Optional<String> jsonPayloadOptional = ATAOrderPayload.createInstance (Order.Type.OPEN,
                                                                               position.getDirection (),
                                                                               position.getIdealOpenDate (),
                                                                               position.getIdealOpenPrice (),
                                                                               accountNo,
                                                                               position.getLot ()).toJson ();
        if (jsonPayloadOptional.isPresent ()){
            HttpGet request = new HttpGet (url);
            try {
                HttpResponse response = httpClient.execute (request);
                return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
            } catch ( IOException e ) {
                log.error ("Cant send order to GTP.", e);
            }
        }
        return new AccountRemoteResponse(position, TradeOperationResult.FAILED);
    }

    public AccountRemoteResponse closePosition (Position position, Date closeDate, double closePrice) {
        ATAOrderPayload.createInstance (Order.Type.CLOSE,
                                        position.getDirection (),
                                        position.getIdealOpenDate (),
                                        position.getIdealOpenPrice (),
                                        accountNo,
                                        position.getLot ());
        return null;
    }
}
