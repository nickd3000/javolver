import com.physmo.javolver.Individual;

// Simple gene used for testing.
// This gene will contain a random (ish) (but constant) score.
public class DummyGeneRandom extends Individual {

    private final int index;
    DummyGeneRandom(int index) {
        this.index = index;
    }

    @Override
    public Individual clone() {
        return new DummyGeneRandom(index);
    }

    @Override
    public String toString() {
        return ""+index;
    }

    @Override
    public double calculateScore() {
        int i = (((index+12381)*13523)/31);
        return (double)(i%255)/255.0;
    }
}
