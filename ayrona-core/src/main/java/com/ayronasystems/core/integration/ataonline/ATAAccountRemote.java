package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.AccountRemote;
import com.ayronasystems.core.account.AccountRemoteResponse;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.Direction;
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

    private String customerNo;

    private String accountNo;

    private HttpClient httpClient;

    private String url;

    public ATAAccountRemote (String accountNo) {
        String[] an = accountNo.split ("-");
        this.customerNo = an[0];
        if (an.length > 1){
            this.accountNo = an[1];
        }
        httpClient = HttpClientBuilder.create ().build ();
        url = "http://"+conf.getString (ConfKey.ATA_GTP_HOST)+"/algo/SendDerivativeTransaction?derivativeOrderJson=";
    }

    public AccountRemoteResponse openPosition (Position position) {

        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrder (Order.Type.OPEN,
                                                    position.getDirection (),
                                                    position.getIdealOpenDate (),
                                                    position.getIdealOpenPrice (),
                                                    position.getLot ()));
        log.info ("Opened position account:{}, {}", accountNo, position);
        return response;
    }

    public AccountRemoteResponse closePosition (Position position, Date closeDate, double closePrice) {
        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrder (Order.Type.CLOSE,
                                                    position.getDirection (),
                                                    closeDate,
                                                    closePrice,
                                                    position.getLot ()));
        log.info ("Closed position account:{}, {}", accountNo, position);
        return response;
    }

    private TradeOperationResult sendOrder(Order.Type type, Direction direction, Date date, double price, double lot){
        Optional<String> jsonPayloadOptional = ATAOrderPayload.createInstance (
                                        type,
                                        direction,
                                        date,
                                        price,
                                        customerNo,
                                        accountNo,
                                        lot).toJson ();
        if (jsonPayloadOptional.isPresent ()){
            HttpGet request = new HttpGet (url);
            try {
                HttpResponse response = httpClient.execute (request);
                return TradeOperationResult.SUCCESSFUL;
            } catch ( IOException e ) {
                log.error ("Cant send order to GTP.", e);
            }
        }
        return TradeOperationResult.FAILED;
    }
}
