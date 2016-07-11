package com.ayronasystems.data.listener;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketDataModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gorkemgok on 07/06/16.
 */
public class BarDBSaverListener implements BarListener {

    private static Logger log = LoggerFactory.getLogger (BarDBSaverListener.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    public void newBar (Symbol symbol, Period period, Bar bar) {
        MarketDataModel marketDataModel = new MarketDataModel ();
        marketDataModel.setSymbol (symbol);
        marketDataModel.setPeriod (period);
        marketDataModel.setPeriodDate (bar.getDate ());
        marketDataModel.setOpen (bar.getOpen ());
        marketDataModel.setHigh (bar.getHigh ());
        marketDataModel.setLow (bar.getLow ());
        marketDataModel.setClose (bar.getClose ());
        marketDataModel.setVolume (bar.getVolume ());
        dao.createMarketData (marketDataModel);
        log.info ("Bar saved to {} - {}, {}", symbol, period, bar);
    }
}
