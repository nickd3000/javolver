package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;
import java.util.Random;

public class SelectionStrategyTournament implements SelectionStrategy {

    private final double selectionRange;
    private final Random random = new Random();

    public SelectionStrategyTournament(double selectionRange) {
        this.selectionRange = selectionRange;
    }

    @Override
    public Individual select(List<Individual> pool) {
        int poolSize = pool.size();
        double maxScore = -1000;
        Individual currentWinner = getRandomIndividual(pool);

        double tournamentSize = selectionRange;

        int tSize = (int) (tournamentSize * (double) poolSize);
        if (tSize < 2) tSize = 2;

        for (int i = 0; i < tSize; i++) {
            Individual contender = getRandomIndividual(pool);

            if (getSelectionScore(contender) > maxScore || maxScore == -1000) {
                maxScore = getSelectionScore(contender);
                currentWinner = contender;
            }
        }

        return currentWinner;
    }


    private double getSelectionScore(Individual ind) {
        return ind.getScore();
    }

    private Individual getRandomIndividual(List<Individual> pool) {
        //int id = (int) ((float) (pool.size() - 1) * Math.random());
        int id = random.nextInt(pool.size());
        return pool.get(id);
    }
}
