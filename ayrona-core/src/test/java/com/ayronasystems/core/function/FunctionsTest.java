package com.ayronasystems.core.function;

import com.ayronasystems.core.algo.FIOExchange;
import com.ayronasystems.core.algo.Function;
import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.algo.function.*;
import com.ayronasystems.core.algo.indicator.SAR;
import com.ayronasystems.core.algo.indicator.SMA;
import com.ayronasystems.core.algo.indicator.WMA;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.core.timeseries.series.BasicTimeSeries;
import com.ayronasystems.core.data.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 17/03/16.
 */
public class FunctionsTest {

    private static final Bar[] ERROR_BARS = {
            new Bar (new Date (), 1, 1, 1, 1, 1)
    };

    private static final Bar[] BARS = {
            new Bar (new Date (), 2.1, 1.2, 1.5, 3.4, 1.5),
            new Bar (new Date (), 1.1, 2.23, 2.3, 2.4, 2.5),
            new Bar (new Date (), 3.1, 1.2, 2.3, 2.4, 5.5),
            new Bar (new Date (), 4.1, 3.2, 1.3, 4.4, 4.5),
            new Bar (new Date (), 5.2, 5.2, 5.3, 5.4, 6.5)
    };

    private static final Bar[] BARS_LONG = {
            new Bar (new Date (), 3.1, 1.2, 2.3, 2.4, 5.5),
            new Bar (new Date (), 2.1, 1.2, 1.5, 3.4, 1.5),
            new Bar (new Date (), 1.1, 2.23, 2.3, 2.4, 2.5),
            new Bar (new Date (), 3.1, 1.2, 2.3, 2.4, 5.5),
            new Bar (new Date (), 4.1, 3.2, 1.3, 4.4, 4.5),
            new Bar (new Date (), 5.2, 5.2, 5.3, 5.4, 6.5),
    };

    private BasicTimeSeries<Bar> errorBaseSeries;

    private BasicTimeSeries<Bar> baseSeries;

    private BasicTimeSeries<Bar> longBaseSeries;

    private BasicTimeSeries<Bar> baseSeries1;

    @Before
    public void setUp () throws Exception {
        FunctionFactory.scanFunctions ();
        errorBaseSeries = BasicTimeSeries.getDynamicSizeInstance (Bar.class, BARS.length);
        for(Bar bar : ERROR_BARS ){
            errorBaseSeries.addMoment (bar);
        }

        baseSeries = BasicTimeSeries.getDynamicSizeInstance (Bar.class, BARS.length);
        for(Bar bar : BARS ){
            baseSeries.addMoment (bar);
        }

        longBaseSeries = BasicTimeSeries.getDynamicSizeInstance (Bar.class, BARS_LONG.length);
        for(Bar bar : BARS_LONG ){
            longBaseSeries.addMoment (bar);
        }

        baseSeries1 = BasicTimeSeries.getDynamicSizeInstance (Bar.class, TestData.BARS_1.length);
        for(Bar bar : TestData.BARS_1 ){
            baseSeries1.addMoment (bar);
        }
    }

    @After
    public void tearDown () throws Exception {

    }

    @Test
    public void calculateBARSINCE(){
        double[] conditionArr = new double[]{0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0};
        BARSINCE barsince = new BARSINCE ();
        FIOExchange fioExchange = barsince.calculate (new FIOExchange (conditionArr));
        double[] barsinceArr = fioExchange.getData (0);
        assertEquals (0, barsinceArr[0], 0);
        assertEquals (0, barsinceArr[1], 0);
        assertEquals (1, barsinceArr[2], 0);
        assertEquals (2, barsinceArr[3], 0);
        assertEquals (3, barsinceArr[4], 0);
        assertEquals (1, barsinceArr[5], 0);
        assertEquals (1, barsinceArr[6], 0);
        assertEquals (2, barsinceArr[7], 0);
        assertEquals (3, barsinceArr[8], 0);
        assertEquals (4, barsinceArr[9], 0);
        assertEquals (5, barsinceArr[10], 0);
    }

