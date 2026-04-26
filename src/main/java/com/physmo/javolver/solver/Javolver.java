package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.mutationoperator.MutationOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A population-based genetic algorithm solver.
 * <p>
 * {@code Javolver} manages a pool of {@link Individual} instances and evolves them over
 * successive generations. Each generation scores the current population, selects parents,
 * breeds new children, applies mutation operators, and replaces the old population with
 * the newly generated one.
 * <p>
 * Solver behavior is controlled by {@link JavolverConfig}, including population size,
 * DNA size, scoring, selection, breeding, mutation, elitism, duplicate prevention, and
 * optional parallel scoring.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 */
public class Javolver extends Solver {

    Random random = new Random();
    private List<Individual> genePool = new ArrayList<>();
    private boolean allScored = false;
    private final double changeAmount = 1;
    private SpeciesCheck speciesCheck = null;
    private JavolverConfig config;

    /**
     * Constructs a Javolver instance with a given configuration.
     *
     * @param javolverConfig The configuration object specifying all solver settings.
     */
    public Javolver(JavolverConfig javolverConfig) {
        this.config = javolverConfig;
    }

    /**
     * Provides a builder for conveniently creating and configuring a Javolver instance.
     *
     * @return A new {@link JavolverBuilder} instance.
     */
    public static JavolverBuilder builder() {
        return new JavolverBuilder();
    }

    /**
     * Retrieves the current configuration of the solver.
     *
     * @return The {@link JavolverConfig} object.
     */
    public JavolverConfig getConfig() {
        return config;
    }

    /**
     * Sets a new configuration for the solver.
     *
     * @param config The {@link JavolverConfig} object to set.
     */
    public void setConfig(JavolverConfig config) {
        this.config = config;
    }

    /**
     * Returns the number of iterations (generations) performed so far.
     *
     * @return The current iteration count.
     */
    @Override
    public int getIteration() {
        return iteration;
    }


    /**
     * Initializes the solver by creating the initial population.
     */
    @Override
    public void init() {
        increasePopulation(config.getTargetPopulationSize());
    }

    /**
     * Adds new, randomly initialized individuals to the population until it reaches the target size.
     *
     * @param targetCount The desired size of the population.
     */
    public void increasePopulation(int targetCount) {
        Individual n;
        int target = targetCount - genePool.size();
        if (target < 1) return;

        for (int i = 0; i < target; i++) {
            n = new Individual(config.getDnaSize());
            n.setScoreFunction(config.getScoreFunction());
            if (config.getDnaInitializer() != null) n.getDna().initFromFunction(config.getDnaInitializer());
            genePool.add(n);
        }
    }

    /**
     * Gets the DNA size for individuals in the population.
     *
     * @return The configured DNA size.
     */
    public int getDnaSize() {
        return config.getDnaSize();
    }

    /**
     * Sets the DNA size for individuals in the population.
     *
     * @param dnaSize The new DNA size.
     */
    @Override
    public void setDnaSize(int dnaSize) {
        config.setDnaSize(dnaSize);
    }


    /**
     * Finds the best score of any individual in the supplied pool.
     *
     * @param pool The list of individuals to check.
     * @return The best score found in the pool.
     */
    public double getBestScore(List<Individual> pool) {
        if (pool == null) pool = genePool;
        return findBestScoringIndividual(pool).getScore();
    }

    /**
     * Finds the individual with the highest score in the supplied pool.
     *
     * @param pool The list of individuals to search.
     * @return The best-scoring individual.
     */
    public Individual findBestScoringIndividual(List<Individual> pool) {
        return pool.stream().max(Comparator.comparing(Individual::getScore)).get();
    }

    /**
     * Finds the best score among all individuals in the current population.
     *
     * @return The highest score in the current generation.
     */
    public double getBestScore() {
        return findBestScoringIndividual(genePool).getScore();
    }

