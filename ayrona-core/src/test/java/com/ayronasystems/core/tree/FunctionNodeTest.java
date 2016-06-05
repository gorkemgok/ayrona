package com.ayronasystems.core.tree;

import com.ayronasystems.core.data.TestData;
import com.ayronasystems.core.data.OHLC;
import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.tree.FunctionNode;
import com.ayronasystems.core.algo.tree.MarketDataNode;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.exception.PrerequisiteException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class FunctionNodeTest {

    private OHLC ohlc;

    @Before
    public void setUp(){
        FunctionFactory.scanFunctions ();
        ohlc = TestData.OHLC;

    }

    @Test
    public void neededInputCountIsCorrectAndCalcCorrectWMA_WMA_5_4() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("WMA",
                                  new FunctionNode ("WMA", new MarketDataNode (PriceColumn.CLOSE), new double[]{5}),
                                  new double[]{4}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (8, nc);
        FIOExchange output = functionNode.calculate (ohlc);
        assertData (new double[][]{TestData.WMA_WMA_C_5_4}, output.getData (), 1);
    }

    @Test
    public void calcCorrectEMA_EMA_10_10() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("EMA",
                                  new FunctionNode ("EMA",
                                                    new FunctionNode ("EMA", new MarketDataNode (PriceColumn.OPEN), new double[]{10}),
                                                    new double[]{10}),
                                  new double[]{10}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (28, nc);
        FIOExchange output = functionNode.calculate (ohlc);
        assertData (new double[][]{TestData.EMA_EMA_EMA_O_10}, output.getData (), 1);
    }

    @Test
    public void neededInputCountIsCorrectWMA_RSI_2_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("WMA",
                                  new FunctionNode ("RSI", new MarketDataNode (PriceColumn.CLOSE), new double[]{2}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (4, nc);
    }

    @Test
    public void neededInputCountIsCorrectRSI_WMA_2_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("RSI",
                                  new FunctionNode ("WMA", new MarketDataNode (PriceColumn.CLOSE), new double[]{2}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (4, nc);
    }

    @Test
    public void neededInputCountIsCorrectRSI_RSI_2_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("RSI",
                                  new FunctionNode ("RSI", new MarketDataNode (PriceColumn.CLOSE), new double[]{2}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (5, nc);
    }

    @Test
    public void neededInputCountIsCorrectRSI_RSI_RSI_2_2_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("RSI",
                                  new FunctionNode ("RSI",
                                                    new FunctionNode ("RSI", new MarketDataNode (PriceColumn.CLOSE),
                                                                      new double[]{2}), new double[]{2}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (7, nc);
    }

    @Test
    public void neededInputCountIsCorrectRSI_WMA_RSI_2_3_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("RSI",
                                  new FunctionNode ("WMA",
                                                    new FunctionNode ("RSI", new MarketDataNode (PriceColumn.CLOSE),
                                                                      new double[]{2}), new double[]{3}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (7, nc);
    }

    @Test
    public void neededInputCountIsCorrectRSI_WMA_RSI_RSI_2_3_2_2() throws PrerequisiteException {
        FunctionNode functionNode =
                new FunctionNode ("RSI",
                                  new FunctionNode ("WMA",
                                                    new FunctionNode ("RSI", new FunctionNode ("RSI", new MarketDataNode (PriceColumn.CLOSE),
                                                                                               new double[]{2}),
                                                                      new double[]{2}), new double[]{3}),
                                  new double[]{2}
                );
        int nc = functionNode.getNeededInputCount ();

        assertEquals (9, nc);
    }

    @Test
    public void calculateFunction(){
        FunctionNode fn = new FunctionNode ("SMA",
                                            new FunctionNode ("TSF", new MarketDataNode (PriceColumn.CLOSE), new double[]{2}),
                                            new double[]{2}
        );

        try {
            fn.checkPrerequisite (true);
            fn.calculate (ohlc);
        } catch ( PrerequisiteException e ) {
            e.printStackTrace ();
        }
    }

    private void assertData(double[][] expectedData, double[][] actualData, double tolerance){
        assertNotNull (expectedData);
        assertNotNull (actualData);

        assertEquals ("Data Count",expectedData.length, actualData.length);

        for ( int i = 0; i < actualData.length; i++ ) {
            assertEquals ("Data Size for "+i,expectedData[i].length, actualData[i].length);
            for ( int j = 0; j < actualData[i].length; j++ ) {
                assertEquals ("Value for "+i+", "+j, expectedData[i][j], actualData[i][j], tolerance);
            }
        }
    }

}