    @Test
    public void fnFactoryCreatesReturnsFunctionClass(){
        Function fn = FunctionFactory.getInstance ("WMA");
        assertEquals (fn.getClass (), WMA.class);
    }

    @Test
    public void calculateSAR(){
        double[] highSeries = new double[]{89.95,90.075,90.125,90.05,90.025,89.95,90.075,90.175,90.175,90.225,90.175,90.1,90.1,90.15,90.225,90.175,90.225,90.225,90.175,90.3,90.3,90.25,90.225,90.1,90,90.075,90.075,90.05,90.025,90.05,90.05,90.05,90.1,90.125,90.325,90.35,90.375,90.375,90.35,90.3,90.25,90.225,90.25,90.3,90.325,90.375,90.2,90.225,90.1,90.15,90.05,90.05,89.85,89.825,89.8,89.825,89.8,89.825,89.9,89.9,89.8,89.825,89.8,89.75,89.8,89.85,89.825,89.875,90.075,90.075,90.125,90.175,90.2,90.125,90.1,90.1,90.175,90.1,89.975,90.05,90,89.875,90.25,90.35,90.475,90.525,90.55,90.625,90.75,91.075,90.85,90.925,90.95,90.9,90.875,90.75,90.75,90.85,90.8,90.85,90.95,91,91.075,91.075,90.875,90.775,90.65,90.575,90.6,90.575,90.5,90.375,90.3,90.425,90.425,90.375,90.3,90.2,90.175,90.2,90.2,90.125,90.175,90.15,90.075,90.1,90.175,90.175,90.275,90.275,90.2,90.175,90.25,90.25,90.2,90.2,90.225,90.275,90.25,90.175,90.225,90.25,90.275,90.225,90.225,90.15,90.1,89.95,89.95,90,90.05,90.05,90.05,90.025,90,90.05,90.05,90,90.125,90.25,90.25,90.25,90.2,90.175,90.075,90.1,90.125,90.1,90.225,90.225,90.1,89.95,89.9,89.925,89.9,89.85,89.85,89.775,89.75,89.7,89.8,89.7,89.75,89.8,89.9,89.9,89.725,89.675,89.55,89.375,89.225,89.3,89,88.6,88.575,88.675,88.65,88.675,88.6,88.675,88.825,88.825,89.025,88.875,88.95,88.925,89.1,89.15,89.075,89.225,89.25,89.175,89.175,89.225,89.2,89.275,89.35,89.35,89.25,89.275,89.275,89.225,89.15,89,88.95,88.95,88.875,88.65,88.675,88.725,88.7,88.8,88.8,88.825,88.85,89,89.2,89.25,89.225,89.425,89.725,89.75,89.725,89.725,89.7,89.675,89.65,89.725,89.7,89.7,90,90.1,90,90,89.975,89.95,89.9,89.9,89.775,89.8,89.925,89.9,89.95,90.025,90.05,90.125,90.1,90.3,90.4,90.45,90.525,90.5,90.5,90.475,90.625,90.525,90.5,90.65,90.85,90.8,90.725,90.675,90.675,90.75,90.75,90.725,90.7,90.45,90.4,90.4,90.575,90.625,90.525,90.475,90.575,91.6,91.575,91.5,91.5};
        double[] lowSeries = new double[]{89.825,89.925,90.025,89.95,89.875,89.825,89.925,90.05,90.075,90.05,89.95,89.95,89.95,90.075,90.125,90.125,90.125,90.15,90.125,90.15,90.2,90.175,90.05,89.975,89.925,89.95,90,90,90,90.025,90.025,90.025,90.025,90.075,90.125,90.25,90.275,90.25,90.225,90.125,90.125,90.15,90.175,90.15,90.225,90.15,90.075,90.05,90.025,89.95,89.925,89.85,89.75,89.55,89.55,89.725,89.675,89.775,89.775,89.725,89.725,89.775,89.575,89.55,89.675,89.725,89.7,89.725,89.825,89.875,89.95,90.075,90.05,89.975,89.975,89.975,89.975,89.95,89.875,89.925,89.75,89.725,89.825,90.175,90.225,90.425,90.45,90.5,90.575,90.725,90.775,90.8,90.825,90.825,90.6,90.65,90.525,90.6,90.675,90.7,90.775,90.875,90.925,90.8,90.625,90.6,90.425,90.45,90.525,90.475,90.3,90.25,90.175,90.275,90.325,90.25,90.15,90.1,90.075,90.1,90.125,89.925,90.025,89.975,89.975,90.025,90.05,90.1,90.1,90.15,90.175,90.15,90.15,90.175,90.15,90.175,90.175,90.225,90.1,90.1,90.15,90.2,90.15,90.175,90.1,90.05,89.9,89.85,89.85,89.9,89.9,89.975,89.95,89.975,89.875,89.925,89.975,89.95,90,90.125,90.15,90.15,90.125,90.025,90.025,90.025,90.025,89.925,90.05,89.975,89.925,89.725,89.8,89.8,89.775,89.675,89.7,89.65,89.6,89.575,89.65,89.6,89.65,89.675,89.725,89.675,89.625,89.575,89.325,89.1,89.075,89.1,88.525,88.475,88.425,88.525,88.525,88.55,88.475,88.425,88.65,88.675,88.675,88.6,88.7,88.775,88.825,88.975,88.825,89.05,89.1,89.1,89.1,89.075,89.125,89.125,89.25,89.225,89.175,89.175,89.125,89.1,88.95,88.825,88.875,88.775,88.65,88.475,88.55,88.6,88.625,88.675,88.725,88.725,88.75,88.825,88.95,89.15,89.125,89.225,89.375,89.55,89.55,89.6,89.35,89.375,89.55,89.625,89.6,89.55,89.7,89.95,89.9,89.9,89.85,89.85,89.825,89.725,89.725,89.7,89.75,89.775,89.8,89.9,89.925,89.9,89.95,90.1,90.2,90.275,90.325,90.3,90.35,90.35,90.4,90.4,90.4,90.4,90.6,90.65,90.6,90.575,90.5,90.55,90.625,90.65,90.375,90.2,90.15,90.2,90.275,90.475,90.375,90.325,90.425,90.6,91.45,91.45,91.425};
        double[] expectedSarSeries = new double[]{90.126,89.925,90.125,90.125,90.123,89.825,89.826,89.826,89.828,89.83,89.831,89.833,89.834,89.836,89.837,89.839,89.841,89.842,89.845,89.848,89.85,89.853,89.856,89.858,89.861,89.864,89.866,89.869,89.871,89.874,89.877,89.879,89.882,89.885,89.89,89.896,89.901,89.907,89.913,89.918,89.924,89.929,89.934,89.94,89.945,89.95,89.955,90.375,90.374,90.372,90.369,90.364,90.356,90.348,90.34,90.332,90.324,90.317,90.309,90.301,90.294,90.286,90.279,90.272,90.265,90.257,90.25,90.243,90.236,90.23,90.223,90.216,90.209,90.203,90.196,90.19,90.183,90.177,90.171,90.165,89.55,89.551,89.555,89.56,89.568,89.578,89.59,89.606,89.63,89.653,89.676,89.698,89.72,89.742,89.763,89.784,89.805,89.825,89.845,89.865,89.884,89.903,89.922,89.941,89.959,89.977,89.994,90.011,90.028,90.045,90.062,90.078,90.094,90.11,90.125,91.075,91.073,91.069,91.065,91.061,91.054,91.048,91.041,91.034,91.027,91.021,91.014,91.008,91.001,90.995,90.988,90.982,90.976,90.969,90.963,90.957,90.951,90.945,90.938,90.932,90.926,90.92,90.914,90.908,90.902,90.894,90.884,90.874,90.863,90.853,90.843,90.833,90.823,90.814,90.804,90.795,90.785,90.776,90.767,90.757,90.748,90.739,90.73,90.722,90.713,90.704,90.696,90.687,90.679,90.671,90.659,90.648,90.637,90.626,90.613,90.6,90.584,90.567,90.547,90.527,90.508,90.49,90.471,90.453,90.436,90.419,90.402,90.378,90.347,90.314,90.282,90.233,90.18,90.124,90.07,90.017,89.966,89.917,89.869,89.823,89.778,89.735,89.693,89.652,89.613,89.575,89.538,89.503,89.468,89.435,89.402,89.371,89.341,89.312,88.425,88.427,88.429,88.431,88.432,88.434,88.436,88.438,88.44,88.442,88.443,88.445,88.447,88.449,88.451,88.452,88.454,88.456,88.458,88.46,88.461,88.463,88.465,88.467,88.47,88.478,88.488,88.498,88.508,88.518,88.528,88.538,88.548,88.557,88.567,88.581,88.599,88.617,88.635,88.653,88.67,88.687,88.704,88.721,88.737,88.754,88.77,88.786,88.802,88.817,88.836,88.854,88.877,88.904,88.935,88.97,89.004,89.038,89.07,89.108,89.144,89.18,89.218,89.264,89.308,89.351,89.393,89.434,89.474,89.512,89.55,89.586,89.621,89.656,89.689,89.722,89.753,89.784,89.814,89.843,89.896,89.947,89.996};

        double[] highSeries2 = new double[]{90.175,90.1,90.1,90.15,90.225,90.175,90.225,90.225,90.175,90.3,90.3,90.25,90.225,90.1,90,90.075,90.075,90.05,90.025,90.05,90.05,90.05,90.1,90.125,90.325,90.35,90.375,90.375,90.35,90.3,90.25,90.225,90.25,90.3,90.325,90.375,90.2,90.225,90.1,90.15,90.05,90.05,89.85,89.825,89.8,89.825,89.8,89.825,89.9,89.9,89.8,89.825,89.8,89.75,89.8,89.85,89.825,89.875,90.075,90.075,90.125,90.175,90.2,90.125,90.1,90.1,90.175,90.1,89.975,90.05,90,89.875,90.25,90.35,90.475,90.525,90.55,90.625,90.75,91.075,90.85,90.925,90.95,90.9,90.875,90.75,90.75,90.85,90.8,90.85,90.95,91,91.075,91.075,90.875,90.775,90.65,90.575,90.6,90.575,90.5,90.375,90.3,90.425,90.425,90.375,90.3,90.2,90.175,90.2,90.2,90.125,90.175,90.15,90.075,90.1,90.175,90.175,90.275,90.275,90.2,90.175,90.25,90.25,90.2,90.2,90.225,90.275,90.25,90.175,90.225,90.25,90.275,90.225,90.225,90.15,90.1,89.95,89.95,90,90.05,90.05,90.05,90.025,90,90.05,90.05,90,90.125,90.25,90.25,90.25,90.2,90.175,90.075,90.1,90.125,90.1,90.225,90.225,90.1,89.95,89.9,89.925,89.9,89.85,89.85,89.775,89.75,89.7,89.8,89.7,89.75,89.8,89.9,89.9,89.725,89.675,89.55,89.375,89.225,89.3,89,88.6,88.575,88.675,88.65,88.675,88.6,88.675,88.825,88.825,89.025,88.875,88.95,88.925,89.1,89.15,89.075,89.225,89.25,89.175,89.175,89.225,89.2,89.275,89.35,89.35,89.25,89.275,89.275,89.225,89.15,89,88.95,88.95,88.875,88.65,88.675,88.725,88.7,88.8,88.8,88.825,88.85,89,89.2,89.25,89.225,89.425,89.725,89.75,89.725,89.725,89.7,89.675,89.65,89.725,89.7,89.7,90,90.1,90,90,89.975,89.95,89.9,89.9,89.775,89.8,89.925,89.9,89.95,90.025,90.05,90.125,90.1,90.3,90.4,90.45,90.525,90.5,90.5,90.475,90.625,90.525,90.5,90.65,90.85,90.8,90.725,90.675,90.675,90.75,90.75,90.725,90.7,90.45,90.4,90.4,90.575,90.625,90.525,90.475,90.575,91.6,91.575,91.5,91.5};
        double[] lowSeries2 = new double[]{89.95,89.95,89.95,90.075,90.125,90.125,90.125,90.15,90.125,90.15,90.2,90.175,90.05,89.975,89.925,89.95,90,90,90,90.025,90.025,90.025,90.025,90.075,90.125,90.25,90.275,90.25,90.225,90.125,90.125,90.15,90.175,90.15,90.225,90.15,90.075,90.05,90.025,89.95,89.925,89.85,89.75,89.55,89.55,89.725,89.675,89.775,89.775,89.725,89.725,89.775,89.575,89.55,89.675,89.725,89.7,89.725,89.825,89.875,89.95,90.075,90.05,89.975,89.975,89.975,89.975,89.95,89.875,89.925,89.75,89.725,89.825,90.175,90.225,90.425,90.45,90.5,90.575,90.725,90.775,90.8,90.825,90.825,90.6,90.65,90.525,90.6,90.675,90.7,90.775,90.875,90.925,90.8,90.625,90.6,90.425,90.45,90.525,90.475,90.3,90.25,90.175,90.275,90.325,90.25,90.15,90.1,90.075,90.1,90.125,89.925,90.025,89.975,89.975,90.025,90.05,90.1,90.1,90.15,90.175,90.15,90.15,90.175,90.15,90.175,90.175,90.225,90.1,90.1,90.15,90.2,90.15,90.175,90.1,90.05,89.9,89.85,89.85,89.9,89.9,89.975,89.95,89.975,89.875,89.925,89.975,89.95,90,90.125,90.15,90.15,90.125,90.025,90.025,90.025,90.025,89.925,90.05,89.975,89.925,89.725,89.8,89.8,89.775,89.675,89.7,89.65,89.6,89.575,89.65,89.6,89.65,89.675,89.725,89.675,89.625,89.575,89.325,89.1,89.075,89.1,88.525,88.475,88.425,88.525,88.525,88.55,88.475,88.425,88.65,88.675,88.675,88.6,88.7,88.775,88.825,88.975,88.825,89.05,89.1,89.1,89.1,89.075,89.125,89.125,89.25,89.225,89.175,89.175,89.125,89.1,88.95,88.825,88.875,88.775,88.65,88.475,88.55,88.6,88.625,88.675,88.725,88.725,88.75,88.825,88.95,89.15,89.125,89.225,89.375,89.55,89.55,89.6,89.35,89.375,89.55,89.625,89.6,89.55,89.7,89.95,89.9,89.9,89.85,89.85,89.825,89.725,89.725,89.7,89.75,89.775,89.8,89.9,89.925,89.9,89.95,90.1,90.2,90.275,90.325,90.3,90.35,90.35,90.4,90.4,90.4,90.4,90.6,90.65,90.6,90.575,90.5,90.55,90.625,90.65,90.375,90.2,90.15,90.2,90.275,90.475,90.375,90.325,90.425,90.6,91.45,91.45,91.425};
        double[] expectedSarSeries2 = new double[]{89.831,89.833,89.834,89.836,89.837,89.839,89.841,89.842,89.845,89.848,89.85,89.853,89.856,89.858,89.861,89.864,89.866,89.869,89.871,89.874,89.877,89.879,89.882,89.885,89.89,89.896,89.901,89.907,89.913,89.918,89.924,89.929,89.934,89.94,89.945,89.95,89.955,90.375,90.374,90.372,90.369,90.364,90.356,90.348,90.34,90.332,90.324,90.317,90.309,90.301,90.294,90.286,90.279,90.272,90.265,90.257,90.25,90.243,90.236,90.23,90.223,90.216,90.209,90.203,90.196,90.19,90.183,90.177,90.171,90.165,89.55,89.551,89.555,89.56,89.568,89.578,89.59,89.606,89.63,89.653,89.676,89.698,89.72,89.742,89.763,89.784,89.805,89.825,89.845,89.865,89.884,89.903,89.922,89.941,89.959,89.977,89.994,90.011,90.028,90.045,90.062,90.078,90.094,90.11,90.125,91.075,91.073,91.069,91.065,91.061,91.054,91.048,91.041,91.034,91.027,91.021,91.014,91.008,91.001,90.995,90.988,90.982,90.976,90.969,90.963,90.957,90.951,90.945,90.938,90.932,90.926,90.92,90.914,90.908,90.902,90.894,90.884,90.874,90.863,90.853,90.843,90.833,90.823,90.814,90.804,90.795,90.785,90.776,90.767,90.757,90.748,90.739,90.73,90.722,90.713,90.704,90.696,90.687,90.679,90.671,90.659,90.648,90.637,90.626,90.613,90.6,90.584,90.567,90.547,90.527,90.508,90.49,90.471,90.453,90.436,90.419,90.402,90.378,90.347,90.314,90.282,90.233,90.18,90.124,90.07,90.017,89.966,89.917,89.869,89.823,89.778,89.735,89.693,89.652,89.613,89.575,89.538,89.503,89.468,89.435,89.402,89.371,89.341,89.312,88.425,88.427,88.429,88.431,88.432,88.434,88.436,88.438,88.44,88.442,88.443,88.445,88.447,88.449,88.451,88.452,88.454,88.456,88.458,88.46,88.461,88.463,88.465,88.467,88.47,88.478,88.488,88.498,88.508,88.518,88.528,88.538,88.548,88.557,88.567,88.581,88.599,88.617,88.635,88.653,88.67,88.687,88.704,88.721,88.737,88.754,88.77,88.786,88.802,88.817,88.836,88.854,88.877,88.904,88.935,88.97,89.004,89.038,89.07,89.108,89.144,89.18,89.218,89.264,89.308,89.351,89.393,89.434,89.474,89.512,89.55,89.586,89.621,89.656,89.689,89.722,89.753,89.784,89.814,89.843,89.896,89.947,89.996};

        SAR sar = new SAR();
        FIOExchange fioExchange = sar.calculate (new FIOExchange (highSeries, lowSeries), 0.002, 0.2);
        double[] actualSarSeries = fioExchange.getData (0);
        System.out.println("length:"+expectedSarSeries.length+", "+actualSarSeries.length);
        for ( int i = 4; i < actualSarSeries.length; i++ ) {
            assertEquals (expectedSarSeries[i-1], actualSarSeries[i], 0.01);
        }

        fioExchange = sar.calculate (new FIOExchange (highSeries2, lowSeries2), 0.002, 0.2);
        double[] actualSarSeries2 = fioExchange.getData (0);

        System.out.println("length:"+expectedSarSeries2.length+", "+actualSarSeries2.length);
        for ( int i = 35; i < actualSarSeries2.length; i++ ) {
            assertEquals (expectedSarSeries2[i-1], actualSarSeries2[i], 0.01);
            //System.out.println(expectedSarSeries2[i-1]+", "+actualSarSeries2[i]);
        }
    }

