import com.physmo.javolver.Individual;
import com.physmo.javolver.JavolverRanking;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestJavolverRanking {

    @Test
    public void testStrategy() {
        int poolSize = 500;

        ArrayList<Individual>pool = (ArrayList<Individual>) generateTestPool(poolSize);

        System.out.println("Before ranking:");
        for (Individual i : pool) {
            reportIndividual(i);
        }

        System.out.println("\nAfter ranking:");
        JavolverRanking.calculateFitnessRank(pool);

        for (Individual i : pool) {
            reportIndividual(i);
        }

        assertEquals(pool.get(0).getClass(), DummyGeneRandom.class);
        assertEquals(pool.get(0).getRankScore(),1);
        assertEquals(pool.get(poolSize-1).getRankScore(), poolSize);
    }

    @Test
    public void testThatTopRankedIndividualIsAlsoTopScoringIndividual() {
        int poolSize = 5;

        ArrayList<Individual>pool = (ArrayList<Individual>) generateTestPool(poolSize);
        JavolverRanking.calculateFitnessRank(pool);

    }



    public void reportIndividual(Individual i) {
        double s1 = i.getScore();
        double s2 = i.getRankScore();
        System.out.println("Score: "+s1+"  Rank Score: "+s2);
    }

    public List<Individual> generateTestPool(int size) {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            pool.add(new DummyGeneRandom(i));
        }
        return pool;
    }
}
