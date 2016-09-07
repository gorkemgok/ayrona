package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.FitnessFunction;
import com.ayronasystems.genetics.core.ast.ASTChromosome;
import com.ayronasystems.genetics.core.ast.Node;
import com.ayronasystems.genetics.core.ast.RootNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemgok on 01/04/16.
 */
public class BridgeFitnessFunction implements FitnessFunction<ASTChromosome>{

    private List<Person> leftSide = new ArrayList<Person> ();

    private List<Person> rightSide = new ArrayList<Person> ();

    private boolean rightToLeft = true;

    private static int recursionCount = 0;

    public double calculateFitness (ASTChromosome chromosome) {
        recursionCount = 0;
        leftSide.clear ();
        rightSide.clear ();
        rightToLeft = true;

        RootNode rootNode = chromosome.getGen ();

        for (Node node : rootNode.getNestedNodes (BPNodeTypes.TRM_PERSON_WITH)){
            Person person = (Person)node;
            if (!rightSide.contains (person)) rightSide.add (person);
        }

        Move firstMove = (Move) rootNode.getChildNode (0);
        double travelTime = calculateMoveTime (firstMove) + 30;
        if (leftSide.size () == 4 && rightSide.size () == 1){
                travelTime -= 30;
        }
        chromosome.setFitnessValue (travelTime);
        //System.out.println ("Recursion : "+recursionCount);
        return travelTime;
    }

    private double calculateMoveTime(Move move){
        recursionCount++;
        int max = 0;
        PersonSelector personSelector = move.getPersonSelector ();
        if (personSelector != null) {
            List<Person> movingPersonList = move.getPersonSelector ()
                                                .getPersonList ();
            for ( Person person : movingPersonList ) {
                if (rightToLeft){
                    if (rightSide.remove (person))
                        leftSide.add (person);
                }else {
                    if (leftSide.remove (person))
                        rightSide.add (person);
                }
                max = Math.max (person.getMoveTime (), max);
            }
            rightToLeft = !rightToLeft;
        }
        double nextMoveTime = 0;
        Move nextMove = move.getNextMove ();
        if (nextMove != null){
            nextMoveTime = calculateMoveTime (nextMove);
        }
        return max + nextMoveTime;
    }
}
