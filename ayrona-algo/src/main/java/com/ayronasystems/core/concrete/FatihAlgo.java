package com.ayronasystems.core.concrete;

import com.ayronasystems.core.algo.AlgoModule;
import com.ayronasystems.core.algo.tree.Node;

/**
 * Created by gorkemgok on 13/05/16.
 */
public class FatihAlgo extends AlgoModule{

    private double[] inputs;

    public FatihAlgo (double[] inputs) {
        this.inputs = inputs;
        define ();
    }

    public FatihAlgo () {
        this(new double[]{0.002, 0.2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 10, 10, 81});

    }

    public void define () {
        NAME = "FatihAlgo1";
        Node SAR = SAR (inputs[0], inputs[1]);

        Node SMA1 = SMA (C, (int)inputs[2]);
        Node SMA2 = SMA (SMA1, (int)inputs[3]);
        Node SMA3 = SMA (SMA2, (int)inputs[4]);
        Node SMA4 = SMA (SMA3, (int)inputs[5]);
        Node SMA5 = SMA (SMA4, (int)inputs[6]);
        Node SMA6 = SMA (SMA5, (int)inputs[7]);
        Node SMA7 = SMA (SMA6, (int)inputs[8]);
        Node SMA8 = SMA (SMA7, (int)inputs[9]);
        Node SMA9 = SMA (SMA8, (int)inputs[10]);
        Node SMA10 = SMA (SMA9, (int)inputs[11]);

        Node TOPLAM_SMA = ADD (SMA1, SMA2, SMA3, SMA4, SMA5, SMA6, SMA7, SMA8, SMA9, SMA10);
        Node KAPANIS_EKSI_SMA_BOLU_10 = SUB (C, DIV(TOPLAM_SMA, inputs[12]));

        Node HHV = HIGHEST (H, (int) inputs[13]);
        Node LLV = LOWEST (L, (int) inputs[14]);
        Node HLV_EKSI_LLV = SUB (HHV, LLV);

        Node ATAFILTRE = DIV (KAPANIS_EKSI_SMA_BOLU_10, HLV_EKSI_LLV);
        Node XX = SMA(ATAFILTRE, (int)inputs[15]);

        BUY = AND(GT (C , SAR), GT(XX, 0));
        SELL = AND(LT (C , SAR), LT(XX, 0));
    }

    @Override
    public String toString () {
        return "BUY("+BUY.toString ()+")\nSELL("+SELL.toString ()+")";
    }
}
