package com.ayronasystems.core.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Moment;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 20/03/16.
 */
public interface MarketData extends Iterable<Moment> {

    Symbol getSymbol();

    Period getPeriod();

    List<Date> getDates();

    Date getBeginningDate();

    Date getEndingDate();

    Date getDate(int index);

    MarketData subData(int period);

    MarketData subData(int beginIdx, int endIdx);

    int getDataCount();

    double[] getData (PriceColumn priceColumn);

    double getData (PriceColumn priceColumn, int index);
}
