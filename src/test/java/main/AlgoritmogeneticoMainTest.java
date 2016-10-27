package main;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by allan on 26/10/16.
 */
@Slf4j
public class AlgoritmogeneticoMainTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String TARGET_STRING = "Teste de Strings";
    public static final int DIMENSION = TARGET_STRING.length();
    private static final int POPULATION_SIZE = 100;
    private static final int NUM_GENERATIONS = 10000;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 0.6;
    private static final double MUTATION_RATE = 0.05;
    private static final int TOURNAMENT_ARITY = 2;

    @Getter
    @Setter
    private static String genes = "";

    @Test
    public void test() {
        long startTime = System.currentTimeMillis();

        // initialize a new genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new OnePointCrossover<Integer>(), CROSSOVER_RATE, // all selected chromosomes will be recombined (=crosssover)
                new RandomCharacterMutation(), MUTATION_RATE,
                new TournamentSelection(TOURNAMENT_ARITY)
        );

        assertEquals(0, geneticAlgorithm.getGenerationsEvolved());

        // initial population
        Population initial = getInitialPopulation();

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

    private static List<Character> randomRepresentation(int length) {
        return asList(RandomStringUtils.randomAscii(length));
    }

    private static List<Character> asList(String str) {
        return Arrays.asList(ArrayUtils.toObject(str.toCharArray()));
    }

    private static Population getInitialPopulation() {
        List<Chromosome> popList = new LinkedList<Chromosome>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            popList.add(new StringChromosome(randomRepresentation(DIMENSION)));
        }
        return new ElitisticListPopulation(popList, 2 * popList.size(), ELITISM_RATE);
    }

    /**
     * String Chromosome represented by a list of characters.
     */
    public static class StringChromosome extends AbstractListChromosome<Character> {

        public StringChromosome(List<Character> repr) {
            super(repr);
        }

        public StringChromosome(String str) {
            this(asList(str));
        }

        public double fitness() {
            String target = TARGET_STRING;
            int fitness = 0; // start at 0; the best fitness
            List<Character> chromosome = getRepresentation();
            for (int i = 0, c = target.length(); i < c; i++) {
                // subtract the ascii difference between the target character and the chromosome character.
                // Thus 'c' is fitter than 'd' when compared to 'a'.
                fitness -= FastMath.abs(target.charAt(i) - chromosome.get(i).charValue());
            }
            return fitness;
        }

        @Override
        protected void checkValidity(List<Character> repr) throws InvalidRepresentationException {
            for (char c : repr) {
                if (c < 32 || c > 126) {
                    throw new InvalidRepresentationException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME);
                }
            }
        }

        public List<Character> getStringRepresentation() {
            return getRepresentation();
        }

        @Override
        public StringChromosome newFixedLengthChromosome(List<Character> repr) {
            return new StringChromosome(repr);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Character i : getRepresentation()) {
                sb.append(i.charValue());
            }
            return String.format("(f=%s '%s')", getFitness(), sb.toString());
        }
    }

    private static class RandomCharacterMutation implements MutationPolicy {
        public Chromosome mutate(Chromosome original) {
            if (!(original instanceof StringChromosome)) {
                throw new IllegalArgumentException();
            }

            StringChromosome strChromosome = (StringChromosome) original;
            List<Character> characters = strChromosome.getStringRepresentation();

            int mutationIndex = GeneticAlgorithm.getRandomGenerator().nextInt(characters.size());

            List<Character> mutatedChromosome = new ArrayList<Character>(characters);
            char newValue = (char) (32 + GeneticAlgorithm.getRandomGenerator().nextInt(127 - 32));
            mutatedChromosome.set(mutationIndex, newValue);

            return strChromosome.newFixedLengthChromosome(mutatedChromosome);
        }
    }
}