package com.physmo.javolver;

import com.physmo.javolver.breedingstrategy.BreedingStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;

public class JavolverBuilder {

    Javolver javolver;

    public JavolverBuilder() {
        javolver = new Javolver();
    }

    public Javolver build() {
        javolver.init();
        return javolver;
    }

    public JavolverBuilder scoreFunction(ScoreFunction scoreFunction) {
        javolver.setScoreFunction(scoreFunction);
        return this;
    }

    public JavolverBuilder populationTargetSize(int targetSize) {
        javolver.setTargetPopulationSize(targetSize);
        return this;
    }

    public JavolverBuilder dnaSize(int dnaSize) {
        javolver.setDnaSize(dnaSize);
        return this;
    }

    public JavolverBuilder setBreedingStrategy(BreedingStrategy strategy) {
        javolver.setBreedingStrategy(strategy);
        return this;
    }

    public JavolverBuilder setSelectionStrategy(SelectionStrategy strategy) {
        javolver.setSelectionStrategy(strategy);
        return this;
    }

    public JavolverBuilder addMutationStrategy(MutationStrategy strategy) {
        javolver.addMutationStrategy(strategy);
        return this;
    }

    public JavolverBuilder keepBestIndividualAlive(boolean val) {
        javolver.keepBestIndividualAlive(val);
        return this;
    }
}
