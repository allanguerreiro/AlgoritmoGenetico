package main;

/**
 * Created by allan on 26/10/16.
 */

import org.apache.commons.math3.genetics.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is also an example of usage.
 */
public class GeneticAlgorithmTestBinary {

    // parameters for the GA
    private static final int DIMENSION = 50;
    private static final int POPULATION_SIZE = 50;
    private static final int NUM_GENERATIONS = 50;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 0.6;
    private static final double MUTATION_RATE = 0.01;
    private static final int TOURNAMENT_ARITY = 2;

    @Test
    public void test() {
        // to test a stochastic algorithm is hard, so this will rather be an usage example
        // initialize a new genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(),
                CROSSOVER_RATE, // all selected chromosomes will be recombined (=crosssover)
                new BinaryMutation(),
                MUTATION_RATE,
                new TournamentSelection(TOURNAMENT_ARITY)
        );

        assertEquals(0, geneticAlgorithm.getGenerationsEvolved());

        // initial population
        Population initial = randomPopulation();

        // stopping conditions
        StoppingCondition stopCond = new FixedGenerationCount(NUM_GENERATIONS);

        // best initial chromosome
        Chromosome bestInitial = initial.getFittestChromosome();

        // run the algorithm
        Population finalPopulation = geneticAlgorithm.evolve(initial, stopCond);

        // best chromosome from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();

        // the only thing we can test is whether the final solution is not worse than the initial one
        // however, for some implementations of GA, this need not be true :)
        assertTrue(bestFinal.compareTo(bestInitial) > 0);
        assertEquals(NUM_GENERATIONS, geneticAlgorithm.getGenerationsEvolved());

    }

    /**
     * Initializes a random population.
     */
    private static ElitisticListPopulation randomPopulation() {
        List<Chromosome> popList = new LinkedList();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            BinaryChromosome randChrom = new FindOnes(BinaryChromosome.randomBinaryRepresentation(DIMENSION));
            popList.add(randChrom);
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

    /**
     * Chromosomes represented by a binary chromosome.
     * <p>
     * The goal is to set all bits (genes) to 1.
     */
    private static class FindOnes extends BinaryChromosome {

        public FindOnes(List<Integer> representation) {
            super(representation);
        }

        /**
         * Returns number of elements != 0
         */
        public double fitness() {
            int num = 0;
            for (int val : this.getRepresentation()) {
                if (val != 0)
                    num++;
            }
            // number of elements >= 0
            return num;
        }

        @Override
        public AbstractListChromosome<Integer> newFixedLengthChromosome(List chromosomeRepresentation) {
            return new FindOnes(chromosomeRepresentation);
        }
    }
}
