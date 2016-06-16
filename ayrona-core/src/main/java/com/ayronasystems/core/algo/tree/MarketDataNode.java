package com.ayronasystems.core.algo.tree;

import com.ayronasystems.core.data.MarketData;
import com.ayronasystems.core.definition.PriceColumn;
import com.ayronasystems.core.algo.FIOExchange;

/**
 * Created by gorkemgok on 19/03/16.
 */
public class MarketDataNode implements Node{

    private PriceColumn[] priceColumns;

    public MarketDataNode (PriceColumn... priceColumns) {
        this.priceColumns = priceColumns;
    }

    public void checkPrerequisite (boolean checkPrerequisite) {

    }

    public int getNeededInputCount () {
        //Zero because needed input count FOR CALCULATION is zero because there is no calculation
        return 0;
    }

    public FIOExchange calculate (MarketData marketData) {
        double[][] data = new double[priceColumns.length][];
        for ( int i = 0; i < data.length; i++ ) {
            data[i] = marketData.getPrice (priceColumns[i]);
        }
        return new FIOExchange (data);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder ();
        boolean hasElem = false;
        for ( PriceColumn priceColumn :
                priceColumns ) {
            if (hasElem){
                sb.append (",");
            }
            sb.append (priceColumn);
            hasElem = true;
        }
        return sb.toString ();
    }
}
