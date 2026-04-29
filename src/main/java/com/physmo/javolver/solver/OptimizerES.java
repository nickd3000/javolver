package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.mutationoperator.MutationOperator;

import java.util.*;


/**
 * A solver that uses an Evolutionary Strategy (ES) approach.
 * Unlike the standard population-based genetic algorithm, ES typically focuses on
 * evolving a single best individual (or a small set of individuals) through
 * heavy mutation and recombination of a pool of mutated clones.
 */
public class OptimizerES extends Solver {

    private final List<MutationOperator> mutationStrategies = new ArrayList<>();
    Individual bestIndividual;
    int dnaSize = 10;
    int poolSize = 20;
    int combineSize = 5;
    int mutationCount = 2;
    int iteration = 0;
    Random random = new Random();
    double changeAmount = 0.1;
    private ScoreFunction scoreFunction;

    /**
     * Default constructor.
     */
    public OptimizerES() {

    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);

    }

    @Override
    public void resetPopulation() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);
    }

    @Override
    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    @Override
    public void runOneGeneration() {
        iteration++;
        algorithm();
        changeAmount *= 0.99;
    }

    /**
     * Performs one generation of the ES algorithm.
     * This includes creating a pool of mutated clones, sorting them by score,
     * and combining the top individuals into a new "best" individual.
     */
    public void algorithm() {
        List<Individual> pool = new ArrayList<>();

        // Create pool of mutated clones.
        for (int i = 0; i < poolSize; i++) {
            pool.add(createMutatedClone(bestIndividual, 0.1));
        }

        // Sort pool.
        pool.sort(Comparator.comparingDouble(Individual::getScore).reversed());

        // Combine top results.
        Individual clone = bestIndividual.cloneFully();
        clone.setProcessed(false);

        double[] cloneDnaArray = clone.getDna().getData();
        Arrays.fill(cloneDnaArray, 0);
        for (int i = 0; i < combineSize; i++) {
            double[] parentDnaArray = pool.get(i).getDna().getData();
            for (int j = 0; j < parentDnaArray.length; j++) {
                cloneDnaArray[j] += parentDnaArray[j] / (double) combineSize;
            }
        }

        bestIndividual = clone;

    }

    /**
     * Creates a mutated copy of a parent individual.
     *
     * @param parent         The parent individual to clone.
     * @param mutationAmount The magnitude of mutation to apply.
     * @return A new, mutated Individual.
     */
    public Individual createMutatedClone(Individual parent, double mutationAmount) {
        Individual clone = parent.cloneFully();
        clone.setProcessed(false);

        for (int j = 0; j < mutationCount; j++) {
            int i = random.nextInt(clone.getDna().getSize());
            double dnaElement = clone.getDna().getDouble(i);
            dnaElement += (Math.random() - 0.5) * mutationAmount * changeAmount;
            clone.getDna().set(i, dnaElement);
        }
        return clone;
    }

    @Override
    public Individual getBestScoringIndividual() {
        return bestIndividual;
    }

    @Override
    public void setTemperature(double temperature) {
        this.changeAmount = temperature;
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    /**
     * Adds a mutation operator to be used by the solver.
     *
     * @param strategy The mutation strategy to add.
     */
    public void addMutationStrategy(MutationOperator strategy) {
        mutationStrategies.add(strategy);
    }
}
