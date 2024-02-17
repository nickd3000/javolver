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

public class JavolverConfig {
    private final List<MutationOperator> mutationOperators = new ArrayList<>();
    private final boolean preventDuplicateChildren = false;
    private IntToDoubleFunction dnaInitializer = null;
    private BreedingOperator breedingOperator = null;
    private SelectionOperator selectionOperator = null;
    private boolean keepBestIndividualAlive = false;
    private boolean parallelScoring = false;
    private ScoreFunction scoreFunction;
    private int targetPopulationSize = 0;
    private int dnaSize = 0;
    private double changeAmount = 1;
    private SpeciesCheck speciesCheck = null;

    /**
     * Apply some sensible default strategies.
     *
     */
    public void setDefaultOperators() {

        breedingOperator = new BreedingOperatorUniform();

        selectionOperator = new SelectionOperatorTournament(0.15);

        mutationOperators.add(new MutationOperatorSimple(1, 0.012));

    }

    public SpeciesCheck getSpeciesCheck() {
        return speciesCheck;
    }

    public void setSpeciesCheck(SpeciesCheck speciesCheck) {
        this.speciesCheck = speciesCheck;
    }

    public List<MutationOperator> getMutationOperators() {
        return mutationOperators;
    }

    public boolean isPreventDuplicateChildren() {
        return preventDuplicateChildren;
    }

    public IntToDoubleFunction getDnaInitializer() {
        return dnaInitializer;
    }

    public void setDnaInitializer(IntToDoubleFunction dnaInitializer) {
        this.dnaInitializer = dnaInitializer;
    }

    public BreedingOperator getBreedingOperator() {
        return breedingOperator;
    }

    public void setBreedingOperator(BreedingOperator breedingOperator) {
        this.breedingOperator = breedingOperator;
    }

    public SelectionOperator getSelectionOperator() {
        return selectionOperator;
    }

    public void setSelectionOperator(SelectionOperator selectionOperator) {
        this.selectionOperator = selectionOperator;
    }

    public boolean isKeepBestIndividualAlive() {
        return keepBestIndividualAlive;
    }

    public void setKeepBestIndividualAlive(boolean keepBestIndividualAlive) {
        this.keepBestIndividualAlive = keepBestIndividualAlive;
    }

    public boolean isParallelScoring() {
        return parallelScoring;
    }

    public void setParallelScoring(boolean parallelScoring) {
        this.parallelScoring = parallelScoring;
    }

    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }

    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    public int getTargetPopulationSize() {
        return targetPopulationSize;
    }

    public void setTargetPopulationSize(int targetPopulationSize) {
        this.targetPopulationSize = targetPopulationSize;
    }

    public int getDnaSize() {
        return dnaSize;
    }

    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }
}
