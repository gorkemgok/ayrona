package com.ayronasystems.genetics.core.ast;

import com.ayronasystems.genetics.core.ChromosomePair;
import com.ayronasystems.genetics.examples.bridge.BPNodeTypes;
import com.ayronasystems.genetics.examples.bridge.Move;
import com.ayronasystems.genetics.examples.bridge.Person;
import com.ayronasystems.genetics.examples.bridge.PersonSelector;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gorkemgok on 02/04/16.
 */
public class ASTCrossoverTest {

    @Test
    public void apply () throws Exception {

        //Root 1 children
        Move move_1_1 = new Move (Move.Type.CROSS);
        Move move_1_2 = new Move (Move.Type.CROSS);
        Move move_1_3 = new Move (Move.Type.STOP);
        PersonSelector personSelector_1_2 = new PersonSelector ();
        PersonSelector personSelector_1_1 = new PersonSelector ();

        RootNode rootNode1 = new RootNode (move_1_1);
        move_1_1.setNextMove (move_1_2);
        move_1_1.setPersonSelector (personSelector_1_1);
        move_1_2.setNextMove (move_1_3);
        move_1_2.setPersonSelector (personSelector_1_2);

        Person person_1_1_1 = new Person ("A", 1);
        personSelector_1_1.addPerson (person_1_1_1);
        Person person_1_1_2 = new Person ("B",2);
        personSelector_1_1.addPerson (person_1_1_2);
        Person person_1_2_1 = new Person ("C",2);
        personSelector_1_2.addPerson (person_1_2_1);
        Person person_1_2_2 = new Person ("D",2);
        personSelector_1_2.addPerson (person_1_2_2);

        //Root 2 children
        Move move_2_1 = new Move (Move.Type.CROSS);
        Move move_2_2 = new Move (Move.Type.CROSS);
        Move move_2_3 = new Move (Move.Type.CROSS);
        Move move_2_4 = new Move (Move.Type.STOP);

        PersonSelector personSelector_2_1 = new PersonSelector ();
        PersonSelector personSelector_2_2 = new PersonSelector ();
        PersonSelector personSelector_2_3 = new PersonSelector ();

        RootNode rootNode2 = new RootNode (move_2_1);
        move_2_1.setNextMove (move_2_2);
        move_2_1.setPersonSelector (personSelector_2_1);
        move_2_2.setNextMove (move_2_3);
        move_2_2.setPersonSelector (personSelector_2_2);
        move_2_3.setNextMove (move_2_4);
        move_2_3.setPersonSelector (personSelector_2_3);

        personSelector_2_1.addPerson (new Person ("A2",1));
        personSelector_2_1.addPerson (new Person ("B2",1));
        personSelector_2_2.addPerson (new Person ("C3",2));
        personSelector_2_2.addPerson (new Person ("D3",2));
        personSelector_2_3.addPerson (new Person ("C4",2));
        personSelector_2_3.addPerson (new Person ("D4",2));

        List<Node> nodeList1 = rootNode1.getNestedNodes ();
        List<Node> nodeList2 = rootNode2.getNestedNodes ();

        assertEquals ("RootNode1 Depth", 5 , rootNode1.getDepth ());

        assertEquals ("RootNode2 Depth", 6 , rootNode2.getDepth ());

        for ( Node node : rootNode1.getNestedNodes (BPNodeTypes.FN_CROSS, 2)) {
            assertTrue ("Desired depth", 2 <= node.getDepth ());
        }

        assertEquals (nodeList1.get (0), move_1_1);
        assertEquals (nodeList2.get (0), move_2_1);

        assertEquals (nodeList1.get (1), move_1_2);
        assertEquals (nodeList2.get (1), move_2_2);

        assertEquals (nodeList1.get (2), rootNode1.getChildNode(2));
        assertEquals (nodeList2.get (2),rootNode2.getChildNode (2));

        assertEquals (nodeList1.get (3), rootNode1.getChildNode (3));
        assertEquals (nodeList2.get (3), rootNode2.getChildNode (3));

        ASTCrossover crossoverMethod = new ASTCrossover ();

        System.out.println (rootNode1);
        System.out.println (rootNode2);

        List<ASTChromosome> crossovers = crossoverMethod.apply (new ChromosomePair<ASTChromosome> (new ASTChromosome (rootNode1), new ASTChromosome (rootNode2)));
        for ( ASTChromosome chromosome :
                crossovers ) {
            System.out.println (chromosome);
        }

    }
}