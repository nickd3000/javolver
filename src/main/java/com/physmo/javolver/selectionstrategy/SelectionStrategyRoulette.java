package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;

public class SelectionStrategyRoulette implements SelectionStrategy {

    @Override
    public Individual select(List<Individual> pool) {
        double totalScore = 0;
        double runningScore = 0;
        double nextScore;
        for (Individual g : pool) {
            totalScore += g.getScoreSquared();
        }

        double rnd = (Math.random() * totalScore);

        for (Individual g : pool) {
            nextScore = runningScore + g.getScoreSquared();
            if (rnd >= runningScore && rnd <= nextScore) {
                return g;
            }
            runningScore = nextScore;
        }

        return null;
    }

}
