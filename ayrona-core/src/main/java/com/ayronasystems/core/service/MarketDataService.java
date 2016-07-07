package com.ayronasystems.core.service;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;

import java.util.Date;

/**
 * Created by gorkemgok on 21/05/16.
 */
public interface MarketDataService {

    MarketData getOHLC(Symbol symbol, Period period, Date startDate, Date endDate);

    MarketData getOHLC(Symbol symbol, Period period, Date startDate);

    MarketData getOHLC (Symbol symbol, Period period, Date endDate, int count);

    MarketData fetchOHLC (Symbol symbol, Period period, Date startDate, Date endDate);

}
