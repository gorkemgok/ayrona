package com.ayronasystems.core.integration.mt4;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.definition.Direction;
import com.jfx.*;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by gorkemgok on 26/12/15.
 */
public class MT4Account extends BasicAccount {

    private static Logger log = LoggerFactory.getLogger (MT4Account.class);

    private MT4Connection mt4Connection;

    private String accountId;

    public MT4Account (String accountId, MT4Connection mt4Connection) {
        this.accountId = accountId;
        this.mt4Connection = mt4Connection;
    }

    @Override
    public boolean openPosition (Position position) {
        super.openPosition (position);
        try {
            long brokerId = mt4Connection.orderSend (position.getSymbol ()
                                                                   .getSymbolString (),
                                                           position.getDirection () == Direction.LONG ? TradeOperation.OP_BUY : TradeOperation.OP_SELL,
                                                           position.getLot (),
                                                           position.getOpenPrice (),
                                                           10, 0, 0,
                                                           position.getDescription (),
                                                           0, null);
            position.setId (String.valueOf (brokerId));
            log.info ("Position OPENED: {}",position.toString ());
            sendSMS (position);
            return true;
        } catch ( ErrTradeTooManyOrders errTradeTooManyOrders ) {
            errTradeTooManyOrders.printStackTrace ();
        } catch ( ErrUnknownSymbol errUnknownSymbol ) {
            errUnknownSymbol.printStackTrace ();
        } catch ( ErrCustomIndicatorError errCustomIndicatorError ) {
            errCustomIndicatorError.printStackTrace ();
        } catch ( ErrPriceChanged errPriceChanged ) {
            errPriceChanged.printStackTrace ();
        } catch ( ErrTradeNotAllowed errTradeNotAllowed ) {
            errTradeNotAllowed.printStackTrace ();
        } catch ( ErrTooFrequentRequests errTooFrequentRequests ) {
            errTooFrequentRequests.printStackTrace ();
        } catch ( ErrAccountDisabled errAccountDisabled ) {
            errAccountDisabled.printStackTrace ();
        } catch ( ErrNotEnoughMoney errNotEnoughMoney ) {
            errNotEnoughMoney.printStackTrace ();
        } catch ( ErrInvalidAccount errInvalidAccount ) {
            errInvalidAccount.printStackTrace ();
        } catch ( ErrTradeTimeout4 errTradeTimeout4 ) {
            errTradeTimeout4.printStackTrace ();
        } catch ( ErrTradeTimeout3 errTradeTimeout3 ) {
            errTradeTimeout3.printStackTrace ();
        } catch ( ErrOrderLocked errOrderLocked ) {
            errOrderLocked.printStackTrace ();
        } catch ( ErrInvalidTradeParameters errInvalidTradeParameters ) {
            errInvalidTradeParameters.printStackTrace ();
        } catch ( ErrTradeTimeout2 errTradeTimeout2 ) {
            errTradeTimeout2.printStackTrace ();
        } catch ( ErrInvalidTradeVolume errInvalidTradeVolume ) {
            errInvalidTradeVolume.printStackTrace ();
        } catch ( ErrInvalidFunctionParamvalue errInvalidFunctionParamvalue ) {
            errInvalidFunctionParamvalue.printStackTrace ();
        } catch ( ErrOffQuotes errOffQuotes ) {
            errOffQuotes.printStackTrace ();
        } catch ( ErrCommonError errCommonError ) {
            errCommonError.printStackTrace ();
        } catch ( ErrLongsNotAllowed errLongsNotAllowed ) {
            errLongsNotAllowed.printStackTrace ();
        } catch ( ErrStringParameterExpected errStringParameterExpected ) {
            errStringParameterExpected.printStackTrace ();
        } catch ( ErrShortsNotAllowed errShortsNotAllowed ) {
            errShortsNotAllowed.printStackTrace ();
        } catch ( ErrInvalidPrice errInvalidPrice ) {
            errInvalidPrice.printStackTrace ();
        } catch ( ErrIntegerParameterExpected errIntegerParameterExpected ) {
            errIntegerParameterExpected.printStackTrace ();
        } catch ( ErrTradeModifyDenied errTradeModifyDenied ) {
            errTradeModifyDenied.printStackTrace ();
        } catch ( ErrTradeContextBusy errTradeContextBusy ) {
            errTradeContextBusy.printStackTrace ();
        } catch ( ErrRequote errRequote ) {
            errRequote.printStackTrace ();
        } catch ( ErrTradeTimeout errTradeTimeout ) {
            errTradeTimeout.printStackTrace ();
        } catch ( ErrNoConnection errNoConnection ) {
            errNoConnection.printStackTrace ();
        } catch ( ErrInvalidStops errInvalidStops ) {
            errInvalidStops.printStackTrace ();
        } catch ( ErrInvalidPriceParam errInvalidPriceParam ) {
            errInvalidPriceParam.printStackTrace ();
        } catch ( ErrTradeExpirationDenied errTradeExpirationDenied ) {
            errTradeExpirationDenied.printStackTrace ();
        } catch ( ErrTradeDisabled errTradeDisabled ) {
            errTradeDisabled.printStackTrace ();
        } catch ( ErrMarketClosed errMarketClosed ) {
            errMarketClosed.printStackTrace ();
        } catch ( ErrServerBusy errServerBusy ) {
            errServerBusy.printStackTrace ();
        } catch ( ErrOldVersion errOldVersion ) {
            errOldVersion.printStackTrace ();
        } catch ( ErrTooManyRequests errTooManyRequests ) {
            errTooManyRequests.printStackTrace ();
        } catch ( ErrLongPositionsOnlyAllowed errLongPositionsOnlyAllowed ) {
            errLongPositionsOnlyAllowed.printStackTrace ();
        }
        return false;
    }

