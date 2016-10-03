package com.ayronasystems.gpe;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.core.dao.model.TrainingSessionModel;
import com.ayronasystems.genetics.optimizer.Optimizer;
import com.ayronasystems.genetics.optimizer.OptimizerChromosome;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gorkemgok on 09/09/16.
 */
public class GeneticEngine {

    private static Logger log = LoggerFactory.getLogger (GeneticEngine.class);

    private List<Optimizer> optimizerList = new ArrayList<Optimizer> ();

    private Dao dao = Singletons.INSTANCE.getDao ();

    public void startOptimizerSession(OptimizerSessionModel optimizerSessionModel){
        Optimizer optimizer = new Optimizer (optimizerSessionModel, Collections.<OptimizerChromosome>emptyList ());
        optimizerList.add (optimizer);
        optimizer.start ();
        optimizerSessionModel.setState (TrainingSessionModel.State.RUNNING);
        dao.updateOptimizerSession (optimizerSessionModel);
        log.info ("Optimizer session started: {}", optimizer);
    }

    public boolean stopOptimizerSession(String sessionId){
        for (Optimizer optimizer : optimizerList){
            if (optimizer.getSessionId ().equals (sessionId)){
                optimizer.interrupt ();
                log.info ("Optimizer session interrupted:{}", optimizer);
                Optional<OptimizerSessionModel> sessionOptional = dao.findOptimizerSessionById (sessionId);
                if (sessionOptional.isPresent ()){
                    OptimizerSessionModel sessionModel = sessionOptional.get ();
                    sessionModel.setState (TrainingSessionModel.State.STOPPED);
                    dao.updateOptimizerSession (sessionModel);
                }
                return true;
            }
        }
        log.info ("Cant find optimizer with id {}", sessionId);
        Optional<OptimizerSessionModel> modelOptional = dao.findOptimizerSessionById (sessionId);
        if (modelOptional.isPresent ()){
            OptimizerSessionModel model = modelOptional.get ();
            model.setState (TrainingSessionModel.State.STOPPED);
            dao.updateOptimizerSession (model);
        }
        return false;
    }

}
