package com.ayronasystems.core.util.calendar;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketCalendarModel;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.definition.Symbols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 12/08/16.
 */
public class MarketCalendarService {

    private static Logger log = LoggerFactory.getLogger (MarketCalendarService.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    private Map<Symbol, MarketCalendar> calendarMap = new HashMap<Symbol, MarketCalendar> ();

    public MarketCalendarService () {
        List<MarketCalendarModel> marketCalendarModelList = dao.findAllMarketCalendars ();
        for(MarketCalendarModel marketCalendarModel : marketCalendarModelList){
            try {
                MarketCalendar marketCalendar = marketCalendarModel.toMarketCalendar ();
                for ( String symbolString : marketCalendarModel.getSymbols () ){
                    Symbol symbol = Symbols.of (symbolString);
                    calendarMap.put (symbol, marketCalendar);
                }
            } catch ( ParseException e ) {
                log.error ("Cant load market calendar {}", marketCalendarModel.getName (), e);
            }
        }
        for(Map.Entry<Symbol, MarketCalendar> entry : calendarMap.entrySet ()){
            log.info ("Loaded market calendar for {}", entry.getKey ());
        }
    }

    public boolean isMarketOpen(Symbol symbol, Date date){
        MarketCalendar marketCalendar = calendarMap.get (symbol);
        if (marketCalendar != null){
            return marketCalendar.isMarketOpen (date);
        }
        return false;
    }
}
