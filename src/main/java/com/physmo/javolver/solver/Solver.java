package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;

/**
 * An abstract base class for all solver implementations. It provides the fundamental
 * structure and essential methods that every solver must implement.
 * <p>
 * This class manages the iteration count and a "temperature" property, which can be
 * used by subclasses to control aspects like mutation rate.
 */
public abstract class Solver {
    protected int iteration = 0;
    private double temperature = 1;

    /**
     * Initializes the solver, preparing it for the first cycle.
     * This method should be implemented by subclasses to set up the initial population
     * and any other required state.
     */
    public abstract void init();

    // Clear population and rebuild it to the population size.
    public abstract void resetPopulation();

    /**
     * Executes a single cycle or iteration of the solving process.
     * This method increments the iteration counter and then calls the
     * abstract {@link #runOneGeneration()} method.
     */
    public void doOneCycle() {
        iteration++;
        runOneGeneration();
    }


    /**
     * Abstract method to be implemented by subclasses. This method should contain the
     * core logic for running one generation of the solver, which typically includes
     * selection, breeding, and mutation.
     */
    abstract void runOneGeneration();

    /**
     * Retrieves the individual with the best (highest) score from the current population.
     *
     * @return The best-scoring {@link Individual}.
     */
    public abstract Individual getBestScoringIndividual();

    /**
     * Sets the temperature, a value typically between 0.0 and 1.0.
     * The temperature can be used by mutation or other operators to control the
     * magnitude of changes applied to individuals.
     *
     * @param temperature The new temperature value.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the current iteration number of the solver.
     *
     * @return The current iteration count.
     */
    public int getIteration() {
        return iteration;
    }

    /**
     * Sets the scoring function to be used for evaluating individuals.
     *
     * @param scoreFunction The {@link ScoreFunction} to use.
     */
    public abstract void setScoreFunction(ScoreFunction scoreFunction);

    /**
     * Sets the size of the DNA (genome) for each individual in the population.
     *
     * @param size The size of the DNA array.
     */
    public abstract void setDnaSize(int size);
}