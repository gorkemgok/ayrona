package com.ayronasystems.core.util.calendar;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketCalendarModel;
import com.ayronasystems.core.definition.Symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 12/08/16.
 */
public class MarketCalendarService {

    private Dao dao = Singletons.INSTANCE.getDao ();

    private Map<Symbol, MarketCalendar> calendarMap = new HashMap<Symbol, MarketCalendar> ();

    public MarketCalendarService () {
        List<MarketCalendarModel> marketCalendarModelList = dao.findAllMarketCalendars ();
        for(MarketCalendarModel marketCalendarModel : marketCalendarModelList){
            
        }
    }
}
