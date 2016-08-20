package com.ayronasystems.data;

import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.series.TimeSeries;
import com.ayronasystems.core.util.DateUtils;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 01/06/16.
 */
public class BarifierTest {

    private static final List<LiveTick> tickList = new ArrayList<LiveTick> (
            Arrays.asList (
                    new LiveTick[]{
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:00:00"), Symbols.of("TEST"), 0, 0, 1.4, false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:00:01"), Symbols.of("TEST"), 0, 0, 2,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:00:10"), Symbols.of("TEST"), 0, 0, 0.1,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:00:50"), Symbols.of("TEST"), 0, 0, 1.3,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:00:53"), Symbols.of("TEST"), 0, 0, .8,  false),

                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:01:01"), Symbols.of("TEST"), 0, 0, 2.4,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:01:03"), Symbols.of("TEST"), 0, 0, 5, false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:01:17"), Symbols.of("TEST"), 0, 0, 2.1,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:01:50"), Symbols.of("TEST"), 0, 0, 1.3,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:01:53"), Symbols.of("TEST"), 0, 0, 7.8,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:02:03"), Symbols.of("TEST"), 0, 0, 7.8,  true),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:02:03"), Symbols.of("TEST"), 0, 0, 7.8,  true),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:02:13"), Symbols.of("TEST"), 0, 0, 7.8,  true),

                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:03:03"), Symbols.of("TEST"), 0, 0, 1.11,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:03:13"), Symbols.of("TEST"), 0, 0, 5.11,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:03:23"), Symbols.of("TEST"), 0, 0, 2.11,  false),
                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:03:53"), Symbols.of("TEST"), 0, 0, 3.11,  false),

                            new LiveTick (DateUtils.parseDate ("01.01.2016 01:04:03"), Symbols.of("TEST"), 0, 7.8, 0,  true)

                    }
            )
    );
    @Test
    public void newTick () throws Exception {
        Barifier barifier = new Barifier (Period.M1);

        for (LiveTick tick : tickList){
            barifier.newTick (tick);
        }

        Optional<TimeSeries<Bar>> actualSeriesOptional = barifier.getSeries (Symbols.of("TEST"));
        assertTrue (actualSeriesOptional.isPresent ());

        TimeSeries<Bar> timeSeries = actualSeriesOptional.get ();
        assertEquals (3, timeSeries.size ());
        assertEquals (1.4, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.OPEN), 0);
        assertEquals (2, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.HIGH), 0);
        assertEquals (0.1, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.LOW), 0);
        assertEquals (.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:00:00")).get (ColumnDefinition.CLOSE), 0);

        assertEquals (2.4, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.OPEN), 0);
        assertEquals (7.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.HIGH), 0);
        assertEquals (1.3, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.LOW), 0);
        assertEquals (7.8, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:01:00")).get (ColumnDefinition.CLOSE), 0);

        assertEquals (1.11, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:03:00")).get (ColumnDefinition.OPEN), 0);
        assertEquals (5.11, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:03:00")).get (ColumnDefinition.HIGH), 0);
        assertEquals (1.11, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:03:00")).get (ColumnDefinition.LOW), 0);
        assertEquals (3.11, timeSeries.getMoment (DateUtils.parseDate ("01.01.2016 01:03:00")).get (ColumnDefinition.CLOSE), 0);


    }

}