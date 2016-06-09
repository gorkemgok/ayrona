package com.ayronasystems.core.account;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.definition.TradeOperationResult;

import java.util.Date;

/**
 * Created by gorkemg on 09.06.2016.
 */
public class NoOpAccountRemote implements AccountRemote {

    public static final NoOpAccountRemote INSTANCE = new NoOpAccountRemote();

    private NoOpAccountRemote(){

    }
    public AccountRemoteResponse openPosition(Position position) {
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }

    public AccountRemoteResponse closePosition(Position position, Date closeDate, double closePrice) {
        return new AccountRemoteResponse(position, TradeOperationResult.SUCCESSFUL);
    }
}
