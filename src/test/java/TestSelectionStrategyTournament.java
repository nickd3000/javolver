import com.physmo.javolver.Individual;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestSelectionStrategyTournament {
    final int numIterations = 10000;

    @Test
    public void testStrategy() {

        double [] ranges = {0.00001,0.001,0.01,0.05,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};

        for (double range : ranges) {
            double score = testStrategyForRange(range);
            System.out.println("Range: " + range + "  score = " + score);
        }

    }

    public double testStrategyForRange(double selectionRange) {
        SelectionStrategy ss = new SelectionStrategyTournament(selectionRange);
        List<Individual> pool = generateTestPool();
        double scoreSum = 0;

        Individual individual = null;
        for (int i = 0; i < numIterations; i++) {
            individual = ss.select(pool);
            scoreSum += individual.getScore();
        }

        return (double)scoreSum/(double) (numIterations);
    }


    public List<Individual> generateTestPool() {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            pool.add(new Individual(2));
        }
        return pool;
    }
}
