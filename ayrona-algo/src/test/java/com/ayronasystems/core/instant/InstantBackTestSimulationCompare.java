package com.ayronasystems.core.instant;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.data.GrowingStrategyOHLC;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.data.StrategyOHLC;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Signal;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.CorruptedMarketDataException;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.service.MarketDataService;
import com.ayronasystems.core.service.StandaloneMarketDataService;
import com.ayronasystems.core.strategy.BasicOrderGenerator;
import com.ayronasystems.core.strategy.OrderGenerator;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.moment.Moment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 29/05/16.
 */
public class InstantBackTestSimulationCompare {

    public static void main(String[] args){
        FunctionFactory.scanFunctions ();
        String code =
                "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                "var SMA_5 = Sistem.SMA(Sistem.C, 5);" +
                "var SAR = Sistem.SAR(0.02, 0.2);" +
                "Sistem.BUY = Sistem.GT(SMA_5, SMA_20);" +
                "Sistem.SELL = Sistem.LT(SMA_5, SAR);";
        SignalGenerator signalGenerator = Algo.createInstance (code);
        OrderGenerator btOrderGenerator = new BasicOrderGenerator ();
        OrderGenerator simOrderGenerator = new BasicOrderGenerator ();
        int neededInputCount = signalGenerator.getNeededInputCount ();
        MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();
        MarketData baseMarketData = marketDataService.getOHLC (Symbol.VOB30, Period.M5);
        MarketData initialMarketData = baseMarketData.subData (0, neededInputCount - 1);
        MarketData simulationMarketData = baseMarketData.subData (neededInputCount - 1, baseMarketData.getDataCount () - 1);

        try {
            List<Signal> btSignals = signalGenerator.getSignalList (baseMarketData);
            List<Order> btOrders = btOrderGenerator.process (baseMarketData, btSignals);
            List<Order> simOrders = new ArrayList<Order> ();
            StrategyOHLC strategyOHLC = GrowingStrategyOHLC.valueOf (initialMarketData);
            List<Signal> simSignals = new ArrayList();
            for ( Moment moment: simulationMarketData) {
                Bar bar = (Bar)moment;
                strategyOHLC.overwriteLastBar (bar);
                List<Signal> signals = signalGenerator.getSignalList (strategyOHLC);
                List<Order> orders = simOrderGenerator.process (strategyOHLC, signals);
                for (Signal signal : signals){
                    simSignals.add (signal);
                }
                for (Order order : orders){
                    simOrders.add (order);
                }
                strategyOHLC.prepareForNextData ();
            }
            System.out.println (btOrders.size ());
            System.out.println (simOrders.size ());
            int c = Math.min (btOrders.size (), simOrders.size ());
            for ( int i = c - 1; i > -1; i-- ) {
                //System.out.println (btOrders.get (i)+"\t"+simOrders.get (i));
            }
            System.out.println (btSignals.size ());
            System.out.println (simSignals.size ());

            if (btSignals.size () == simSignals.size ()){
                int size = btSignals.size ();
                for ( int i = 0; i < size; i++ ) {
                    if (btSignals.get (i) != simSignals.get (i))
                        System.out.println (i+"."+btSignals.get (i)+","+simSignals.get (i));
                }
            }

        } catch ( PrerequisiteException e ) {
            e.printStackTrace ();
        } catch ( CorruptedMarketDataException e ) {
            e.printStackTrace ();
        }
    }
}