    @Test
    public void calculateX(){
        double[] in = new double[]{0, 1, 2, 3, 4};
        FIOExchange fioExchange = new FIOExchange (in);
        X X = new X();
        FIOExchange output = X.calculate (fioExchange, 3);
        double[] outputArr = output.getData (0);

        assertEquals (3, outputArr.length);
        assertEquals (2, outputArr[0], 0);
        assertEquals (3, outputArr[1], 0);
        assertEquals (4, outputArr[2], 0);
    }

    @Test
    public void calculateCROSS (){
        FIOExchange testInput = new FIOExchange (baseSeries.toArrays (0 , 1));

        assertEquals (2.23, testInput.getData (1, 1), 0);

        CROSS CROSS = new CROSS ();
        FIOExchange testOutput = CROSS.calculate (testInput, null);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length - 1, testOutput.getDataSize (0));
        assertEquals ("Value", 1, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 3), 0);
    }

    @Test
    public void calculateSMA(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArrays (0));

        SMA sma = new SMA();
        FIOExchange testOutput = sma.calculate (testInput, 2);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length - 1, testOutput.getDataSize (0));
        assertEquals ("Value", 1.6, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 2.1, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 3.6, testOutput.getData (0, 2), 0.01);
        assertEquals ("Value", 4.65, testOutput.getData (0, 3), 0.01);
    }

    @Test
    public void calculateMIN(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArrays (2));

        MIN min = new MIN ();
        FIOExchange testOutput = min.calculate (testInput, 2);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length - 1, testOutput.getDataSize (0));
        assertEquals ("Value", 1.5, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 2.3, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 1.3, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 1.3, testOutput.getData (0, 3), 0);
    }

    @Test
    public void calculateMAX(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArrays (3));

        MAX max = new MAX ();
        FIOExchange testOutput = max.calculate (testInput, 3);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length - 2, testOutput.getDataSize (0));
        assertEquals ("Value", 3.4, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 4.4, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 5.4, testOutput.getData (0, 2), 0);
    }

    @Test
    public void calculateLT(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArrays (1,2));

        LT lt = new LT();
        FIOExchange testOutput = lt.calculate (testInput);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length , testOutput.getDataSize (0));
        assertEquals ("Value", 1, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 3), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 4), 0);

    }

    @Test
    public void calculateGT(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArrays (1,2));

        GT gt = new GT();
        FIOExchange testOutput = gt.calculate (testInput);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length , testOutput.getDataSize (0));
        assertEquals ("Value", 0, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 3), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 4), 0);

    }

    @Test
    public void calculateLTwithDifferentSizeInputs(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArray (1), longBaseSeries.toArray (2));

        LT lt = new LT();
        FIOExchange testOutput = lt.calculate (testInput);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length , testOutput.getDataSize (0));
        assertEquals ("Value", 1, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 3), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 4), 0);

    }

    @Test
    public void calculateGTwithDifferentSizeInputs(){
        FIOExchange testInput = new FIOExchange(baseSeries.toArray (1), longBaseSeries.toArray (2));

        GT gt = new GT();
        FIOExchange testOutput = gt.calculate (testInput);

        Assert.assertNotNull (testOutput);

        assertEquals ("Output Size", 1, testOutput.getDataCount ());
        assertEquals ("Output Value Size", BARS.length , testOutput.getDataSize (0));
        assertEquals ("Value", 0, testOutput.getData (0, 0), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 1), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 2), 0);
        assertEquals ("Value", 1, testOutput.getData (0, 3), 0);
        assertEquals ("Value", 0, testOutput.getData (0, 4), 0);

    }

    @Test
    public void calculateWMA(){
        double[] controlData = TestData.WMA_C_5;
        int column = PriceColumn.CLOSE.getIndex ();
        int period = 5;
        assertSingleCalculation (FunctionFactory.getInstance ("WMA"), controlData, column, period);
    }

    @Test
    public void calculateEMA(){
        double[] controlData = TestData.EMA_O_10;
        int column = PriceColumn.OPEN.getIndex ();
        int period = 10;
        assertSingleCalculation (FunctionFactory.getInstance ("EMA"), controlData, column, period);
    }

    @Test
    public void calculateTRIX(){
        double[] controlData = TestData.EMA_O_10;
        int column = PriceColumn.OPEN.getIndex ();
        int period = 10;
        //assertSingleCalculation (FunctionFactory.getInstance ("TRIX"), controlData, column, period);
    }

    @Test
    public void calculateADD(){
        FIOExchange in1_in2 = new FIOExchange(TestData.IN_FOR_ADD_1, TestData.IN_FOR_ADD_2);
        FIOExchange in1_in3 = new FIOExchange(TestData.IN_FOR_ADD_1, TestData.IN_FOR_ADD_3);
        FIOExchange only_in1 = new FIOExchange(TestData.IN_FOR_ADD_1);

        ADD add = new ADD();
        FIOExchange out1 = add.calculate (in1_in3);
        assertTrue (Arrays.equals (TestData.ADD_IN1_IN3, out1.getData (0)));

        FIOExchange out2 = add.calculate (in1_in2);
        assertTrue (Arrays.equals (TestData.ADD_IN1_IN2, out2.getData (0)));
    }

    @Test
    public void calculateEMA_EMA(){
        double[] controlData = TestData.EMA_EMA_O_10;
        int column = PriceColumn.OPEN.getIndex ();
        int period = 10;
        assertNestedCalculation (
                FunctionFactory.getInstance ("EMA"),
                FunctionFactory.getInstance ("EMA"),
                column, period, period, controlData);
    }

    @Test
    public void calculateWMA_WMA(){
        double[] controlData = TestData.WMA_WMA_C_5_4;
        int column = PriceColumn.CLOSE.getIndex ();
        assertNestedCalculation (
                FunctionFactory.getInstance ("WMA"),
                FunctionFactory.getInstance ("WMA"),
                column, 5, 4, controlData);
    }

    @Test
    public void calculateTEMA(){
        double[] controlData = TestData.TEMA_O_10;
        int column = PriceColumn.OPEN.getIndex ();
        int period = 10;
        assertSingleCalculation (FunctionFactory.getInstance ("TEMA"), controlData, column, period);
    }

    @Test
    public void calculateRSI(){
        double[] controlData = TestData.RSI_C_14;
        int period = 14;
        assertSingleCalculation (FunctionFactory.getInstance ("RSI"), controlData, TestData.CLOSE_FOR_RSI, period);
    }

    @Test
    public void calculateDEMA(){
        double[] controlData = TestData.DEMA_O_10;
        int column = PriceColumn.OPEN.getIndex ();
        int period = 10;
        assertSingleCalculation (FunctionFactory.getInstance ("DEMA"), controlData, column, period);
    }

    private void assertSingleCalculation(Function fn, double[] controlData, int column, int period){
        FIOExchange testInput = new FIOExchange (baseSeries1.toArray (column));

        FIOExchange testOutput = fn.calculate (testInput, period);

        Assert.assertNotNull (testOutput);

        assertEquals (controlData.length, testOutput.getDataSize (0));

        for ( int i = 0; i < controlData.length; i++ ) {
            assertEquals (controlData[i], testOutput.getData (0, i), 1);
        }
    }

    private void assertSingleCalculation(Function fn, double[] controlData, double[] data, int period){
        FIOExchange testInput = new FIOExchange (data);

        FIOExchange testOutput = fn.calculate (testInput, period);

        Assert.assertNotNull (testOutput);

        assertEquals (controlData.length, testOutput.getDataSize (0));

        for ( int i = 0; i < controlData.length; i++ ) {
            assertEquals (controlData[i], testOutput.getData (0, i), 1);
        }
    }

    private void assertNestedCalculation(Function fn1, Function fn2, int column, int period1, int period2, double[] controlData){
        FIOExchange testInput = new FIOExchange (baseSeries1.toArray (column));

        FIOExchange testOutput = fn1.calculate (fn2.calculate (testInput, period2), period1);

        Assert.assertNotNull (testOutput);

        assertEquals (controlData.length, testOutput.getDataSize (0));

        for ( int i = 0; i < controlData.length; i++ ) {
            assertEquals (controlData[i], testOutput.getData (0, i), 1);
        }
    }
}