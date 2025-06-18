package com.physmo.javolver.solver;

import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.breedingoperator.BreedingOperator;
import com.physmo.javolver.mutationoperator.MutationOperator;
import com.physmo.javolver.selectionoperator.SelectionOperator;

import java.util.function.IntToDoubleFunction;

/**
 * A builder class for constructing and configuring a {@link Javolver} instance.
 * This class provides a fluent API for setting up the various components
 * of the genetic algorithm solver.
 */
public class JavolverBuilder {

    final Javolver javolver;
    final JavolverConfig javolverConfig;

    public JavolverBuilder() {
        javolverConfig = new JavolverConfig();
        javolver = new Javolver(javolverConfig);
    }

    /**
     * Builds the Javolver instance with the specified configuration.
     *
     * @return The configured {@link Javolver} instance.
     * @throws Error if essential components like the score function are not defined.
     */
    public Javolver build() {
        javolver.init();

        if (javolver.getConfig().getScoreFunction() == null) {
            throw new Error("No score function defined.");
        }

        return javolver;
    }

    /**
     * Sets the score function used to evaluate the fitness of each individual.
     *
     * @param scoreFunction The score function to use.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder scoreFunction(ScoreFunction scoreFunction) {
        javolverConfig.setScoreFunction(scoreFunction);
        return this;
    }

    /**
     * Sets the target size of the population.
     *
     * @param targetSize The desired number of individuals in the population.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder populationTargetSize(int targetSize) {
        javolverConfig.setTargetPopulationSize(targetSize);
        return this;
    }

    /**
     * Sets the size of the DNA for each individual.
     *
     * @param dnaSize The length of the DNA array.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder dnaSize(int dnaSize) {
        javolverConfig.setDnaSize(dnaSize);
        return this;
    }

    /**
     * Sets the breeding operator, which defines how parent DNA is combined.
     *
     * @param strategy The breeding strategy to use.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder setBreedingOperator(BreedingOperator strategy) {
        javolverConfig.setBreedingOperator(strategy);
        return this;
    }

    /**
     * Sets the selection operator, which defines how individuals are chosen for breeding.
     *
     * @param strategy The selection strategy to use.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder setSelectionOperator(SelectionOperator strategy) {
        javolverConfig.setSelectionOperator(strategy);
        return this;
    }

    /**
     * Adds a mutation operator to the solver. Multiple mutation operators can be added.
     *
     * @param strategy The mutation strategy to add.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder addMutationOperator(MutationOperator strategy) {
        javolverConfig.getMutationOperators().add(strategy);
        return this;
    }

    /**
     * Configures whether to keep the best individual from one generation to the next.
     * This is also known as "elitism".
     *
     * @param val True to keep the best individual, false otherwise.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder keepBestIndividualAlive(boolean val) {
        javolverConfig.setKeepBestIndividualAlive(val);
        return this;
    }

    /**
     * Sets the function used to initialize the DNA of the first generation of individuals.
     *
     * @param dnaInitializer The function to initialize DNA.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder dnaInitializer(IntToDoubleFunction dnaInitializer) {
        javolverConfig.setDnaInitializer(dnaInitializer);
        return this;
    }

    /**
     * Enables or disables parallel scoring of individuals.
     * This can improve performance on multi-core systems.
     *
     * @param val True to enable parallel scoring, false otherwise.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder parallelScoring(boolean val) {
        javolverConfig.setParallelScoring(val);
        return this;
    }

    /**
     * Sets a species check, allowing the solver to handle speciation.
     * Speciation can help maintain diversity in the population.
     *
     * @param speciesCheck The species check to use.
     * @return This builder instance for chaining.
     */
    public JavolverBuilder setSpeciesCheck(SpeciesCheck speciesCheck) {
        javolverConfig.setSpeciesCheck(speciesCheck);
        return this;
    }
}