    /**
     * Executes one full generation of the evolutionary process. This involves:
     * 1. Scoring all individuals in the population.
     * 2. Optionally preserving the best individual (elitism).
     * 3. Selecting parents and breeding them to create a new generation.
     * 4. Mutating the offspring to introduce genetic diversity.
     * 5. Replacing the old population with the new one.
     */
    @Override
    public void runOneGeneration() {
        iteration++;

        // Ensure all individuals have an up-to-date score.
        scoreGenes(genePool);

        ArrayList<Individual> newGenePool = new ArrayList<>();
        int targetPop = genePool.size();

        // Elitism: keep the best individual if configured to do so.
        if (config.isKeepBestIndividualAlive()) {
            Individual bestScorer = findBestScoringIndividual(genePool);
            bestScorer.setProcessed(false);
            newGenePool.add(bestScorer);
        }

        Individual g1, g2;

        // Create the new generation.
        while (newGenePool.size() < targetPop) {
            g1 = g2 = null;
            int speciationClashes = 0;
            // Select two distinct parents.
            for (int ii = 0; ii < 100; ii++) {
                g1 = config.getSelectionOperator().select(genePool);
                g2 = config.getSelectionOperator().select(genePool);

                if (speciesCheck != null && !speciesCheck.isSameSpecies(g1, g2) && speciationClashes < 50) {
                    speciationClashes++;
                    continue;
                }

                if (g1 != null && g2 != null && g1 != g2) break;
            }

            // Breed parents to produce children.
            List<Individual> children = config.getBreedingOperator().breed(g1, g2);

            // Mutate the children.
            for (Individual child : children) {
                MutationOperator ms = config.getMutationOperators().get(random.nextInt(config.getMutationOperators().size()));
                ms.mutate(child, changeAmount);
            }

            // Optionally prevent duplicate individuals in the new generation.
            if (config.isPreventDuplicateChildren()) {
                boolean skip = false;
                for (Individual child : children) {
                    for (Individual individual : newGenePool) {
                        if (individual.getHash() == child.getHash()) skip = true;
                    }
                }
                if (skip) {
                    continue;
                }
            }


            // Add children to the new gene pool.
            newGenePool.addAll(children);
        }

        // Replace the old population with the new one.
        genePool = newGenePool;
        allScored = false;

        // Score the new generation.
        scoreGenes(genePool);
    }

    /**
     * Triggers the scoring calculation for each individual in a given pool.
     * This can be run sequentially or in parallel, based on the solver's configuration.
     *
     * @param pool The list of individuals to be scored.
     */
    public void scoreGenes(List<Individual> pool) {
        if (pool == null) pool = getPool();
        if (allScored) return;

        if (config.isParallelScoring()) {
            scoreGenesParallel(pool);
        } else {
            scoreGenesSequential(pool);
        }

        allScored = true;
    }

    /**
     * Retrieves the current population of individuals.
     *
     * @return A list of all individuals in the gene pool.
     */
    public List<Individual> getPool() {
        return genePool;
    }

    /**
     * Scores each individual in sequence.
     */
    private void scoreGenesSequential(List<Individual> pool) {
        pool.forEach(Individual::getScore);
    }

    /**
     * Scores individuals in parallel to improve performance on multi-core systems.
     */
    private void scoreGenesParallel(List<Individual> pool) {
        pool.parallelStream().unordered().forEach(Individual::getScore);
    }


    /**
     * Generates a simple report about the current state of the solver.
     *
     * @return A string containing the report.
     */
    public String report() {
        Individual best = findBestScoringIndividual(genePool);
        return "Pool Size: " + genePool.size();
    }

    /**
     * Retrieves the individual with the best (highest) score from the current population.
     *
     * @return The best-scoring {@link Individual}.
     */
    @Override
    public Individual getBestScoringIndividual() {
        return findBestScoringIndividual(genePool);
    }


    /**
     * Sets the scoring function to be used for evaluating individuals.
     *
     * @param scoreFunction The {@link ScoreFunction} to use.
     */
    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        config.setScoreFunction(scoreFunction);
    }

}