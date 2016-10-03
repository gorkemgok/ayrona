package com.ayronasystems.core.service;

import com.ayronasystems.core.backtest.BackTestResult;
import com.ayronasystems.core.definition.Period;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.exception.PrerequisiteException;
import com.ayronasystems.core.strategy.SignalGenerator;

import java.util.Date;

/**
 * Created by gorkemgok on 21/05/16.
 */
public interface BackTestService {

    BackTestResult doSimulationBackTest (String code, Symbol symbol, Period period, Date startDate, Date endDate)
            throws PrerequisiteException;

    BackTestResult doBackTest(String code, Symbol symbol, Period period, Date startDate, Date endDate, boolean isDetailed)
            throws PrerequisiteException;

    BackTestResult doBackTest(SignalGenerator signalGenerator, Symbol symbol, Period period, Date startDate, Date endDate, boolean isDetailed)
            throws PrerequisiteException;
}
