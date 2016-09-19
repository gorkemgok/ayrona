package com.ayronasystems.gpe;

import com.ayronasystems.core.algo.FunctionFactory;
import com.ayronasystems.core.definition.Symbols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 * Created by gorkemgok on 11/09/16.
 */
public class Main {

    private static final long SYNC_PERIOD = 1000 * 30;

    private static Logger log = LoggerFactory.getLogger (Main.class);

    public static void main(String[] args){
        Symbols.init ();
        FunctionFactory.scanFunctions ();

        GeneticEngine ge = new GeneticEngine ();
        log.info ("Genetic engine created");
        CheckSessionQueue checkSessionQueue = new CheckSessionQueue (ge);
        Timer timer = new Timer (false);
        timer.schedule (checkSessionQueue, SYNC_PERIOD, SYNC_PERIOD);
        log.info ("Optimizer session checker is scheduled.");
    }

}
