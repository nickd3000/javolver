package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.mutationoperator.MutationOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

// TODO: Would it be better to move all configuration to a different class?

/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 * @version 1.0
 * @since 2016-04-01
 */
public class Javolver extends Solver {

    Random random = new Random();
    private List<Individual> genePool = new ArrayList<>();
    private boolean allScored = false;
    //private int iteration = 0;
    private final double changeAmount = 1;
    private SpeciesCheck speciesCheck = null;
    private JavolverConfig config;

    /**
     * Create Javolver object with prototype individual and set the population size.
     */
    public Javolver(JavolverConfig javolverConfig) {
        this.config = javolverConfig;
    }

    /**
     * Builder for Javolver
     *
     * @return
     */
    public static JavolverBuilder builder() {
        return new JavolverBuilder();
    }

    public JavolverConfig getConfig() {
        return config;
    }

    public void setConfig(JavolverConfig config) {
        this.config = config;
    }

    /**
     * Number of iteration performed.
     *
     * @return Number of iterations so far
     */
    @Override
    public int getIteration() {
        return iteration;
    }


    @Override
    public void init() {
        increasePopulation(config.getTargetPopulationSize());
    }

    /**
     * Add a number of randomly initialized genes to the population, until it reaches specified size.
     *
     * @param targetCount The target number of individuals that the population will reach.
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

    public int getDnaSize() {
        return config.getDnaSize();
    }

    @Override
    public void setDnaSize(int dnaSize) {
        config.setDnaSize(dnaSize);
    }


    /**
     * Find the best score of any individual in the current generation.
     *
     * @return The best score of any individual in the current generation.
     */
    public double getBestScore(List<Individual> pool) {
        if (pool == null) pool = genePool;
        return findBestScoringIndividual(pool).getScore();
    }

    public Individual findBestScoringIndividual(List<Individual> pool) {
        return pool.stream().max(Comparator.comparing(Individual::getScore)).get();
    }

    public double getBestScore() {
        return findBestScoringIndividual(genePool).getScore();
    }

    /**
     * The main function that does most of the work to evolve the system.<br>
     * 1. All individuals scoring mechanisms get called.<br>
     * 2. The best scoring individual is automatically moved to the next generation.<br>
     * 3. A new generation of individuals is created by breeding selected member from the current generation.<br>
     * <br>
     * The size of the new pool will match the previous generation population.
     */
    @Override
    public void runOneGeneration() {
        iteration++;

        // Request that all individuals perform scoring.
        scoreGenes(genePool);

        ArrayList<Individual> newGenePool = new ArrayList<>();

        int targetPop = genePool.size();

        // Elitism - keep the best individual in the new pool.
        if (config.isKeepBestIndividualAlive()) {
            Individual bestScorer = findBestScoringIndividual(genePool);
            bestScorer.setProcessed(false);
            newGenePool.add(bestScorer);
        }

        Individual g1, g2;

        while (newGenePool.size() < targetPop) {
            g1 = g2 = null;
            int speciationClashes = 0;
            // Select parents
            for (int ii = 0; ii < 100; ii++) {

                g1 = config.getSelectionOperator().select(genePool);
                g2 = config.getSelectionOperator().select(genePool);


                if (speciesCheck != null && !speciesCheck.isSameSpecies(g1, g2) && speciationClashes < 50) {
                    speciationClashes++;
                    continue;
                }

                if (g1 != null && g2 != null && g1 != g2) break;
            }

            // Breed
            List<Individual> children = config.getBreedingOperator().breed(g1, g2);

            // Mutate children.
            for (Individual child : children) {
                MutationOperator ms = config.getMutationOperators().get(random.nextInt(config.getMutationOperators().size()));
                ms.mutate(child, changeAmount);
            }

            if (config.isPreventDuplicateChildren()) {
                boolean skip = false;
                for (Individual child : children) {
                    for (Individual individual : newGenePool) {
                        if (individual.getHash() == child.getHash()) skip = true;
                    }
                }
                if (skip) {
                    //System.out.println("skipping");
                    continue;
                }
            }


            // Add children to new gene pool.
            newGenePool.addAll(children);
        }

        // Copy new pool over main pool.
        genePool = newGenePool;
        allScored = false;

        // Request that all individuals perform scoring.
        scoreGenes(genePool);
    }

    /***
     * Triggers each individual in the pool to calculate it's score.
     * The sequential or parallel method is used depending on config settings.
     * @param    pool    ArrayList of individuals to be scored.
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

    public List<Individual> getPool() {
        return genePool;
    }

    /**
     * Score each individual in turn.
     */
    private void scoreGenesSequential(List<Individual> pool) {
        pool.forEach(Individual::getScore);
    }

    /**
     * Score each individual in parallel.
     *
     * @param pool
     */
    private void scoreGenesParallel(List<Individual> pool) {
        pool.parallelStream().unordered().forEach(Individual::getScore);
    }


    /***
     * Return a string containing some basic information about the state of the system.
     * @return String containing simple report
     */
    public String report() {
        Individual best = findBestScoringIndividual(genePool);
        return "Pool Size: " + genePool.size();
    }

    /***
     * Search the supplied pool of individuals and return the highest scoring one.
     * @return Highest scoring member of the supplied list.
     */
    @Override
    public Individual getBestScoringIndividual() {
        return findBestScoringIndividual(genePool);
    }


    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        config.setScoreFunction(scoreFunction);
    }

}
