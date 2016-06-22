package com.ayronasystems.core.account;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.definition.TradeOperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by gorkemg on 09.06.2016.
 */
public class NoOpAccountRemote implements AccountRemote {

    private static Logger log = LoggerFactory.getLogger (NoOpAccountRemote.class);

    public static final NoOpAccountRemote INSTANCE = new NoOpAccountRemote();

    private NoOpAccountRemote(){

    }
    public AccountRemoteResponse openPosition(Position position) {
        position.setOpenDate (position.getIdealOpenDate ());
        position.setOpenPrice (position.getIdealOpenPrice ());
        log.debug ("Position opened for {}: {}, {}, {}, {}",position.getAccountName (), position.getDirection (), position.getSymbol (), position.getInitiator ().getName (), position.getOpenPrice ());
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }

    public AccountRemoteResponse closePosition(Position position, Date closeDate, double closePrice) {
        position.close (closeDate, closePrice);
        position.setCloseDate (position.getIdealCloseDate ());
        position.setClosePrice (position.getIdealClosePrice ());
        log.debug ("Position closed for {}: {}, {}, {}, {}",position.getAccountName (), position.getDirection (), position.getSymbol (),  position.getInitiator ().getName (), position.getClosePrice ());
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }
}
