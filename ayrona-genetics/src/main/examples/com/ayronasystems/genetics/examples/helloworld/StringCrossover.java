package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.Util;
import com.ayronasystems.genetics.core.ChromosomePair;
import com.ayronasystems.genetics.core.CrossoverMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class StringCrossover implements CrossoverMethod<StringChromosome> {

    public List<StringChromosome> apply (ChromosomePair<StringChromosome> pair) {
        List<StringChromosome> offsets = new ArrayList<StringChromosome> ();
        String s1 = pair.getChromosome1 ().getGen ();
        String s2 = pair.getChromosome2 ().getGen ();
        int minLen = Math.min (s1.length (), s2.length ()
        );
        int crossOverPoint = Util.randomInt (minLen - 2);
        StringBuilder offset1 = new StringBuilder ();
        offset1.append (s1.substring (0, crossOverPoint))
               .append (s2.substring (crossOverPoint));

        StringBuilder offset2 = new StringBuilder ();
        offset2.append (s2.substring (0, crossOverPoint))
               .append (s1.substring (crossOverPoint));

        offsets.add (new StringChromosome (offset1.toString ()));
        offsets.add (new StringChromosome (offset2.toString ()));
        pair.getChromosome1 ().setParents (null, null);
        pair.getChromosome2 ().setParents (null, null);
        offsets.get (0).setParents (pair.getChromosome1 (), pair.getChromosome2 ());
        offsets.get (1).setParents (pair.getChromosome1 (), pair.getChromosome2 ());

        //System.out.println (s1 + " X " +s2+" = "+offsets.get (0));

        return offsets;
    }
}
