package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.ast.ASTChromosome;
import com.ayronasystems.genetics.core.ast.Node;
import com.ayronasystems.genetics.core.ast.RootNode;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gorkemgok on 01/04/16.
 */
public class BridgeProblemTest {

    @Test
    public void getCorrectChildNodes (){
        Person A = new Person ("A", 1);
        Person B = new Person ("B", 2);
        Person C = new Person ("C", 5);
        Person D = new Person ("D", 8);
        Person NONE = new Person ("NONE");

        PersonSelector personSelector1 = new PersonSelector ();
        personSelector1.addPerson (A);
        personSelector1.addPerson (B);
        PersonSelector personSelector2 = new PersonSelector ();
        personSelector2.addPerson (A);
        personSelector2.addPerson (NONE);
        PersonSelector personSelector3 = new PersonSelector ();
        personSelector3.addPerson (C);
        personSelector3.addPerson (D);
        PersonSelector personSelector4 = new PersonSelector ();
        personSelector4.addPerson (B);
        personSelector4.addPerson (NONE);
        PersonSelector personSelector5 = new PersonSelector ();
        personSelector5.addPerson (A);
        personSelector5.addPerson (B);

        Move move5 = new Move (Move.Type.CROSS);
        move5.setPersonSelector (personSelector5);
        move5.setNextMove (new Move (Move.Type.STOP));

        Move move4 = new Move (Move.Type.CROSS);
        move4.setPersonSelector (personSelector4);
        move4.setNextMove (move5);

        Move move3 = new Move (Move.Type.CROSS);
        move3.setPersonSelector (personSelector3);
        move3.setNextMove (move4);

        Move move2 = new Move (Move.Type.CROSS);
        move2.setPersonSelector (personSelector2);
        move2.setNextMove (move3);

        Move move1 = new Move (Move.Type.CROSS);
        move1.setPersonSelector (personSelector1);
        move1.setNextMove (move2);

        RootNode rootNode = new RootNode (move1);

        List<? extends Node> allNodes = rootNode.getNestedNodes ();

        assertEquals ("All node count", 21, allNodes.size ());

        BridgeFitnessFunction fitnessFunction = new BridgeFitnessFunction ();

        double travelTime = fitnessFunction.calculateFitness (new ASTChromosome (rootNode));

        assertEquals ("Travel Time", 15, travelTime, 0);
    }

}