package com.physmo.javolver;

import com.physmo.javolver.mutationoperator.MutationOperatorRandomize;
import com.physmo.javolver.mutationoperator.MutationOperatorShuffle;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSingle;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import org.junit.Test;

import java.util.ArrayList;

public class TestMutationStrategies {
    @Test
    public void blah() {

        MutationOperatorRandomize mutationStrategyRandomize = new MutationOperatorRandomize(0.5);
        MutationOperatorSimple mutationStrategySimple = new MutationOperatorSimple(2, 0.5);
        MutationOperatorShuffle mutationStrategyShuffle = new MutationOperatorShuffle(2);
        MutationOperatorSwap mutationStrategySwap = new MutationOperatorSwap(0.5, 2);
        MutationOperatorSingle mutationStrategySingle = new MutationOperatorSingle(0.2);

        ArrayList<Individual> pool = (ArrayList<Individual>) Helpers.generateTestPool(50);

        for (int i = 0; i < 1000; i++) {
            mutationStrategyRandomize.mutate(pool.get(0),1);
            mutationStrategySimple.mutate(pool.get(0),1);
            mutationStrategyShuffle.mutate(pool.get(0),1);
            mutationStrategySwap.mutate(pool.get(0),1);
            mutationStrategySingle.mutate(pool.get(0),1);
        }

    }
}
