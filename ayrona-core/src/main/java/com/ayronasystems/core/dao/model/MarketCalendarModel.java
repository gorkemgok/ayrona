package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.util.calendar.MarketCalendar;
import org.mongodb.morphia.annotations.Indexed;

import java.text.ParseException;
import java.util.List;

/**
 * Created by gorkemgok on 09/08/16.
 */
public class MarketCalendarModel extends BaseModel{

    @Indexed(name = "name", unique = true)
    private String name;

    private List<DayIntervalsEmbedded> dayIntervalsEmbeddedList;

    private List<String> symbols;

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<DayIntervalsEmbedded> getDayIntervalsEmbeddedList () {
        return dayIntervalsEmbeddedList;
    }

    public void setDayIntervalsEmbeddedList (List<DayIntervalsEmbedded> dayIntervalsEmbeddedList) {
        this.dayIntervalsEmbeddedList = dayIntervalsEmbeddedList;
    }

    public List<String> getSymbols () {
        return symbols;
    }

    public void setSymbols (List<String> symbols) {
        this.symbols = symbols;
    }

    public MarketCalendar toMarketCalendar() throws ParseException {
        MarketCalendar marketCalendar = new MarketCalendar ();
        for (DayIntervalsEmbedded dayIntervalsEmbedded : dayIntervalsEmbeddedList){
            if (dayIntervalsEmbedded.isOff ()){
                marketCalendar.exclude (
                        dayIntervalsEmbedded.getExpression (),
                        dayIntervalsEmbedded.getExcludedDays ()
                );
            }else{
                marketCalendar.include (
                        dayIntervalsEmbedded.getExpression (),
                        dayIntervalsEmbedded.getExcludedDays ()
                );
            }
        }
        return marketCalendar;
    }
}
