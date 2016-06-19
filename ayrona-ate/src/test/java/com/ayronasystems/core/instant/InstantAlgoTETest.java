package com.ayronasystems.core.instant;

import com.ayronasystems.ate.AlgoTradingEngine;
import com.ayronasystems.core.algo.FunctionFactory;
import org.mozilla.javascript.EcmaError;

import javax.jms.JMSException;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class InstantAlgoTETest {

    public static void main(String[] args){
        FunctionFactory.scanFunctions();
        System.setProperty ("ayrona.fake.today", "01.12.2015");
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
