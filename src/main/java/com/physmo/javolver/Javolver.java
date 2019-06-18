package com.physmo.javolver;

import com.physmo.javolver.breedingstrategy.BreedingStrategy;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

import java.util.ArrayList;
import java.util.List;


/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 * @version 1.0
 * @since 2016-04-01
 */
// TODO: make list types use interface type instead of ArrayList etc.

public class Javolver {

    // Keep the best individual alive between generations.
    private boolean keepBestIndividualAlive = false;

    // Use multi-threading for the scoring process.
    // Set this to true if your {@link Individual#calculateScore()} method
    // is expensive to run and may benefit from parallelization.
    private boolean parallelScoring = false;


    public Javolver keepBestIndividualAlive(boolean val) {
        this.keepBestIndividualAlive = val;
        return this;
    }

    public Javolver parallelScoring(boolean val) {
        this.parallelScoring = val;
        return this;
    }

    private ArrayList<Individual> genePool = new ArrayList<>();
    private final Individual proto; // Copy of type of chromosome we will use.
    private boolean allScored = false;

    private BreedingStrategy breedingStrategy = null;
    private SelectionStrategy selectionStrategy = null;
    private final List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();

    public ArrayList<Individual> getPool() {
        return genePool;
    }

    /**
     * Default constructor.
     *
     * @param proto A subclassed object from Individual, from which to clone the other members of the generation.
     */
    private Javolver(Individual proto) {
        this.proto = proto;

    }

    /**
     * Apply some sensible default strategies.
     * @return
     */
    public Javolver setDefaultStrategies() {

        breedingStrategy = new BreedingStrategyUniform();

        selectionStrategy = new SelectionStrategyTournament(0.15);

        mutationStrategies.add(new MutationStrategySimple(0.1, 0.012));

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
     * Create Javolver object with prototype individual and set the population size.
     *
     * @param proto          A subclassed object from Individual, from which to clone the other members of the generation.
     * @param populationSize Required population size.
     */
    public Javolver(Individual proto, int populationSize) {
        this(proto);
        increasePopulation(populationSize);
    }


    /**
     * Find the best score of any individual in the current generation.
     *
     * @return The best score of any individual in the current generation.
     */
    public double getBestScore(ArrayList<Individual> pool) {
        if (pool==null) pool = genePool;
        return findBestScoringIndividual(pool).getScore();
    }
    public double getBestScore() {
        return findBestScoringIndividual(genePool).getScore();
    }


    /**
     * Add a number of randomly initialized genes to the population, until it reaches specified size.
     *
     * @param targetCount The target number of individuals that the population will reach.
     */
    private void increasePopulation(int targetCount) {
        Individual n = null;
        int target = targetCount - genePool.size();
        if (target < 1) return;

        for (int i = 0; i < target; i++) {
            n = proto.clone();

            genePool.add(n);
        }
    }


    /**
     * The main function that does most of the work to evolve the system.<br>
     * 1. All individuals scoring mechanisms get called.<br>
     * 2. The best scoring individual is automatically moved to the next generation.<br>
     * 3. A new generation of individuals is created by breeding selected member from the current generation.<br>
     * <br>
     * The size of the new pool will match the previous generation population.
     */
    public void doOneCycle() {
        // Request that all individuals perform scoring.
        scoreGenes(genePool);

        JavolverRanking.calculateFitnessRank(genePool);
        JavolverRanking.calculateDiversityRank(genePool);

        ArrayList<Individual> newGenePool = null;

        newGenePool = new ArrayList<Individual>();

        int targetPop = genePool.size();

        // Elitism - keep the best individual in the new pool.
        if (keepBestIndividualAlive) {
            Individual bestScorer = findBestScoringIndividual(genePool);
            bestScorer.processed=false;
            newGenePool.add(bestScorer);
        }

        Individual g1 = null, g2 = null;

        while (newGenePool.size() < targetPop) {
            g1 = g2 = null;

            // Select parents
            for (int ii = 0; ii < 100; ii++) {

                g1 = selectionStrategy.select(genePool);
                g2 = selectionStrategy.select(genePool);

                if (g1!=null && g2!=null && g1!=g2) break;
            }

            // Breed
            List<Individual> children = breedingStrategy.breed(g1, g2);

            // Mutate children.
            for (Individual child : children) {
                for (MutationStrategy ms : mutationStrategies) {
                    ms.mutate(child);
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
     * Return a string containing some basic information about the state of the system.
     * @return String containing simple report
     */
    public String report() {
        Individual best = findBestScoringIndividual(genePool);
        return "Pool Size: " + genePool.size() + " Best: " + best.toString();
    }

    /***
     * Triggers each individual in the pool to calculate it's score.
     * The sequential or parallel method is used depending on config settings.
     * @param    pool    ArrayList of individuals to be scored.
     */
    public void scoreGenes(ArrayList<Individual> pool) {
        if (pool==null) pool=getPool();

        if (allScored) return;

        if (parallelScoring) {
            scoreGenesParallel(genePool);
        } else {
            scoreGenesSequential(genePool);
        }

        allScored = true;
    }

    /**
     * Score each individual in turn.
     * @param pool
     */
    private void scoreGenesSequential(ArrayList<Individual> pool) {
        for (Individual gene : pool) {
            gene.getScore();
        }
    }

    /**
     * Score each individual in parallel.
     * @param pool
     */
    private void scoreGenesParallel(ArrayList<Individual> pool) {
        pool.parallelStream().unordered().forEach(Individual::getScore);
    }


    /***
     * Search the supplied pool of individuals and return the highest scoring one.
     * @return Highest scoring member of the supplied list.
     */
    public Individual findBestScoringIndividual() {
        return findBestScoringIndividual(genePool);
    }
    public Individual findBestScoringIndividual(ArrayList<Individual> pool) {
        double highestScore = 0.0f;
        Individual highestGene = pool.get(0);
        for (Individual gene : pool) {
            if (gene.getScore() > highestScore) {
                highestGene = gene;
                highestScore = gene.getScore();
            }
        }
        return highestGene;
    }


}
