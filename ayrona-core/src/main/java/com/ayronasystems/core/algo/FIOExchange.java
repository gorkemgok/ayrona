package com.ayronasystems.core.algo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 18/03/16.
 */
//TODO: Consider to make FIOExchange Immutable because of caching mechanism
public class FIOExchange {

    public enum Boolean{
        FALSE(0, false),
        TRUE(1, true);

        private double doubleValue;

        private boolean booleanValue;

        Boolean (double doubleValue, boolean booleanValue) {
            this.doubleValue = doubleValue;
            this.booleanValue = booleanValue;
        }

        public double getDoubleValue () {
            return doubleValue;
        }

        public boolean isBooleanValue () {
            return booleanValue;
        }

        public static Boolean valueOf(double value){
            if ( value == 0) {
                return FALSE;
            }else{
                return TRUE;
            }
        }

        public static Boolean valueOf(boolean value){
            if (value){
                return TRUE;
            }else {
                return FALSE;
            }
        }
    }

    private String hash;

    private final double[][] data;

    public FIOExchange(List<FIOExchange> fioExchangeList){
        int totalCol = 0;
        for ( FIOExchange fioExchange : fioExchangeList ){
            totalCol += fioExchange.getDataCount ();
        }
        double[][] result = new double[totalCol][];
        int ci = 0;
        for ( FIOExchange fioExchange : fioExchangeList ){
            for ( int i = 0; i < fioExchange.getDataCount (); i++){
                result[ci++] = fioExchange.getData (i);
            }
        }
        this.data = result;
    }

    public FIOExchange (double[]... data) {
        this.data = data;
    }

    public FIOExchange (double[] data) {
        this.data = new double[][]{data};
    }

    public double[][] getData () {
        return data;
    }

    public double[] getData (int index) {
        return data[index];
    }

    public double[] getDataWithShortestSize (int index) {
        int shortestSize = getShortestDataSize ();
        double[] data = getData (index);
        if (data.length > shortestSize){
            return Arrays.copyOfRange (data, data.length - shortestSize, data.length);
        }
        return data;
    }

    public double getData (int x, int y) {
        return data[x][y];
    }

    public int getDataCount(){
        return data.length;
    }

    public int getDataSize(int index){
        return data[index].length;
    }

    public int getShortestDataSize(){
        int min = Integer.MAX_VALUE;
        for (double[] d : data){
            min = Math.min (min, d.length);
        }
        return min;
    }

    public static FIOExchange generateConstant(double value, int size){
        double[] constant = new double[size];
        for ( int i = 0; i < size; i++ ) {
            constant[i] = value;
        }
        return new FIOExchange (constant);
    }
}
