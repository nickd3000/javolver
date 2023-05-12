package com.physmo.javolver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRandom;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import org.junit.Test;

import java.util.ArrayList;

public class TestSelectionStrategies {

    @Test
    public void testStrategy() {
        SelectionStrategyRoulette selectionStrategyRoulette = new SelectionStrategyRoulette();
        SelectionStrategyTournament selectionStrategyTournament = new SelectionStrategyTournament(0.2);
        SelectionStrategyRandom selectionStrategyRandom = new SelectionStrategyRandom();

        ArrayList<Individual> pool = (ArrayList<Individual>) Helpers.generateTestPool(50);

        double rouletteScore = 0;
        double rouletteRankedScore = 0;
        double tournamentScore = 0;
        double randomScore = 0;
        double scoreOfWholePopulation = 0;

        int numIterations = 1000000;
        Individual individual;

        for (Individual value : pool) {
            scoreOfWholePopulation += value.getScore();
        }

        for (int i = 0; i < numIterations; i++) {
            rouletteScore += selectionStrategyRoulette.select(pool).getScore();
            tournamentScore += selectionStrategyTournament.select(pool).getScore();
            randomScore += selectionStrategyRandom.select(pool).getScore();
        }

        System.out.println("Average population score    = " + (scoreOfWholePopulation / pool.size()));
        System.out.println("Average rouletteScore       = " + (rouletteScore / numIterations));
        System.out.println("Average tournamentScore     = " + (tournamentScore / numIterations));
        System.out.println("Average randomScore         = " + (randomScore / numIterations));

    }


}