    @Override
    public boolean closePosition (Position position, Date closeDate, double closePrice) {
        try {
            mt4Connection.orderClose (Long.valueOf (position.getId ()), position.getLot (), closePrice, 10, 0);
            super.closePosition (position, closeDate, closePrice);
            log.info ("Position CLOSED: {}",position.toString ());
            sendSMS (position);
            return true;
        } catch ( ErrTradeTimeout errTradeTimeout ) {
            errTradeTimeout.printStackTrace ();
        } catch ( ErrAccountDisabled errAccountDisabled ) {
            errAccountDisabled.printStackTrace ();
        } catch ( ErrInvalidTicket errInvalidTicket ) {
            errInvalidTicket.printStackTrace ();
        } catch ( ErrCustomIndicatorError errCustomIndicatorError ) {
            errCustomIndicatorError.printStackTrace ();
        } catch ( ErrInvalidPrice errInvalidPrice ) {
            errInvalidPrice.printStackTrace ();
        } catch ( ErrInvalidTradeParameters errInvalidTradeParameters ) {
            errInvalidTradeParameters.printStackTrace ();
        } catch ( ErrUnknownSymbol errUnknownSymbol ) {
            errUnknownSymbol.printStackTrace ();
        } catch ( ErrLongPositionsOnlyAllowed errLongPositionsOnlyAllowed ) {
            errLongPositionsOnlyAllowed.printStackTrace ();
        } catch ( ErrNotEnoughMoney errNotEnoughMoney ) {
            errNotEnoughMoney.printStackTrace ();
        } catch ( ErrOldVersion errOldVersion ) {
            errOldVersion.printStackTrace ();
        } catch ( ErrInvalidStops errInvalidStops ) {
            errInvalidStops.printStackTrace ();
        } catch ( ErrTradeTooManyOrders errTradeTooManyOrders ) {
            errTradeTooManyOrders.printStackTrace ();
        } catch ( ErrIntegerParameterExpected errIntegerParameterExpected ) {
            errIntegerParameterExpected.printStackTrace ();
        } catch ( ErrTradeContextBusy errTradeContextBusy ) {
            errTradeContextBusy.printStackTrace ();
        } catch ( ErrCommonError errCommonError ) {
            errCommonError.printStackTrace ();
        } catch ( ErrMarketClosed errMarketClosed ) {
            errMarketClosed.printStackTrace ();
        } catch ( ErrInvalidFunctionParamvalue errInvalidFunctionParamvalue ) {
            errInvalidFunctionParamvalue.printStackTrace ();
        } catch ( ErrTradeDisabled errTradeDisabled ) {
            errTradeDisabled.printStackTrace ();
        } catch ( ErrTooManyRequests errTooManyRequests ) {
            errTooManyRequests.printStackTrace ();
        } catch ( ErrInvalidPriceParam errInvalidPriceParam ) {
            errInvalidPriceParam.printStackTrace ();
        } catch ( ErrTradeExpirationDenied errTradeExpirationDenied ) {
            errTradeExpirationDenied.printStackTrace ();
        } catch ( ErrTooFrequentRequests errTooFrequentRequests ) {
            errTooFrequentRequests.printStackTrace ();
        } catch ( ErrRequote errRequote ) {
            errRequote.printStackTrace ();
        } catch ( ErrNoConnection errNoConnection ) {
            errNoConnection.printStackTrace ();
        } catch ( ErrPriceChanged errPriceChanged ) {
            errPriceChanged.printStackTrace ();
        } catch ( ErrOffQuotes errOffQuotes ) {
            errOffQuotes.printStackTrace ();
        } catch ( ErrTradeTimeout4 errTradeTimeout4 ) {
            errTradeTimeout4.printStackTrace ();
        } catch ( ErrTradeTimeout3 errTradeTimeout3 ) {
            errTradeTimeout3.printStackTrace ();
        } catch ( ErrTradeTimeout2 errTradeTimeout2 ) {
            errTradeTimeout2.printStackTrace ();
        } catch ( ErrInvalidTradeVolume errInvalidTradeVolume ) {
            errInvalidTradeVolume.printStackTrace ();
        } catch ( ErrOrderLocked errOrderLocked ) {
            errOrderLocked.printStackTrace ();
        } catch ( ErrTradeNotAllowed errTradeNotAllowed ) {
            errTradeNotAllowed.printStackTrace ();
        } catch ( ErrTradeModifyDenied errTradeModifyDenied ) {
            errTradeModifyDenied.printStackTrace ();
        } catch ( ErrServerBusy errServerBusy ) {
            errServerBusy.printStackTrace ();
        } catch ( ErrInvalidAccount errInvalidAccount ) {
            errInvalidAccount.printStackTrace ();
        }
        return false;
    }

    public void sendSMS (Position position) {
        String islem = position.getDirection () == Direction.LONG ? "Alis" : "Satis";
        try {
            String msg = "Ayrona Otomatik Islem:"+position.getSymbol ()+","+position.getLot ()+" Lot,"+islem+",Fiyat:"+position.getOpenPrice ();
            msg += position.isClosed () ? ", Kapanis:"+position.getClosePrice ()+", Kar:%"+((position.calculateProfit () / position.getOpenPrice ()) * 100) : "" ;
            String url =
                    "http://api.clickatell.com/http/sendmsg?" +
                            "user=gorkemgok&" +
                            "password=g0rk3mg0k&" +
                            "api_id=3376551&" +
                            "from=905432609612&" +
                            "to=905432609612,905320634533,905443996962&" +
                            "text="+ URIUtil.encodeAll (msg);

            HttpClient client = HttpClientBuilder.create ().build ();
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            log.info ("SMS request send, Response Code : "
                              + response.getStatusLine().getStatusCode());

        }catch ( Exception ex ){
            log.error ("Error on sending SMS ", ex);
        }
    }
}
