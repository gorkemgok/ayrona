package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.strategy.Order;
import com.ayronasystems.core.strategy.Position;
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

    private static class SendOrderResult{
        private String refCode;

        private TradeOperationResult tradeOperationResult;

        public SendOrderResult (TradeOperationResult tradeOperationResult) {
            this.tradeOperationResult = tradeOperationResult;
        }

        public SendOrderResult (String refCode, TradeOperationResult tradeOperationResult) {
            this.refCode = refCode;
            this.tradeOperationResult = tradeOperationResult;
        }

        public String getRefCode () {
            return refCode;
        }

        public TradeOperationResult getTradeOperationResult () {
            return tradeOperationResult;
        }
    }

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
        SendOrderResult sendOrderResult = sendOrder (
                position.getSymbol (),
                Order.Type.OPEN,
                position.getDirection (),
                position.getIdealOpenDate (),
                position.getIdealOpenPrice (),
                position.getLot ());
        position.setRemoteId (sendOrderResult.getRefCode ());
        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrderResult.getTradeOperationResult ());
        if (response.getTradeOperationResult ().isFailed ()){
            log.info ("Failed Opening position account:{}-{}, {}", customerNo, accountNo, position);
        }else{
            log.info ("Opened position account:{}-{}, {}", customerNo, accountNo, position);
        }
        return response;
    }

    public AccountRemoteResponse closePosition (Position position, Date closeDate, double closePrice) {
        SendOrderResult sendOrderResult = sendOrder (
                position.getSymbol (),
                Order.Type.CLOSE,
                position.getDirection (),
                closeDate,
                closePrice,
                position.getLot ());
        position.setRemoteId (sendOrderResult.getRefCode ());
        AccountRemoteResponse response = new AccountRemoteResponse(position, sendOrderResult.getTradeOperationResult ());
        if (response.getTradeOperationResult ().isFailed ()){
            log.info ("Failed Closing position account:{}-{}, {}", customerNo, accountNo, position);
        }else {
            log.info ("Closed position account:{}-{}, {}", customerNo, accountNo, position);
        }
        return response;
    }

    private SendOrderResult sendOrder(Symbol symbol, Order.Type type, Direction direction, Date date, double price, double lot){
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
                String msg = ataOrderResponse.getMessage ();
                SendOrderResult sendOrderResult;
                if (ataOrderResponse.isSuccessful ()){
                    int p = msg.indexOf (':');
                    String refCode = msg.substring (p+1).trim ();
                    sendOrderResult = new SendOrderResult(refCode, TradeOperationResult.SUCCESSFUL);
                    log.info ("Order send:{}, {}-{}, {}, price:{}, lot:{}, {}, refCode : {}",
                              "SUCCESSFUL",
                              customerNo, accountNo, type, price, lot, DateUtils.formatDate (date, "dd.MM.yyyy HH:mm:ss"),
                              refCode);
                }else {
                    sendOrderResult = new SendOrderResult (TradeOperationResult.FAILED);
                    log.info ("Order send:{}, account:{}-{}, {}, price:{}, lot:{}, {}, message : {}, full response {}",
                              "FAILED",
                              customerNo, accountNo, type, price, lot, DateUtils.formatDate (date, "dd.MM.yyyy HH:mm:ss"),
                              msg,
                              responseString);
                }
                return sendOrderResult;
            } catch ( IOException e ) {
                log.error ("Cant send order to GTP.", e);
            }
        }
        return new SendOrderResult (TradeOperationResult.FAILED);
    }

    public static void main(String[] args){
        /*Symbols.init ();
        ATAAccountRemote ataAccountRemote = new ATAAccountRemote ("233910-100");
        TradeOperationResult result = ataAccountRemote.sendOrder (
                Symbols.of ("VOB_XAUTRY"), Order.Type.OPEN, Direction.LONG, new Date(), 128, 1
        );*/
    }
}
