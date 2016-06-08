package com.ayronasystems.core.concurrent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 08/06/16.
 */
public class QueueRunnerTest {

    private class DummyQueueRunnable implements QueueRunnable<Integer>{

        private Map<Integer, List<Integer>> listOfIntegerList = new HashMap<Integer, List<Integer>> ();

        public void process (Integer element) {
            System.out.println ("Processing element:"+element);
            List<Integer> integerList = new ArrayList<Integer> ();
            for ( int i = 0; i < element; i++ ) {
                integerList.add (i);
                try {
                    Thread.currentThread ().sleep (10);
                } catch ( InterruptedException e ) {
                    e.printStackTrace ();
                }
            }
            listOfIntegerList.put (element, integerList);
        }

        public Map<Integer, List<Integer>> getListOfIntegerList () {
            return listOfIntegerList;
        }
    }

    @Test
    public void run () throws Exception {
        DummyQueueRunnable queueRunnable = new DummyQueueRunnable ();
        QueueRunner<Integer, DummyQueueRunnable> queueRunner =
                new QueueRunner<Integer, DummyQueueRunnable> (queueRunnable);
        DummyQueueRunnable queueRunnable2 = new DummyQueueRunnable ();
        QueueRunner<Integer, DummyQueueRunnable> queueRunner2 =
                new QueueRunner<Integer, DummyQueueRunnable> (queueRunnable2);

        queueRunner.start ();
        queueRunner2.start ();

        queueRunner.put (5);
        System.out.println ("Put to queue 1:"+5);
        queueRunner.put (7);
        System.out.println ("Put to queue 1:"+7);
        queueRunner.put (8);
        System.out.println ("Put to queue 1:"+8);
        queueRunner.put (9);
        System.out.println ("Put to queue 1:"+9);
        queueRunner.put (10);
        System.out.println ("Put to queue 1:"+10);

        queueRunner2.put (25);
        System.out.println ("Put to queue 2:"+25);
        queueRunner2.put (27);
        System.out.println ("Put to queue 2:"+27);
        queueRunner2.put (28);
        System.out.println ("Put to queue 2:"+28);
        queueRunner2.put (29);
        System.out.println ("Put to queue 2:"+29);
        queueRunner2.put (20);
        System.out.println ("Put to queue 2:"+20);

        Thread.currentThread ().sleep (3000);
        queueRunner.stop ();
        Thread.currentThread ().sleep (500);
        assertTrue (queueRunner.unwrapThread ().getState () == Thread.State.TERMINATED);

        Map<Integer, List<Integer>> listOfIntegerList = queueRunnable.getListOfIntegerList ();
        Map<Integer, List<Integer>> listOfIntegerList2 = queueRunnable2.getListOfIntegerList ();

        assertNotNull (listOfIntegerList.get (5));
        assertNotNull (listOfIntegerList.get (7));
        assertNotNull (listOfIntegerList.get (8));
        assertNotNull (listOfIntegerList.get (9));
        assertNotNull (listOfIntegerList.get (10));

        assertEquals (5, listOfIntegerList.get (5).size ());
        assertEquals (7, listOfIntegerList.get (7).size ());
        assertEquals (8, listOfIntegerList.get (8).size ());
        assertEquals (9, listOfIntegerList.get (9).size ());
        assertEquals (10, listOfIntegerList.get (10).size ());

        assertNotNull (listOfIntegerList2.get (25));
        assertNotNull (listOfIntegerList2.get (27));
        assertNotNull (listOfIntegerList2.get (28));
        assertNotNull (listOfIntegerList2.get (29));
        assertNotNull (listOfIntegerList2.get (20));

        assertEquals (25, listOfIntegerList2.get (25).size ());
        assertEquals (27, listOfIntegerList2.get (27).size ());
        assertEquals (28, listOfIntegerList2.get (28).size ());
        assertEquals (29, listOfIntegerList2.get (29).size ());
        assertEquals (20, listOfIntegerList2.get (20).size ());
    }

}