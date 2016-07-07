package com.ayronasystems.core.instant;

import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.tree.FunctionNode;
import com.ayronasystems.core.algo.tree.MarketDataNode;
import com.ayronasystems.core.data.GrowingStrategyOHLC;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;
import com.ayronasystems.core.util.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 30/05/16.
 */
public class InstantSARTest {

    private static final int SAR_PER = 30;

    public static void main(String[] args) throws CorruptedMarketDataException {
        FunctionFactory.scanFunctions ();
        MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();
        MarketData marketData = marketDataService.getOHLC (
                Symbol.VOB30, Period.M5,
                DateUtils.parseDate ("01.01.2015 00:00:00"),
                DateUtils.parseDate ("01.01.2016 00:00:00")
        );
        StrategyOHLC strategyMarketData = GrowingStrategyOHLC.valueOf (marketData.subData (0 , SAR_PER));
        MarketData simMarketData = marketData.subData (SAR_PER, marketData.size () - 1);

        FunctionNode fn = new FunctionNode("SAR",
                                           Arrays.asList (new MarketDataNode (PriceColumn.HIGH), new MarketDataNode(PriceColumn.LOW)),
                                           new double[]{0.02, .02});

        try {
            FIOExchange fioExchange = fn.calculate (marketData);
            double[] sarResult = fioExchange.getData (0);
            List<Double> simSarResult = new ArrayList<Double> ();
            for ( Moment moment : simMarketData){
                Bar bar = (Bar)moment;
                strategyMarketData.overwriteLastBar (bar);
                FIOExchange sfio = fn.calculate (strategyMarketData);
                double[] d = sfio.getData (0);
                simSarResult.add (d[d.length - 1]);
                strategyMarketData.prepareForNextData ();
            }
            int c = Math.min (sarResult.length, simSarResult.size ());
            int diff = Math.abs (sarResult.length - simSarResult.size ());
            System.out.println (sarResult.length+", "+ simSarResult.size ());
            int totalDiff = 0;
            for ( int i = 0; i < c; i++ ) {
                System.out.println (sarResult[i + diff]+", "+simSarResult.get (i));
                totalDiff += Math.abs (sarResult[i + diff]-simSarResult.get (i));
            }
            System.out.print (totalDiff);
        } catch ( PrerequisiteException e ) {
            e.printStackTrace ();
        }

    }

}
