package com.ayronasystems.core.instant;

import com.ayronasystems.algo.AlgoTradingEngine;
import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.AccountBinder;
import com.ayronasystems.core.dao.model.StrategyModel;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import org.mozilla.javascript.EcmaError;

import javax.jms.JMSException;
import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class InstantAlgoTETest {

    public static void main(String[] args){
        FunctionFactory.scanFunctions();
        Dao dao = Singletons.INSTANCE.getDao();
        List<StrategyModel> strategyModelList = dao.findAllStrategies();
        if (strategyModelList.isEmpty()) {
            StrategyModel strategyModel = new StrategyModel();
            strategyModel.setName("Test");
            strategyModel.setSymbol(Symbol.EURUSD);
            strategyModel.setPeriod(Period.M5);
            strategyModel.setState(AccountBinder.State.ACTIVE);
            strategyModel.setCode(
                    "Sistem.NAME='Tests Algo Strategy';" +
                            "var SMA_5 = Sistem.SMA(Sistem.O,5);"+
                            "var SMA_20 = Sistem.SMA(Sistem.C,20);" +
                            "Sistem.BUY = Sistem.LT(Sistem.C, SMA_5);"+
                            "Sistem.SELL = Sistem.GT(Sistem.C, SMA_20);"
            );
            dao.createStrategy(strategyModel);
        }

        AlgoTradingEngine algo = new AlgoTradingEngine();
        try {
            algo.init();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (EcmaError e){
            System.out.println(e.getErrorMessage());
            System.out.println(e.details());
            System.exit(2);
        }
    }
}
