package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.Tick;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.ayronasystems.core.util.DateUtils;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class BarifierTest {

    private static final List<Tick> tickList = new ArrayList<Tick> (
            Arrays.asList (
                    new Tick[]{
                            new Tick (DateUtils.parseDate ("01.01.2016 01:00:00"), Symbol.VOB30, 0, 1.4, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:00:01"), Symbol.VOB30, 0, 2, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:00:10"), Symbol.VOB30, 0, 0.1, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:00:50"), Symbol.VOB30, 0, 1.3, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:00:53"), Symbol.VOB30, 0, .8, 0),

                            new Tick (DateUtils.parseDate ("01.01.2016 01:01:01"), Symbol.VOB30, 0, 2.4, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:01:03"), Symbol.VOB30, 0, 5, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:01:17"), Symbol.VOB30, 0, 2.1, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:01:50"), Symbol.VOB30, 0, 1.3, 0),
                            new Tick (DateUtils.parseDate ("01.01.2016 01:01:53"), Symbol.VOB30, 0, 7.8, 0),

                            new Tick (DateUtils.parseDate ("01.01.2016 01:02:03"), Symbol.VOB30, 0, 7.8, 0)
                    }
            )
    );
    @Test
    public void newTick () throws Exception {
        Barifier barifier = new Barifier (Period.M1);

        for (Tick tick : tickList){
            barifier.newTick (tick);
        }

        Optional<TimeSeries<Bar>> actualSeriesOptional = barifier.getSeries (Symbol.VOB30);
        assertTrue (actualSeriesOptional.isPresent ());

        TimeSeries<Bar> timeSeries = actualSeriesOptional.get ();
        assertEquals (1.4, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.OPEN), 0);
        assertEquals (2, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.HIGH), 0);
        assertEquals (0.1, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.LOW), 0);
        assertEquals (.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.CLOSE), 0);

        assertEquals (2.4, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.OPEN), 0);
        assertEquals (7.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.HIGH), 0);
        assertEquals (1.3, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.LOW), 0);
        assertEquals (7.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.CLOSE), 0);


    }

}