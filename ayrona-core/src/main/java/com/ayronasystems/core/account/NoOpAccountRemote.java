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
        log.info ("Position opened: {}", position);
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }

    public AccountRemoteResponse closePosition(Position position, Date closeDate, double closePrice) {
        log.info ("Position closed: {}", position);
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }
}
