import com.physmo.javolver.Individual;
import com.physmo.javolver.JavolverRanking;
import com.physmo.javolver.selectionstrategy.SelectionStrategy;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRandom;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestSelectionStrategyRoulette {

    @Test
    public void testStrategy() {
        SelectionStrategy ss = new SelectionStrategyRoulette();
        SelectionStrategy ssRRanked = new SelectionStrategyRouletteRanked();
        SelectionStrategy ssr = new SelectionStrategyRandom();

        ArrayList<Individual> pool = (ArrayList<Individual>) generateTestPool(5);

        JavolverRanking.calculateFitnessRank(pool);

        double scoreSumRouletteNormal = 0;
        double scoreSumRouletteRanked = 0;
        double scoreSumBaseline = 0;
        int numIterations = 100000;
        Individual individual = null;
        for (int i = 0; i < numIterations; i++) {
            individual = ss.select(pool);
            scoreSumRouletteNormal += individual.getScore();
            individual = ssRRanked.select(pool);
            scoreSumRouletteRanked += individual.getScore();
            individual = ssr.select(pool);
            scoreSumBaseline += individual.getScore();
        }

        System.out.println("Average score   = " + (scoreSumRouletteNormal / numIterations) );
        System.out.println("Average score R = " + (scoreSumRouletteRanked / numIterations) );
        System.out.println(" (baseline "+(scoreSumBaseline / numIterations)+")");
    }

    public List<Individual> generateTestPool(int size) {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            pool.add(new Individual(2));
        }
        return pool;
    }
}
