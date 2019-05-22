import com.physmo.javolver.Individual;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRandom;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestSelectionStrategyRoulette {

    @Test
    public void testStrategy() {
        SelectionStrategy ss = new SelectionStrategyRoulette();
        SelectionStrategy ssr = new SelectionStrategyRandom();

        List<Individual> pool = generateTestPool();

        double scoreSum = 0;
        double scoreSumBaseline = 0;
        int numIterations = 100000;
        Individual individual = null;
        for (int i = 0; i < numIterations; i++) {
            individual = ss.select(pool);
            scoreSum += individual.getScore();
            individual = ssr.select(pool);
            scoreSumBaseline += individual.getScore();
        }

        System.out.println("Average score = " + (scoreSum / numIterations) + " (baseline "+(scoreSumBaseline / numIterations)+")");
    }

    public List<Individual> generateTestPool() {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            pool.add(new TestGene(i));
        }
        return pool;
    }
}
