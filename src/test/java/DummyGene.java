import com.physmo.javolver.Individual;

// Simple gene used for testing.
// Score multiplier is used to convert the index of the individual to the score.
public class DummyGene extends Individual {

    private double scoreMultiplier=0;

    private final int index;
    DummyGene(int index, double scoreMultiplier) {

        this.index = index;
        this.scoreMultiplier = scoreMultiplier;
    }

    @Override
    public Individual clone() {
        return new DummyGene(index,scoreMultiplier);
    }

    @Override
    public String toString() {
        return ""+index;
    }

    @Override
    public double calculateScore() {
        return (double)index*scoreMultiplier;
    }
}
