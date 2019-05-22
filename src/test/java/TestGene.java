import com.physmo.javolver.Individual;

// Simple gene used for testing.
public class TestGene extends Individual {

    int index;

    TestGene(int index) {
        this.index = index;
    }

    @Override
    public Individual clone() {
        return new TestGene(index);
    }

    @Override
    public String toString() {
        return ""+index;
    }

    @Override
    public double calculateScore() {
        return (double)index;
    }
}
