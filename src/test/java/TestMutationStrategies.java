import com.physmo.javolver.Individual;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategyShuffle;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import org.junit.Test;

import java.util.ArrayList;

public class TestMutationStrategies {
    @Test
    public void blah() {

        MutationStrategyRandomize mutationStrategyRandomize = new MutationStrategyRandomize(0.5);
        MutationStrategySimple mutationStrategySimple = new MutationStrategySimple(2,0.5);
        MutationStrategyShuffle mutationStrategyShuffle = new MutationStrategyShuffle(2);
        MutationStrategySwap mutationStrategySwap = new MutationStrategySwap(0.5,2);
        MutationStrategySingle mutationStrategySingle = new MutationStrategySingle(0.2);

        ArrayList<Individual> pool = (ArrayList<Individual>) Helpers.generateTestPool(50);

        for (int i=0;i<1000;i++) {
            mutationStrategyRandomize.mutate(pool.get(0));
            mutationStrategySimple.mutate(pool.get(0));
            mutationStrategyShuffle.mutate(pool.get(0));
            mutationStrategySwap.mutate(pool.get(0));
            mutationStrategySingle.mutate(pool.get(0));
        }

    }
}
