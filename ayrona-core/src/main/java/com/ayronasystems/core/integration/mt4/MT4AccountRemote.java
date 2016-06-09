package com.ayronasystems.core.integration.mt4;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.AccountRemote;
import com.ayronasystems.core.account.AccountRemoteResponse;
import com.ayronasystems.core.account.TORConverter;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.TradeOperationResult;
import com.jfx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by gorkemg on 09.06.2016.
 */
public class MT4AccountRemote implements AccountRemote{

    private static Logger log = LoggerFactory.getLogger (MT4AccountRemote.class);

    private MT4Connection mt4Connection;

    private TORConverter torc = MT4TORConverter.INSTANCE;

    public MT4AccountRemote(MT4Connection mt4Connection) {
        this.mt4Connection = mt4Connection;
    }

    public AccountRemoteResponse openPosition(Position position) {
        try {
            long brokerId = mt4Connection.orderSend(position.getSymbol()
                            .getSymbolString(),
                    position.getDirection() == Direction.LONG ? TradeOperation.OP_BUY : TradeOperation.OP_SELL,
                    position.getLot(),
                    position.getOpenPrice(),
                    10, 0, 0,
                    position.getDescription(),
                    0, null);
            position.setId(String.valueOf(brokerId));
            log.info("Position OPENED: {}", position.toString());
            return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
        }catch (Exception exception){
            return new AccountRemoteResponse(position, torc.convert(exception));
        }
    }

    public AccountRemoteResponse closePosition(Position position, Date closeDate, double closePrice) {
        try {
            mt4Connection.orderClose (Long.valueOf (position.getId ()), position.getLot (), closePrice, 10, 0);
            log.info ("Position CLOSED: {}",position.toString ());
            return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
        } catch ( Exception exception ) {
            return new AccountRemoteResponse(position, torc.convert(exception));
        }
    }
}
