package com.ayronasystems.core.algo;

import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.account.BasicAccount;
import com.ayronasystems.core.backtest.PositionGenerator;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.OHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 15/06/16.
 */
public class AlgoStrategyTest {

    private MarketDataService mds = StandaloneMarketDataService.getInstance ();

    private MarketData marketData;

    private SignalGenerator signalGenerator;

    private PositionGenerator positionGenerator;

    private Account account1;

    private Account account2;

    private AlgoStrategy slidingAlgoStrategy;

    private AlgoStrategy growingAlgoStrategy;

    @Before
    public void setup () throws Exception {
        FunctionFactory.scanFunctions ();

        marketData = mds.getOHLC (Symbol.VOB30, Period.M5,
                                DateUtils.parseDate ("01.12.2015 00:00:00"),
                                DateUtils.parseDate ("11.12.2015 00:00:00"));
        signalGenerator = new FatihAlgo();

        positionGenerator = new PositionGenerator (signalGenerator, marketData);

        account1 = new BasicAccount ("TEST1");

        account2 = new BasicAccount ("TEST2");

        AccountBindInfo accountBindInfo1 = new AccountBindInfo (account1, 1);

        AccountBindInfo accountBindInfo2 = new AccountBindInfo (account2, 1);

        slidingAlgoStrategy = new AlgoStrategy (true, "TEST1", signalGenerator, OHLC.getEmptyData (Symbol.VOB30, Period.M5), Arrays.asList (accountBindInfo1), 0, 0);

        growingAlgoStrategy = new AlgoStrategy (false, "TEST2", signalGenerator, OHLC.getEmptyData (Symbol.VOB30, Period.M5), Arrays.asList (accountBindInfo2), 0, 0);

    }

    @Test
    public void test1 () throws Exception {

        long start = System.currentTimeMillis ();
        for (Moment moment : marketData){
            Bar bar = (Bar)moment;
            slidingAlgoStrategy.process (bar);
        }
        long end = System.currentTimeMillis ();
        System.out.println ("Simulation Sliding: "+(end-start)+"ms");

        List<Position> slidingSimulationPositions = account1.getPositions ();


        start = System.currentTimeMillis ();
        for (Moment moment : marketData){
            Bar bar = (Bar)moment;
            growingAlgoStrategy.process (bar);
        }
        end = System.currentTimeMillis ();
        System.out.println ("Simulation Growing: "+(end-start)+"ms");

        List<Position> growingSimulationPositions = account2.getPositions ();

        start = System.currentTimeMillis ();
        List<Position> backTestPositions = positionGenerator.generate (marketData);
        end = System.currentTimeMillis ();
        System.out.println ("Simulation : "+(end-start)+"ms");

        assertEquals(backTestPositions.size (), slidingSimulationPositions.size ());
        assertEquals(backTestPositions.size (), growingSimulationPositions.size ());

        for ( int i = 0; i < backTestPositions.size (); i++ ) {
            assertTrue (backTestPositions.get (i).isSame (slidingSimulationPositions.get (i)));
            assertTrue (backTestPositions.get (i).isSame (growingSimulationPositions.get (i)));
        }
    }

}