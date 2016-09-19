package com.ayronasystems.gpe;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by gorkemgok on 09/09/16.
 */
public class CheckSessionQueue extends TimerTask {

    private static Logger log = LoggerFactory.getLogger (CheckSessionQueue.class);

    private Dao dao = Singletons.INSTANCE.getDao ();

    private GeneticEngine ge;

    public CheckSessionQueue (GeneticEngine ge) {
        this.ge = ge;
    }

    public void run () {
        List<OptimizerSessionModel> optimizerSessionModelList = dao.findWaitingOptimizerSessions ();
        if (optimizerSessionModelList.size () > 0) {
            log.debug ("{} Waiting sessions found", optimizerSessionModelList.size ());
            for ( OptimizerSessionModel optimizerSessionModel : optimizerSessionModelList ) {
                ge.startOptimizerSession (optimizerSessionModel);
            }
        }
    }
}
