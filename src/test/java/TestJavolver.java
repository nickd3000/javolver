import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestJavolver {

    @Test
    public void testJavolver() {

        int populationTargetSize = 50;

        // Create the evolver:
        Javolver javolver = new Javolver(new TestGene(),populationTargetSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySingle(0.2))
                .setSelectionStrategy(new SelectionStrategyTournament(0.1))
                .setBreedingStrategy(new BreedingStrategyCrossover());

        for (int i=0;i<1000;i++) {
            javolver.doOneCycle();

            TestGene topScorer = (TestGene)javolver.findBestScoringIndividual(null);

            if ((i%250)==0) {
                System.out.println("Score: " + topScorer.getScore()+ " Target: "+TestGene.scoreTarget);
            }
        }

        double solutionTolerance = 0.01;
        boolean solutionFound = false;

        // Check we evolved a solition.
        TestGene topScorer = (TestGene)javolver.findBestScoringIndividual(null);
        double solutionDelta = TestGene.scoreTarget - topScorer.getScore();
        if (Math.abs(solutionDelta)<solutionTolerance) solutionFound=true;
        if (solutionFound) System.out.println("Solution found.");

        assertEquals(solutionFound, true);

    }
}

class TestGene extends Individual {
    int dnaSize = 5;
    public static double scoreTarget =100;

    public TestGene() {
        dna.init(10);
    }

    @Override
    public Individual clone() {
        return new TestGene();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public double calculateScore() {
        // The score for this is represented by how
        // close the sum of all genes is to a certain number.

        double sum=0;

        for (int i=0;i<dnaSize;i++) {
            sum+=dna.getDouble(i);
        }

        double delta = Math.abs(scoreTarget - sum);
        if (delta> scoreTarget) delta= scoreTarget;

        return scoreTarget - delta;
    }
}

