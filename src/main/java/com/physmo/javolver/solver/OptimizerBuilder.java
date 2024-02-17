package com.physmo.javolver.solver;

import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.mutationoperator.MutationOperator;

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

    public OptimizerBuilder addMutationStrategy(MutationOperator strategy) {
        optimizer.addMutationStrategy(strategy);
        return this;
    }
}
