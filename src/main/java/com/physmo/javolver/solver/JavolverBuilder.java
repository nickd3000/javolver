package com.physmo.javolver.solver;

import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.SpeciesCheck;
import com.physmo.javolver.breedingoperator.BreedingOperator;
import com.physmo.javolver.mutationoperator.MutationOperator;
import com.physmo.javolver.selectionoperator.SelectionOperator;

import java.util.function.IntToDoubleFunction;

public class JavolverBuilder {

    Javolver javolver;
    JavolverConfig javolverConfig;

    public JavolverBuilder() {
        javolverConfig = new JavolverConfig();
        javolver = new Javolver(javolverConfig);

    }

    // TODO: add sanity check that all required setup is done.
    public Javolver build() {
        javolver.init();

        if (javolver.getConfig().getScoreFunction() == null) {
            throw new Error("No score function defined.");
        }

        return javolver;
    }

    public JavolverBuilder scoreFunction(ScoreFunction scoreFunction) {
        javolverConfig.setScoreFunction(scoreFunction);
        return this;
    }

    public JavolverBuilder populationTargetSize(int targetSize) {
        javolverConfig.setTargetPopulationSize(targetSize);
        return this;
    }

    public JavolverBuilder dnaSize(int dnaSize) {
        javolverConfig.setDnaSize(dnaSize);
        return this;
    }

    public JavolverBuilder setBreedingOperator(BreedingOperator strategy) {
        javolverConfig.setBreedingOperator(strategy);
        return this;
    }

    public JavolverBuilder setSelectionOperator(SelectionOperator strategy) {
        javolverConfig.setSelectionOperator(strategy);
        return this;
    }

    public JavolverBuilder addMutationOperator(MutationOperator strategy) {
        javolverConfig.getMutationOperators().add(strategy);
        return this;
    }

    public JavolverBuilder keepBestIndividualAlive(boolean val) {
        javolverConfig.setKeepBestIndividualAlive(val);
        return this;
    }

    public JavolverBuilder dnaInitializer(IntToDoubleFunction dnaInitializer) {
        javolverConfig.setDnaInitializer(dnaInitializer);
        return this;
    }

    public JavolverBuilder parallelScoring(boolean val) {
        javolverConfig.setParallelScoring(val);
        return this;
    }

    public JavolverBuilder setSpeciesCheck(SpeciesCheck speciesCheck) {
        javolverConfig.setSpeciesCheck(speciesCheck);
        return this;
    }
}
