package com.ayronasystems.core.service;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.*;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.Position;
import com.ayronasystems.core.strategy.SignalGenerator;

import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 21/05/16.
 */
public class StandaloneBackTestService implements BackTestService{

    private MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();

    public BackTestResult doSimulationBackTest (String code, Symbol symbol, Period period, Date startDate, Date endDate) throws PrerequisiteException{
        MarketData marketData = marketDataService.getOHLC (symbol, period, startDate, endDate);
        MarketSimulator marketSimulator = new MarketSimulator (code, marketData);
        BackTestCalculator calculator = new BasicBackTestCalculator ();
        Account account = marketSimulator.simulate ();
        return  calculator.calculate (account.getPositions (), marketData);
    }

    public BackTestResult doBackTest (String code, Symbol symbol, Period period, Date startDate, Date endDate, boolean isDetailed) throws PrerequisiteException {
        SignalGenerator signalGenerator = Algo.createInstance (code);
        return doBackTest (signalGenerator, symbol, period, startDate, endDate, isDetailed);
    }

    public BackTestResult doBackTest (SignalGenerator signalGenerator, Symbol symbol, Period period, Date startDate, Date endDate, boolean isDetailed) throws PrerequisiteException{
        MarketData ohlc = marketDataService.getOHLC (symbol, period, startDate, endDate);
        PositionGenerator positionGenerator = new PositionGenerator (signalGenerator);
        BackTestCalculator calculator;
        if (isDetailed) {
            calculator = new PeriodicBackTestCalculator ();
        }else{
            calculator = new BasicBackTestCalculator ();
        }
        List<Position> positionList = positionGenerator.generate (ohlc);
        BackTestResult result = calculator.calculate (positionList, ohlc);
        return result;
    }
}
