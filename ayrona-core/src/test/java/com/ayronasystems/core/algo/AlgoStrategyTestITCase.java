package com.ayronasystems.core.algo;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.backtest.PositionGenerator;
import com.ayronasystems.core.data.GrowingStrategyOHLC;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.SlidingStrategyOHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 15/06/16.
 */
public class AlgoStrategyTestITCase {

    private MarketDataService mds = StandaloneMarketDataService.getInstance ();

    private MarketData liveMarketData;

    private MarketData initialMarketData;

    private MarketData allMarketData;

    private Algo algo;

    private PositionGenerator positionGenerator;

    private List<Position> backTestPositions;

    private Account growingStrategyAccount;

    private Account slidingStrategyAccount;

    private AlgoStrategy slidingAlgoStrategy;

    private AlgoStrategy growingAlgoStrategy;

    Date date1 = DateUtils.parseDate ("23.11.2015 00:00:00");
    Date date2 = DateUtils.parseDate ("01.12.2015 00:00:00");
    Date date3 = DateUtils.parseDate ("11.12.2015 00:00:00");


    @Before
    public void setup () throws Exception {
        FunctionFactory.scanFunctions ();

        initialMarketData = mds.getOHLC (Symbol.VOB30, Period.M5, date1, date2);

        liveMarketData = mds.getOHLC (Symbol.VOB30, Period.M5, date2, date3);

        allMarketData = mds.getOHLC (Symbol.VOB30, Period.M5, date1, date3);

        algo = new FatihAlgo().toAlgo ();

        positionGenerator = new PositionGenerator (algo);

        growingStrategyAccount = new BasicAccount ("TEST2");

        slidingStrategyAccount = new BasicAccount ("TEST3");

        AccountBindInfo accountBindInfo2 = new AccountBindInfo (growingStrategyAccount, 1);

        AccountBindInfo accountBindInfo3 = new AccountBindInfo (slidingStrategyAccount, 1);

        slidingAlgoStrategy = new AlgoStrategy (true, "TEST1", algo, SlidingStrategyOHLC.valueOf (
                initialMarketData), Arrays.asList (accountBindInfo3), 0, 0);

        growingAlgoStrategy = new AlgoStrategy (false, "TEST3", algo, GrowingStrategyOHLC.valueOf (
                initialMarketData), Arrays.asList (accountBindInfo2), 0, 0);

        long start = System.currentTimeMillis ();
        for (Moment moment : liveMarketData ){
            Bar bar = (Bar)moment;
            slidingAlgoStrategy.process (bar);
        }
        long end = System.currentTimeMillis ();
        System.out.println ("Simulation Sliding: "+(end-start)+"ms");

        start = System.currentTimeMillis ();
        for (Moment moment : liveMarketData ){
            Bar bar = (Bar)moment;
            growingAlgoStrategy.process (bar);
        }
        end = System.currentTimeMillis ();
        System.out.println ("Simulation Growing: "+(end-start)+"ms");

        start = System.currentTimeMillis ();
        backTestPositions = positionGenerator.generate (allMarketData);
        end = System.currentTimeMillis ();
        System.out.println ("Backtest : "+(end-start)+"ms");


    }

    @Test
    public void compareMarketdataSizes(){
        assertEquals (allMarketData.size (), initialMarketData.size () + liveMarketData.size ());
    }

    @Test
    public void compareGrowingVSSliding(){
        List<Position> gpl = growingStrategyAccount.getPositions ();
        List<Position> spl = slidingStrategyAccount.getPositions ();

        assertEquals (gpl.size (), spl.size ());

        for ( int i = 0; i < gpl.size (); i++ ) {
            assertTrue (i+". pos", gpl.get (i).isSame (spl.get (i)));
        }
    }

    @Test
    public void compareGrowingVSBacktest(){
        List<Position> gpl = growingStrategyAccount.getPositions ();
        List<Position> btpl = new ArrayList<Position> ();
        for (Position position : backTestPositions){
            if (position.getOpenDate ().equals (date2) || position.getOpenDate ().after (date2)){
                btpl.add (position);
            }
        }

        assertEquals (gpl.size (), btpl.size ());

        for ( int i = 0; i < gpl.size (); i++ ) {
            Position btPos = btpl.get (i);
            Position gp = gpl.get (i);
            assertTrue (i+". pos", gp.isSame (btPos));
        }
    }

}