package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.MarketDataConversionException;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.Interval;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 20/03/16.
 */
public interface MarketData extends Iterable<Moment> {

    Symbol getSymbol();

    Period getPeriod();

    List<Date> getDates();

    Interval getInterval();

    Date getBeginningDate();

    Date getEndingDate();

    Date getLastDate();

    Date getDate(int index);

    MarketData subData(int period);

    MarketData subData(int beginIdx, int endIdx);

    MarketData subData(Date beginDate, Date endDate);

    MarketData convert(Period period) throws MarketDataConversionException;

    MarketData append(MarketData marketData);

    MarketData merge(MarketData marketData);

    MarketData safeMerge(MarketData marketData);

    int size ();

    boolean isEmpty();

    double[] getPrice (PriceColumn priceColumn);

    double getPrice (PriceColumn priceColumn, int index);
}
