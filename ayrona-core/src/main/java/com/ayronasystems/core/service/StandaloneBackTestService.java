package com.ayronasystems.core.service;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.algo.Algo;
import com.ayronasystems.core.backtest.MarketSimulator;
import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.backtest.BackTestCalculator;
import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.backtest.PositionGenerator;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.SignalGenerator;
import com.ayronasystems.core.util.DateUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Date;
import java.util.List;

/**
 * Created by gorkemgok on 21/05/16.
 */
public class StandaloneBackTestService implements BackTestService{

    private MarketDataService marketDataService = StandaloneMarketDataService.getInstance ();

    public BackTestResult doSimulationBackTest (String code, Symbol symbol, Period period, Date startDate, Date endDate) throws PrerequisiteException{
        MarketData marketData = marketDataService.getOHLC (symbol, period);
        MarketSimulator marketSimulator = new MarketSimulator (code, marketData);
        BackTestCalculator calculator = new BackTestCalculator ();
        Account account = marketSimulator.simulate ();
        return  calculator.calculate (account.getPositions (), marketData);
    }

    public BackTestResult doBackTest (String code, Symbol symbol, Period period, Date startDate, Date endDate) throws PrerequisiteException {
        SignalGenerator signalGenerator = Algo.createInstance (code);
        return doBackTest (signalGenerator, symbol, period, startDate, endDate);
    }

    public BackTestResult doBackTest (SignalGenerator signalGenerator, Symbol symbol, Period period, Date startDate, Date endDate) throws PrerequisiteException{
        MarketData ohlc = marketDataService.getOHLC (symbol, period);

        PositionGenerator positionGenerator = new PositionGenerator (signalGenerator, ohlc);
        BackTestCalculator calculator = new BackTestCalculator ();
        List<Position> positionList = positionGenerator.generate (ohlc);
        BackTestResult result = calculator.calculate (positionList, ohlc);

        StringBuilder sb = new StringBuilder ();
        for (Position position : positionList){
            sb.append (DateUtils.formatDate (position.getOpenDate ())).append ("\t")
              .append (position.getOpenPrice ()).append ("\t")
              .append (position.getCloseDate () != null ? DateUtils.formatDate (position.getCloseDate ()) : "").append ("\t")
              .append (position.getClosePrice ()).append ("\t")
              .append (position.calculateProfit ()).append ("\n");
        }
        StringSelection selec= new StringSelection (sb.toString ());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selec, selec);
        System.out.println ("Positions copied to clipboard");

        return result;
    }

    public boolean doBackTestAndSave (SignalGenerator signalGenerator, Symbol symbol, Period period, Date startDate, Date endDate) throws PrerequisiteException{
        BackTestResult result = doBackTest (signalGenerator, symbol, period, startDate, endDate);
        return false;
    }
}
