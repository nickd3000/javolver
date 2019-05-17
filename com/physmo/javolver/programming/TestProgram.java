package com.physmo.javolver.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class TestProgram {
    static int populationSize = 250;
    static int batchSize = 10;


    public static void main(String[] args) {

        BasicDisplay bd = new BasicDisplayAwt(400, 400);

        Javolver testEvolver = new Javolver(new GeneProgram(FunctionEvaluator.class), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 2.3))
                .addMutationStrategy(new MutationStrategySwap(0.1,2))
                .addMutationStrategy(new MutationStrategyRandomize(0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyUniform());


        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            bd.startTimer();

            for (int i = 0; i < batchSize; i++) {
                testEvolver.doOneCycle();
                iteration++;
            }

            System.out.print("iteration: " + iteration + "  Time in ms: " + bd.getEllapsedTime() + "  ");
            testEvolver.report();
            System.out.println(testEvolver.findBestScoringIndividual(null).toString());

            Individual ind = testEvolver.findBestScoringIndividual(null);
            ((GeneProgram)ind).render(bd);

        }

        System.out.print("END ");
    }

}
