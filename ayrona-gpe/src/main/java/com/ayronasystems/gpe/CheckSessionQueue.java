package com.ayronasystems.gpe;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.OptimizerSessionModel;
import com.ayronasystems.core.dao.model.TrainingSessionModel;
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
        List<OptimizerSessionModel> waitingSessionList =
                dao.findOptimizerSessions (TrainingSessionModel.State.WAITING);
        if (waitingSessionList.size () > 0) {
            log.debug ("{} Waiting sessions found", waitingSessionList.size ());
            for ( OptimizerSessionModel optimizerSessionModel : waitingSessionList ) {
                ge.startOptimizerSession (optimizerSessionModel);
            }
        }

        List<OptimizerSessionModel> canceledSessionList =
                dao.findOptimizerSessions (TrainingSessionModel.State.CANCELED);
        if (canceledSessionList.size () > 0) {
            log.debug ("{} Canceled sessions found", canceledSessionList.size ());
            for ( OptimizerSessionModel optimizerSessionModel : canceledSessionList ) {
                ge.stopOptimizerSession (optimizerSessionModel.getId ());
            }
        }
    }
}
