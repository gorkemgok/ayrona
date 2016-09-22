package com.ayronasystems.core.edr;

/**
 * Created by gorkemgok on 22/09/2016.
 */
public class EdrBuilder {

    private static EdrBuilder edrBuilder = new EdrBuilder ();

    public static EdrBuilder getInstance(){
        return edrBuilder;
    }

    private EdrBuilder(){
    }

    public EdrRecord.Builder boundAccount(){return new EdrRecord.Builder (EdrType.BOUND_ACCOUNT);}
    public EdrRecord.Builder unboundAccount(){return new EdrRecord.Builder (EdrType.UNBOUND_ACCOUNT);}
    public EdrRecord.Builder startStrategy(){return new EdrRecord.Builder (EdrType.START_STRATEGY);}
    public EdrRecord.Builder stopStrategy(){return new EdrRecord.Builder (EdrType.STOP_STRATEGY);}
    public EdrRecord.Builder newSignal(){return new EdrRecord.Builder (EdrType.NEW_SIGNAL);}
    public EdrRecord.Builder startAccount(){return new EdrRecord.Builder (EdrType.START_ACCOUNT);}
    public EdrRecord.Builder stopAccount(){return new EdrRecord.Builder (EdrType.STOP_ACCOUNT);}
    public EdrRecord.Builder newPosition(){return new EdrRecord.Builder (EdrType.NEW_POSITION);}

}
