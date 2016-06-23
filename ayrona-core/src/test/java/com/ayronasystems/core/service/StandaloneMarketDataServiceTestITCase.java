package com.ayronasystems.core.service;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.batchjob.impl.ImportMarketDataBatchJob;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.mongo.MongoDaoTestITCase;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.ColumnDefinition;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 15/06/16.
 */
public class StandaloneMarketDataServiceTestITCase {
    public static final Bar[] BARS = {
            new Bar(DateUtils.parseDate("01.01.2016 01:00:00"), 113676, 113683, 113667, 113682, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:05:00"), 113681, 113771, 113670, 113764, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:10:00"), 113742, 113781, 113731, 113773, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:15:00"), 113775, 113776, 113738, 113749, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:20:00"), 113749, 113753, 113740, 113750, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:25:00"), 113749, 113768, 113749, 113763, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:30:00"), 113760, 113763, 113758, 113762, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:35:00"), 113763, 113792, 113761, 113791, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:40:00"), 113791, 113791, 113770, 113780, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:45:00"), 113779, 113782, 113777, 113779, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:50:00"), 113783, 113786, 113766, 113767, 0),
            new Bar(DateUtils.parseDate("01.01.2016 01:55:00"), 113767, 113772, 113764, 113772, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:00:00"), 113769, 113769, 113757, 113759, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:05:00"), 113757, 113771, 113757, 113771, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:10:00"), 113769, 113773, 113768, 113770, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:15:00"), 113771, 113791, 113770, 113779, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:20:00"), 113780, 113811, 113778, 113811, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:25:00"), 113810, 113868, 113810, 113848, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:30:00"), 113847, 113880, 113845, 113870, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:35:00"), 113871, 113882, 113862, 113867, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:40:00"), 113867, 113897, 113863, 113880, 0),
            new Bar(DateUtils.parseDate("01.01.2016 02:45:00"), 113879, 113897, 113876, 113895, 0)
    };


    private Dao dao;

    private MarketDataService mds;

    @Before
    public void setup(){
        String mdsString = "ayrona_marketdatacache_test";
        System.setProperty (ConfigurationConstants.PROP_MONGODB_MDS, mdsString);

        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (mdsString);

        mds = StandaloneMarketDataService.getInstance ();

        ImportMarketDataBatchJob ibj = new ImportMarketDataBatchJob("VOB30", Arrays.asList(BARS), mongoClient, 1);
        ibj.setCallback(BatchJobManager.NO_OP_CALLBACK);
        ibj.run();
    }

    @Test
    public void getOHLC(){
        MarketData marketData = mds.getOHLC(
                Symbol.VOB30, Period.M5,
                BARS[4].getDate(),
                BARS[12].getDate()
        );
        assertBars(marketData, 4, 11);

        marketData = mds.getOHLC(
                Symbol.VOB30, Period.M5,
                BARS[7].getDate(),
                BARS[20].getDate()
        );

        assertBars(marketData, 7, 19);
    }

    private void assertBars(MarketData marketData, int beginIdx, int endIdx){
        assertEquals(endIdx-beginIdx+1, marketData.size());
        int j = 0;
        for (int i = beginIdx; i <= endIdx; i++) {
            assertEquals(BARS[i].getDate(), marketData.getDate(j));
            assertEquals(BARS[i].get(ColumnDefinition.OPEN), marketData.getPrice(PriceColumn.OPEN, j), 0);
            assertEquals(BARS[i].get(ColumnDefinition.HIGH), marketData.getPrice(PriceColumn.HIGH, j), 0);
            assertEquals(BARS[i].get(ColumnDefinition.LOW), marketData.getPrice(PriceColumn.LOW, j), 0);
            assertEquals(BARS[i].get(ColumnDefinition.CLOSE), marketData.getPrice(PriceColumn.CLOSE, j), 0);
            j++;
        }
    }

    @Test
    public void fetchBarsInCorrectOrder () throws Exception {
        Date beginDate = DateUtils.parseDate ("01.01.2015 10:00:00");
        Date endDate = DateUtils.parseDate ("01.01.2016 20:00:00");
        MarketData ohlc = mds.getOHLC (Symbol.VOB30, Period.M5,
                                       beginDate,
                                       endDate);

        assertTrue(beginDate.before (ohlc.getBeginningDate ()) || beginDate.equals (ohlc.getBeginningDate ()));
        Date lastDate = null;
        for (Moment moment : ohlc){
            Date date = moment.getDate ();
            if (lastDate != null){
                long diff = date.getTime () - lastDate.getTime ();
                assertTrue (diff >= Period.M5.getAsMillis ());
            }
            lastDate = date;
        }

    }

}