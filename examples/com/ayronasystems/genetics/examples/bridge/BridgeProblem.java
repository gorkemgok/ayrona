package com.ayronasystems.genetics.examples.bridge;

import com.ayronasystems.genetics.core.*;
import com.ayronasystems.genetics.core.ast.ASTChromosome;
import com.ayronasystems.genetics.core.ast.ASTCrossover;
import com.ayronasystems.genetics.core.ast.RootNode;
import com.ayronasystems.genetics.core.listener.ListenerContext;
import com.ayronasystems.genetics.core.listener.NewFittestListener;
import com.ayronasystems.genetics.core.listener.NewGenerationListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkemgok on 30/03/16.
 */
public class BridgeProblem {

    private static final Person[] PERSONS = {
        new Person ("A", 1),
        new Person ("B", 2),
        new Person ("C", 3),
        new Person ("D", 4),
        new Person ("NONE"),
        new Person ("SLOWEST"),
        new Person ("FASTEST")
    };

    public static void main(String[] args){
        Population<ASTChromosome> initialPopulation = initialPopulation (100);

        SelectionMethod selectionMethod = new TournamentSelection (5, Comparators.SMALLER_IS_FITTER);

        CrossoverMethod crossoverMethod = new ASTCrossover ();

        FitnessFunction fitnessFunction = new BridgeFitnessFunction ();

        StopCondition stopCondition = new StopCondition () {

            //StopCondition fsc = new FittestValueStopCondition (15);

            public boolean satisfy (Population population) {

                return population.getGenerationCount () > 100;
            }
        };

        GPConfiguration configuration = new GPConfiguration ();

        configuration.setNewFittestListener (new NewFittestListener () {
            public void onNewFittest (ListenerContext listenerContext) {
                Population population = listenerContext.getPopulation ();
                System.out.println ("NEW FITTEST at "+population.getGenerationCount ()+". gen : "+population.getFittest ().getFitnessValue ());
                System.out.println ("\t"+population.getFittest ().toString ());
            }
        });

        configuration.setNewGenerationListener (new NewGenerationListener () {
            public void onNewGeneration (ListenerContext listenerContext) {

            }

            public void onNewGeneration (Population population) {
                //System.out.println(population.getGenerationCount ()+". generation with "+population.getPopulationSize ()+" chromosomes");
            }
        });

        GeneticProgram gp = new GeneticProgram (
                4,
                initialPopulation,
                selectionMethod,
                crossoverMethod,
                null,
                fitnessFunction,
                stopCondition,
                configuration
        );

        gp.evolve ();
    }

    public static Population<ASTChromosome> initialPopulation(int size){
        List<ASTChromosome> list = new ArrayList<ASTChromosome> (size);
        for ( int i = 0; i < size; i++ ) {
            list.add (new ASTChromosome (new RootNode (randomMove (0))));
        }
        Population<ASTChromosome> population = new Population<ASTChromosome> (list, Comparators.SMALLER_IS_FITTER);
        return population;
    }

    public static Move randomMove(int depth){
        List<Move.Type> types = Arrays.asList (Move.Type.CROSS, Move.Type.CROSS, Move.Type.STOP);
        Move move = new Move (depth < 20 ? Util.randomFromList (types) : Move.Type.STOP);
        if (!move.getMoveType ().equals (Move.Type.STOP)) {
            move.setNextMove (randomMove (++depth));
            move.setPersonSelector (randomPersonSelector ());
        }
        return move;
    }

    public static PersonSelector randomPersonSelector(){
        PersonSelector personSelector = new PersonSelector ();
        personSelector.addPerson (randomPerson ());
        personSelector.addPerson (randomPerson ());
        return personSelector;
    }

    public static Person randomPerson (){
        return PERSONS[Util.randomInt (6)];
    }
}
