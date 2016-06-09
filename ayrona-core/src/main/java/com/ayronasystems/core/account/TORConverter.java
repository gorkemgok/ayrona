package com.ayronasystems.core.account;

import com.ayronasystems.core.definition.TradeOperationResult;

/**
 * Created by gorkemg on 09.06.2016.
 */
public interface TORConverter {

    TradeOperationResult convert(Exception exception);
}
