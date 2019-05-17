package com.physmo.javolver.examples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class TestProgram {
    static int populationSize = 5000;

    public static void main(String[] args) {

        BasicDisplay bd = new BasicDisplayAwt(100, 100);

        Javolver testEvolver = new Javolver(new GeneProgram(), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.3, 0.3))
                .addMutationStrategy(new MutationStrategySwap(0.1,1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyUniform());


        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            bd.startTimer();

            for (int i = 0; i < 100; i++) {
                testEvolver.doOneCycle();
                iteration++;
            }

            System.out.print("iteration: " + iteration + "  Time in ms: " + bd.getEllapsedTime() + "  ");
            testEvolver.report();
            System.out.println(testEvolver.findBestScoringIndividual(null).toString());

        }

        System.out.print("END ");
    }

}
