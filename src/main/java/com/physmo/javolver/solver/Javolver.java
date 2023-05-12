package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.breedingstrategy.BreedingStrategy;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.IntToDoubleFunction;


/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 * @version 1.0
 * @since 2016-04-01
 */
public class Javolver implements Solver {

    private List<Individual> genePool = new ArrayList<>();
    private final List<MutationStrategy> mutationStrategies = new ArrayList<>();
    private BreedingStrategy breedingStrategy = null;
    private SelectionStrategy selectionStrategy = null;

    IntToDoubleFunction dnaInitializer = null;
    Random random = new Random();
    // Keep the best individual alive between generations.
    private boolean keepBestIndividualAlive = false;
    // Use multi-threading for the scoring process.
    private boolean parallelScoring = false;
    private ScoreFunction scoreFunction;

    private boolean allScored = false;

    private int targetPopulationSize = 0;
    private int dnaSize = 0;
    private int iteration = 0;
    private double changeAmount = 1;
    private boolean preventDuplicateChildren = false;
    private SpeciesCheck speciesCheck = null;

    /**
     * Create Javolver object with prototype individual and set the population size.
     */
    public Javolver() {

    }

    public static JavolverBuilder builder() {
        return new JavolverBuilder();
    }

    @Override
    public int getIteration() {
        return iteration;
    }


    @Override
    public void init() {
        increasePopulation(targetPopulationSize);
    }

    /**
     * Add a number of randomly initialized genes to the population, until it reaches specified size.
     *
     * @param targetCount The target number of individuals that the population will reach.
     */
    private void increasePopulation(int targetCount) {
        Individual n;
        int target = targetCount - genePool.size();
        if (target < 1) return;

        for (int i = 0; i < target; i++) {
            n = new Individual(dnaSize);
            n.setScoreFunction(scoreFunction);
            if (dnaInitializer != null) n.getDna().initFromFunction(dnaInitializer);
            genePool.add(n);
        }
    }

    public int getDnaSize() {
        return dnaSize;
    }

    @Override
    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    public int getTargetPopulationSize() {
        return targetPopulationSize;
    }

    public void setTargetPopulationSize(int targetPopulationSize) {
        this.targetPopulationSize = targetPopulationSize;
    }

    public void keepBestIndividualAlive(boolean val) {
        keepBestIndividualAlive = val;
    }

    public void parallelScoring(boolean val) {
        parallelScoring = val;
    }

    /**
     * Apply some sensible default strategies.
     *
     * @return
     */
    public Javolver setDefaultStrategies() {

        breedingStrategy = new BreedingStrategyUniform();

        selectionStrategy = new SelectionStrategyTournament(0.15);

        mutationStrategies.add(new MutationStrategySimple(1, 0.012));

        return this;
    }

    public Javolver setBreedingStrategy(BreedingStrategy strategy) {
        this.breedingStrategy = strategy;
        return this;
    }

    public Javolver setSelectionStrategy(SelectionStrategy strategy) {
        this.selectionStrategy = strategy;
        return this;
    }

    public Javolver addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
        return this;
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
    public void doOneCycle() {
        iteration++;

        // Request that all individuals perform scoring.
        scoreGenes(genePool);

        ArrayList<Individual> newGenePool = new ArrayList<>();

        int targetPop = genePool.size();

        // Elitism - keep the best individual in the new pool.
        if (keepBestIndividualAlive) {
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

                g1 = selectionStrategy.select(genePool);
                g2 = selectionStrategy.select(genePool);


                if (speciesCheck != null && !speciesCheck.isSameSpecies(g1, g2) && speciationClashes < 50) {
                    speciationClashes++;
                    continue;
                }

                if (g1 != null && g2 != null && g1 != g2) break;
            }

            // Breed
            List<Individual> children = breedingStrategy.breed(g1, g2);

            // Mutate children.
            for (Individual child : children) {
                MutationStrategy ms = mutationStrategies.get(random.nextInt(mutationStrategies.size()));
                ms.mutate(child, changeAmount);
            }

            if (preventDuplicateChildren) {
                boolean skip = false;
                for (Individual child : children) {
                    for (Individual individual : newGenePool) {
                        if (individual.getHash() == child.getHash()) skip = true;
                    }
                }
                if (skip == true) {
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

        if (parallelScoring) {
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
    public void setTemperature(double temperature) {
        this.changeAmount = temperature;
    }

    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    public void setSpeciesCheck(SpeciesCheck speciesCheck) {
        this.speciesCheck = speciesCheck;
    }

    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }

    public void setDnaInitializer(IntToDoubleFunction dnaInitializer) {
        this.dnaInitializer = dnaInitializer;
    }
}
