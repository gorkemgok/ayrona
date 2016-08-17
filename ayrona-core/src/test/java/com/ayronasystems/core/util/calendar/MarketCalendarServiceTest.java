package com.ayronasystems.core.util.calendar;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.MarketCalendarModel;
import com.ayronasystems.core.dao.mongo.MongoDaoTestITCase;
import com.ayronasystems.core.definition.Symbols;
import com.ayronasystems.core.util.DateUtils;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 16/08/16.
 */
public class MarketCalendarServiceTest {

    private static final String TEST_INCLUDE_1 = "01.01.2015-01.01.2016 09:10-17:45:59";
    private static final String TEST_EXCLUDE_1 = "01.01.2015-01.01.2016 13:00-14:00";
    private static final String TEST_HOLIDAY_1 = "05.01.2015-07.01.2015 00:00-23:59";

    private static final String TEST_INCLUDE_2 = "01.01.2015-01.01.2016 17:45-23:45";
    private static final String TEST_EXCLUDE_2 = "01.01.2015-01.01.2016 19:00-20:00";
    private static final String TEST_HOLIDAY_2 = "05.01.2015-07.01.2015 17:45-19:00";

    private static final String SYM_1 = "TEST_SYM_1";
    private static final String SYM_2 = "TEST_SYM_2";
    private static final String SYM_3 = "TEST_SYM_3";

    private static final String[] SYMBOL_ARR_1 = new String []{SYM_1, SYM_2};
    private static final String[] SYMBOL_ARR_2 = new String []{SYM_3};

    private static final String NAME_1 = "SrvTest1";
    private static final String NAME_2 = "SrvTest2";

    private MarketCalendarService mcs;

    public static final String AYRONA_TEST_DB_NAME = "test_ayrona";

    private Dao dao;

    @Before
    public void setUp () throws Exception {
        System.setProperty (ConfigurationConstants.PROP_MONGODB_DS, AYRONA_TEST_DB_NAME);
        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (AYRONA_TEST_DB_NAME);

        dao = Singletons.INSTANCE.getDao ();

        mcs = Singletons.INSTANCE.getMarketCalendarService ();

    }

    @Test
    public void isMarketOpen () throws Exception {
        MarketCalendarModel marketCalendarModel1 = MongoDaoTestITCase.createSampleMarketCalendarModel (NAME_1, TEST_INCLUDE_1, TEST_EXCLUDE_1, TEST_HOLIDAY_1, SYMBOL_ARR_1);
        MarketCalendarModel marketCalendarModel2 = MongoDaoTestITCase.createSampleMarketCalendarModel (NAME_2, TEST_INCLUDE_2, TEST_EXCLUDE_2, TEST_HOLIDAY_2, SYMBOL_ARR_2);

        dao.createMarketCalendar (marketCalendarModel1);
        dao.createMarketCalendar (marketCalendarModel2);

        List<MarketCalendarModel> marketCalendarModelList = dao.findAllMarketCalendars ();

        MarketCalendarModel actualMC1 = null;
        MarketCalendarModel actualMC2 = null;
        for(MarketCalendarModel marketCalendarModel : marketCalendarModelList){
            if (marketCalendarModel.getName ().equals (NAME_1)){
                actualMC1 = marketCalendarModel;
            }else if (marketCalendarModel.getName ().equals (NAME_2)){
                actualMC2 = marketCalendarModel;
            }
        }
        Assert.assertNotNull (actualMC1);
        Assert.assertNotNull (actualMC2);

        mcs.reload ();

        long start = System.currentTimeMillis ();
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("01.01.2015 09:10:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("02.01.2015 17:40:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("01.01.2015 17:45:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("07.02.2015 10:45:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("02.01.2015 13:40:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("02.01.2015 14:00:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("05.01.2015 09:10:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("06.01.2015 17:40:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_1), DateUtils.parseDate ("06.01.2015 23:59:00")));

        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("01.01.2015 09:10:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("02.01.2015 17:40:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("01.01.2015 09:09:59")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("02.01.2015 13:40:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("02.01.2015 14:00:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("05.01.2015 09:10:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_2), DateUtils.parseDate ("06.01.2015 17:40:00")));

        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("01.01.2015 17:44:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("01.01.2015 17:45:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("02.01.2015 23:40:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("01.01.2015 19:10:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("01.01.2015 20:00:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("05.01.2015 17:45:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("05.01.2015 20:10:00")));
        assertEquals (false, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("06.01.2015 18:50:00")));
        assertEquals (true, mcs.isMarketOpen (Symbols.of (SYM_3), DateUtils.parseDate ("06.01.2015 20:00:00")));
        long end = System.currentTimeMillis ();
        System.out.println((end-start)+"ms");

    }

}