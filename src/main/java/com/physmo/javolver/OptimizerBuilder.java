package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategy;

public class OptimizerBuilder {

    Optimizer optimizer;

    public OptimizerBuilder() {
        optimizer = new Optimizer();
    }

    public Optimizer build() {
        optimizer.init();
        return optimizer;
    }

    public OptimizerBuilder dnaSize(int dnaSize) {
        optimizer.setDnaSize(dnaSize);
        return this;
    }

    public OptimizerBuilder scoreFunction(ScoreFunction scoreFunction) {
        optimizer.setScoreFunction(scoreFunction);
        return this;
    }

    public OptimizerBuilder addMutationStrategy(MutationStrategy strategy) {
        optimizer.addMutationStrategy(strategy);
        return this;
    }
}
