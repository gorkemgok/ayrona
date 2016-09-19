package com.ayronasystems.genetics.examples.helloworld;

import com.ayronasystems.genetics.core.Chromosome;
import com.ayronasystems.genetics.core.Util;
import com.ayronasystems.genetics.core.MutationMethod;

/**
 * Created by gorkemgok on 29/03/16.
 */
public class StringMutation implements MutationMethod<String> {


    public String apply (Chromosome<String> chromosome, double probability) {
        String individual = chromosome.getGen ();
        if (probability > Util.randomDouble ()){
            char[] chars = individual.toCharArray ();
            chars[Util.randomInt (chars.length - 1)] = Util.randomChar ();
            String newString = new String (chars);
            //System.out.println ("!Mutation:"+individual+" > "+newString);
            return newString;
        }
        //System.out.println ("No mutation!");
        return individual;
    }
}
