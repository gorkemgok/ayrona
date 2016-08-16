package com.ayronasystems.core.account;

import com.ayronasystems.core.definition.TradeOperationResult;
import com.ayronasystems.core.strategy.Position;

/**
 * Created by gorkemg on 09.06.2016.
 */
public class AccountRemoteResponse {

    private Position position;

    private TradeOperationResult tradeOperationResult;

    public AccountRemoteResponse(Position position, TradeOperationResult tradeOperationResult) {
        this.position = position;
        this.tradeOperationResult = tradeOperationResult;
    }

    public Position getPosition() {
        return position;
    }

    public TradeOperationResult getTradeOperationResult() {
        return tradeOperationResult;
    }
}
