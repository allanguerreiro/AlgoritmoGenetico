package main;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import util.AbstractMatrizDinamica;
import util.MatrizDinamica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by allan on 26/10/16.
 */
@Slf4j
public class TravelingSalesmanMainTest {

    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 0.6;
    private static final double MUTATION_RATE = 0.08;
    private static final int TOURNAMENT_ARITY = 2;
    private static final int ROW = 10;
    private static final int COLUMN = 5;
    private static MatrizDinamica<String> genes;
    private static Chromosome fittestChromosome;
    private static Double menor = new Double(Double.MAX_VALUE);

    @Test
    public void test() {
        long startTime = System.currentTimeMillis();

        // initialize a new genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new OrderedCrossover(),
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
                Chromosome fittestChromosome = getFittestChromosome(population);

                if (generation == 1 || generation % 10 == 0) {
                    log.info("Generation " + generation + ": " + fittestChromosome.getFitness());
                }
                generation++;

                double fitness = fittestChromosome.fitness();
                if (fitness <= menor) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        // best initial chromosome
        Chromosome bestInitial = getFittestChromosome(initial);
        log.info("Generation " + geneticAlgorithm.getGenerationsEvolved() + ": " + bestInitial.getFitness());

        log.info("Starting evolution ...");

        // run the algorithm
        Population finalPopulation = geneticAlgorithm.evolve(initial, stoppingCondition);

        // Get the end time for the simulation.
        long endTime = System.currentTimeMillis();

        // best chromosome from the final population
        Chromosome best = getFittestChromosome(finalPopulation);

        log.info("Generation " + geneticAlgorithm.getGenerationsEvolved() + ": " + best.getFitness());
        log.info("Total execution time: " + (endTime - startTime) + "ms");
    }

    /**
     * Initializes a random population
     */
    private static ElitisticListPopulation randomPopulation() {
        List<Chromosome> popList = new ArrayList<>();
        genes = MatrizDinamica.randomPopulation(ROW, COLUMN);

        for (int i = 0; i < genes.getElementos().size(); i++) {
            Chromosome randChrom = new StringChromosome(genes.getElementos().get(i));
            popList.add(randChrom);
        }

        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

    private static Chromosome getFittestChromosome(Population population) {
        for (Chromosome chromosome : population) {
            if (chromosome.getFitness() < menor) {
                menor = chromosome.getFitness();
                fittestChromosome = chromosome;
            }
        }

        return fittestChromosome;
    }

    /**
     * String Chromosome represented by a list of characters.
     */
    public static class StringChromosome extends AbstractMatrizDinamica<String> {

        public StringChromosome(Map<Integer, String> representation) {
            super(representation);
        }

        public double fitness() {
            int fitness = 0; // start at 0; the best fitness
            for (int i = 0; i < this.getElements().values().size(); i++) {
                String teste = this.getElements().get(i).substring(31, this.getElements().get(i).length());
                int distancia = Integer.parseInt(teste);
                fitness += distancia;
            }
            return fitness;
        }
    }
}