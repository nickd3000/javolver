package com.physmo.javolver.solver;

import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.breedingoperator.BreedingOperator;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperator;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperator;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntToDoubleFunction;

/**
 * JavolverConfig stores configuration options for running a genetic algorithm using Javolver.
 * <p>
 * It allows you to set operators for mutation, selection, and breeding, as well as various evolution parameters.
 */
public class JavolverConfig {

    // List of mutation operators to apply during evolution.
    private final List<MutationOperator> mutationOperators = new ArrayList<>();

    // If true, prevents duplicate children from being created. (Currently fixed as false.)
    private final boolean preventDuplicateChildren = false;

    // Function for initializing individual DNA.
    private IntToDoubleFunction dnaInitializer = null;

    // Operator controlling how parents are combined to produce children.
    private BreedingOperator breedingOperator = null;

    // Operator controlling how individuals are selected for breeding.
    private SelectionOperator selectionOperator = null;

    // If true, the highest scoring individual will survive between generations (elitism).
    private boolean keepBestIndividualAlive = false;

    // If true, individual scoring is performed in parallel for better performance.
    private boolean parallelScoring = false;

    // Function to determine the fitness score of an individual.
    private ScoreFunction scoreFunction;

    // Desired number of individuals in the population.
    private int targetPopulationSize = 0;

    // Length of the DNA array for each individual.
    private int dnaSize = 0;

    // Maximum allowed change when mutating DNA.
    private double changeAmount = 1;

    // Optional function to check whether two individuals belong to the same species.
    private SpeciesCheck speciesCheck = null;

    /**
     * Sets up sensible default operators for breeding, selection and mutation.
     * <ul>
     *     <li>Breeding: Uniform crossover</li>
     *     <li>Selection: Tournament (with probability 0.15)</li>
     *     <li>Mutation: Simple bitwise mutation (amount=1, rate=0.012)</li>
     * </ul>
     */
    public void setDefaultOperators() {
        breedingOperator = new BreedingOperatorUniform();
        selectionOperator = new SelectionOperatorTournament(0.15);
        mutationOperators.add(new MutationOperatorSimple(1, 0.012));
    }

    /**
     * @return the SpeciesCheck function, if any. Used to group individuals by species.
     */
    public SpeciesCheck getSpeciesCheck() {
        return speciesCheck;
    }

    /**
     * Set a SpeciesCheck function, which can be used for speciation of individuals.
     * @param speciesCheck implementation of the check
     */
    public void setSpeciesCheck(SpeciesCheck speciesCheck) {
        this.speciesCheck = speciesCheck;
    }

    /**
     * @return a list of mutation operators used in this configuration. 
     */
    public List<MutationOperator> getMutationOperators() {
        return mutationOperators;
    }

    /**
     * @return whether duplicate children are prevented (always false in current version).
     */
    public boolean isPreventDuplicateChildren() {
        return preventDuplicateChildren;
    }

    /**
     * @return function which initializes the DNA of individuals.
     */
    public IntToDoubleFunction getDnaInitializer() {
        return dnaInitializer;
    }

    /**
     * Set the function for initializing DNA values.
     * @param dnaInitializer an IntToDoubleFunction for DNA values
     */
    public void setDnaInitializer(IntToDoubleFunction dnaInitializer) {
        this.dnaInitializer = dnaInitializer;
    }

    /**
     * @return the breeding operator that controls how parents mix their DNA.
     */
    public BreedingOperator getBreedingOperator() {
        return breedingOperator;
    }

    /**
     * Set the operator controlling how parents are combined to produce children.
     * @param breedingOperator the breeding operator
     */
    public void setBreedingOperator(BreedingOperator breedingOperator) {
        this.breedingOperator = breedingOperator;
    }

    /**
     * @return the selection operator used to choose individuals for breeding.
     */
    public SelectionOperator getSelectionOperator() {
        return selectionOperator;
    }

    /**
     * Set the operator that selects individuals from the population for breeding.
     * @param selectionOperator the selection operator
     */
    public void setSelectionOperator(SelectionOperator selectionOperator) {
        this.selectionOperator = selectionOperator;
    }

    /**
     * @return true if the best individual survives to the next generation (elitism).
     */
    public boolean isKeepBestIndividualAlive() {
        return keepBestIndividualAlive;
    }

    /**
     * Set whether the best individual survives between generations.
     * @param keepBestIndividualAlive true for elitism, false otherwise
     */
    public void setKeepBestIndividualAlive(boolean keepBestIndividualAlive) {
        this.keepBestIndividualAlive = keepBestIndividualAlive;
    }

    /**
     * @return true if scoring runs in parallel for faster evolution.
     */
    public boolean isParallelScoring() {
        return parallelScoring;
    }

    /**
     * Set whether individual scoring should be performed in parallel.
     * Can give a performance boost for large populations.
     * @param parallelScoring boolean value
     */
    public void setParallelScoring(boolean parallelScoring) {
        this.parallelScoring = parallelScoring;
    }

    /**
     * @return the fitness score function.
     */
    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }

    /**
     * Set the score/fitness function for individuals.
     * @param scoreFunction implementation of fitness evaluation
     */
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    /**
     * @return the desired population size.
     */
    public int getTargetPopulationSize() {
        return targetPopulationSize;
    }

    /**
     * Set the desired population size.
     * @param targetPopulationSize number of individuals
     */
    public void setTargetPopulationSize(int targetPopulationSize) {
        this.targetPopulationSize = targetPopulationSize;
    }

    /**
     * @return the length of DNA arrays for individuals.
     */
    public int getDnaSize() {
        return dnaSize;
    }

    /**
     * Set the length of the DNA for each individual.
     * @param dnaSize size of the DNA array
     */
    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    /**
     * @return the change amount parameter, used in mutation calculations.
     */
    public double getChangeAmount() {
        return changeAmount;
    }

    /**
     * Set the maximum allowed change for DNA mutations.
     * @param changeAmount maximum DNA mutation change
     */
    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }
}