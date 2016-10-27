package main;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by allan on 26/10/16.
 */
@Slf4j
public class TravelingSalesmanMainTest {

    public static final int DIMENSION = 20;
    private static final int POPULATION_SIZE = 80;
    private static final int NUM_GENERATIONS = 200;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 0.6;
    private static final double MUTATION_RATE = 0.08;
    private static final int TOURNAMENT_ARITY = 2;
    private static Random gerador = new Random();

    // numbers from 0 to N-1
    private static List<Integer> genes = new ArrayList<Integer>();

    static {
        for (int i = 0; i < DIMENSION; i++) {
            genes.add(i);
        }
    }

    @Test
    public void test() {
        long startTime = System.currentTimeMillis();


        // initialize a new genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(),
                CROSSOVER_RATE,
                new RandomKeyMutation(),
                MUTATION_RATE,
                new TournamentSelection(TOURNAMENT_ARITY)
        );

        // initial population
        Population initial = randomPopulation();

        // stopping condition
        StoppingCondition stoppingCondition = new StoppingCondition() {

            int generation = 0;

            @Override
            public boolean isSatisfied(Population population) {
                Chromosome fittestChromosome = population.getFittestChromosome();

                if (generation == 1 || generation % 10 == 0) {
                    log.info("Generation " + generation + ": " + fittestChromosome.toString());
                }
                generation++;

                double fitness = fittestChromosome.fitness();
                if (Precision.equals(fitness, 0.0, 1e-6)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        // best initial chromosome
        Chromosome bestInitial = initial.getFittestChromosome();
        log.info("Generation " + geneticAlgorithm.getGenerationsEvolved() + ": " + bestInitial.toString());

        log.info("Starting evolution ...");

        // run the algorithm
        Population finalPopulation = geneticAlgorithm.evolve(initial, stoppingCondition);

        // Get the end time for the simulation.
        long endTime = System.currentTimeMillis();

        // best chromosome from the final population
        Chromosome best = finalPopulation.getFittestChromosome();
        log.info("Generation " + geneticAlgorithm.getGenerationsEvolved() + ": " + best.toString());
        log.info("Total execution time: " + (endTime - startTime) + "ms");
    }

    /**
     * Initializes a random population
     */
    private static ElitisticListPopulation randomPopulation() {
        List<Chromosome> popList = new ArrayList<Chromosome>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome randChrom = new MinPermutations(RandomKey.randomPermutation(DIMENSION));
            popList.add(randChrom);
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

    /**
     * Chromosomes representing a permutation of (0,1,2,...,DIMENSION-1).
     * <p>
     * The goal is to sort the sequence.
     */
    private static class MinPermutations extends RandomKey<Integer> {

        public MinPermutations(List<Double> representation) {
            super(representation);
        }

//        public double fitness() {
//            int result = 0;
//            List<Integer> decoded = decode(genes);
//            for (int i = 0; i < decoded.size(); i++) {
//                int value = new Integer(decoded.get(i));
//                if (value != i) {
//                    // bad position found
//                    log.info("Valor " + value);
//                    result += Math.abs(value - i);
//                }
//            }
//            // the most fitted chromosome is the one with minimal error
//            // therefore we must return negative value
//            return -result;
//        }

        public double fitness() {
            int result = 0;
            List<Integer> decoded = decode(genes);
            for (int i = 0; i < decoded.size(); i++) {
                int value = new Integer(decoded.get(i));
                result += value;
            }
            log.info("Resultado " + result);
            // the most fitted chromosome is the one with minimal error
            // therefore we must return negative value
            return result;
        }

        @Override
        public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
            return new MinPermutations(chromosomeRepresentation);
        }
    }
}