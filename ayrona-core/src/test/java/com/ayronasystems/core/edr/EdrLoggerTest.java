package com.ayronasystems.core.edr;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.configuration.ConfigurationConstants;
import com.ayronasystems.core.dao.Dao;
import com.ayronasystems.core.dao.model.EdrModel;
import com.ayronasystems.core.dao.mongo.MongoDaoTestITCase;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class EdrLoggerTest {

    public static int savedCount;

    class EdrTestThread extends Thread{
        private int id;

        public EdrTestThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Edr.boundAccount().strategyName("Fatih ate").strategyId("1s").accountName("Görkem Gök").accountId("1a").lot(1).putQueue();
                System.out.println("Thread "+id+"edr "+i+" put");
                Edr.unboundAccount().strategyName("Fatih ate").strategyId("1s").accountName("Görkem Gök").accountId("1a").putQueue();
                System.out.println("Thread "+id+"edr "+i+" put");
                Edr.startStrategy().strategyName("Fatih ate 2").strategyId("2s").putQueue();
                System.out.println("Thread "+id+"edr "+i+" put");
                Edr.stopStrategy().strategyName("Fatih ate 2").strategyId("2s").putQueue();
                System.out.println("Thread "+id+"edr "+i+" put");
                Edr.startAccount().accountName("Fatih").accountId("2a").putQueue();
                System.out.println("Thread "+id+"edr "+i+" put");
                Edr.stopAccount().accountName("Cemal").accountId("3a").putQueue();
            }
        }
    }

    private Date beginDate;

    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
    private Thread thread4;
    private Thread thread5;
    private Thread thread6;
    private Thread thread7;
    private Thread thread8;
    private Thread thread9;
    private Thread thread10;
    private Thread thread11;
    private Thread thread12;


    private Dao dao;
    @Before
    public void setUp() throws Exception {
        System.setProperty (ConfigurationConstants.PROP_MONGODB_DS, MongoDaoTestITCase.AYRONA_TEST_DB_NAME);

        MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();
        mongoClient.dropDatabase (MongoDaoTestITCase.AYRONA_TEST_DB_NAME);

        beginDate = new Date();
        thread1 = new EdrTestThread(1);
        thread2 = new EdrTestThread(2);
        thread3 = new EdrTestThread(3);
        thread4 = new EdrTestThread(4);
        thread5 = new EdrTestThread(5);
        thread6 = new EdrTestThread(6);
        thread7 = new EdrTestThread(7);
        thread8 = new EdrTestThread(8);
        thread9 = new EdrTestThread(9);
        thread10 = new EdrTestThread(10);
        thread11 = new EdrTestThread(11);
        thread12 = new EdrTestThread(12);
        dao = Singletons.INSTANCE.getDao();
    }

    @Test
    public void saveEdr() throws Exception {
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();
        thread11.start();
        thread12.start();

        Thread.currentThread().sleep(1000);

        while (!EdrQueue.getQueue().isIdle()){

        }
        long edrCount = EdrLogger.getLogger().getEdrCount();
        Date endDate = new Date();
        List<EdrModel> strategyEdrModelList = dao.findEdr(EdrModule.STRATEGY, beginDate, endDate);
        List<EdrModel> accountEdrModelList = dao.findEdr(EdrModule.ACCOUNT, beginDate, endDate);
        int totalEdrCount = strategyEdrModelList.size() + accountEdrModelList.size();
        assertEquals(720, edrCount);
        assertEquals(edrCount, totalEdrCount);
        System.out.print("Finished:"+edrCount);
    }
}