package com.ayronasystems.genetics.optimizer;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.GeneratedCode;
import com.ayronasystems.genetics.core.Population;
import com.ayronasystems.genetics.core.listener.ListenerContext;
import com.ayronasystems.genetics.core.listener.NewFittestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.StringJoiner;

/**
 * Created by gorkemgok on 22/07/16.
 */
public class NewFittestOptimizerListener implements NewFittestListener<OptimizerChromosome> {

    private static Logger log = LoggerFactory.getLogger (NewFittestOptimizerListener.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    private String sessionId;

    private String code;

    public NewFittestOptimizerListener (String sessionId, String code) {
        this.sessionId = sessionId;
        this.code = code;
    }

    public void onNewFittest (ListenerContext<OptimizerChromosome> listenerContext) {
        Population<OptimizerChromosome> population = listenerContext.getPopulation ();
        OptimizerChromosome fittest = population.getFittest ();
        StringJoiner sj = new StringJoiner (",", "Sistem.OPT([", "]);");
        for (Double d : fittest.getGen ()){
            sj.add (String.valueOf (d));
        }
        StringBuilder sb = new StringBuilder (sj.toString ()).append ("\n\n");
        sb.append (code);
        String generatedCodeString = sb.toString ();
        GeneratedCode generatedCode = new GeneratedCode ();
        generatedCode.setCode (generatedCodeString);
        generatedCode.setFitness (fittest.getFitnessValue ());
        generatedCode.setGenerationCount (population.getGenerationCount ());
        dao.addOptimizedCode (sessionId, generatedCode);
        log.info ("NEW FITTEST("+(listenerContext.getProcessTime ())+"ms): "+new DecimalFormat ("##.##").format (fittest.getFitnessValue ())+" : "+fittest);
    }
}
