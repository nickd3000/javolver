import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import org.junit.Test;

public class TestJavolver {

    @Test
    public void testJavolver() {

    }
}

class TestGene extends Individual {
    int dnaSize = 10;

    public TestGene() {
        dna.init(10);
    }

    @Override
    public Individual clone() {
        return new TestGene();
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public double calculateScore() {
        // The score for this is represented by how
        // close the average of all genes is to a certain number.
        double target=5;
        double sum=0;
        //for (int i=0;i<)

        return 0;
    }
}

