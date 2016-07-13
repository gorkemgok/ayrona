package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.AccountRemote;
import com.ayronasystems.core.account.AccountRemoteResponse;
import com.ayronasystems.core.configuration.ConfKey;
import com.ayronasystems.core.configuration.Configuration;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.TradeOperationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
        url = "http://"+conf.getString (ConfKey.ATA_GTP_HOST)+"/Algo/SendDerivativeTransaction";
    }

    public AccountRemoteResponse openPosition (Position position) {

        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrder (
                position.getSymbol (),
                Order.Type.OPEN,
                position.getDirection (),
                position.getIdealOpenDate (),
                position.getIdealOpenPrice (),
                position.getLot ()));
        log.info ("Opened position account:{}, {}", accountNo, position);
        return response;
    }

    public AccountRemoteResponse closePosition (Position position, Date closeDate, double closePrice) {
        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrder (
                position.getSymbol (),
                Order.Type.CLOSE,
                position.getDirection (),
                closeDate,
                closePrice,
                position.getLot ()));
        log.info ("Closed position account:{}, {}", accountNo, position);
        return response;
    }

    private TradeOperationResult sendOrder(Symbol symbol, Order.Type type, Direction direction, Date date, double price, double lot){
        Optional<String> jsonPayloadOptional = ATAOrderPayload.createInstance (
                                        symbol,
                                        type,
                                        direction,
                                        date,
                                        price,
                                        customerNo,
                                        accountNo,
                                        lot).toJson ();
        if (jsonPayloadOptional.isPresent ()){
            HttpPost request = new HttpPost (url);
            try {
                request.setEntity (new UrlEncodedFormEntity (Arrays.asList (new BasicNameValuePair (
                        "derivativeOrderJson",
                        jsonPayloadOptional.get ()
                ))));
                HttpResponse response = httpClient.execute (request);
                BufferedReader rd = new BufferedReader (new InputStreamReader (response.getEntity().getContent()));

                StringBuilder responseString = new StringBuilder ();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    responseString.append(line);
                }
                ObjectMapper objectMapper = new ObjectMapper ();
                ATAOrderResponse ataOrderResponse = objectMapper.readValue (responseString.toString (), ATAOrderResponse.class);
                log.info ("Order send:{}, {}-{}, {}, price:{}, lot:{}, {}, message : {}, full response {}",
                          ataOrderResponse.isSuccessful () ? "SUCCESSFUL" : "FAILED",
                          customerNo, accountNo, type, price, lot, DateUtils.formatDate (date, "dd.MM.yyyy HH:mm:ss"),
                          ataOrderResponse.getMessage (),
                          responseString);
                return ataOrderResponse.isSuccessful () ? TradeOperationResult.SUCCESSFUL : TradeOperationResult.FAILED;
            } catch ( IOException e ) {
                log.error ("Cant send order to GTP.", e);
            }
        }
        return TradeOperationResult.FAILED;
    }

    public static void main(String[] args){
        /*Symbols.init ();
        ATAAccountRemote ataAccountRemote = new ATAAccountRemote ("233910-100");
        TradeOperationResult result = ataAccountRemote.sendOrder (
                Symbols.of ("VOB_XAUTRY"), Order.Type.OPEN, Direction.LONG, new Date(), 128, 1
        );*/
    }
}
