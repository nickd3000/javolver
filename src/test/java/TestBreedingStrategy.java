import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingstrategy.BreedingStrategyAverage;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestBreedingStrategy {

    public void setAllDnaValuesTo(Individual individual, double val) {
        for (int i=0;i<individual.dna.getSize();i++) {
            individual.dna.set(i, val);
        }
    }

    @Test
    public void TestBreedingStrategyAverage() {
        Individual i1 = new Individual(2);
        Individual i2 = new Individual(2);

        i1.dna.init(10);
        setAllDnaValuesTo(i1, 0);
        i2.dna.init(10);
        setAllDnaValuesTo(i2, 1);

        BreedingStrategyAverage breedingStrategyAverage = new BreedingStrategyAverage();

        List<Individual> children = breedingStrategyAverage.breed(i1, i2);

        for (Individual child : children) {
            System.out.println(child);
        }

        Assert.assertEquals(children.size(), 1);

    }

    @Test
    public void TestBreedingStrategyCrossover() {
        Individual i1 = new Individual(2);
        Individual i2 = new Individual(2);

        i1.dna.init(10);
        setAllDnaValuesTo(i1, 0);
        i2.dna.init(10);
        setAllDnaValuesTo(i2, 1);

        BreedingStrategyCrossover breedingStrategyAverage = new BreedingStrategyCrossover();

        List<Individual> children = breedingStrategyAverage.breed(i1, i2);

        Assert.assertEquals(children.size(), 2);

        Individual c1 = children.get(0);
        Individual c2 = children.get(1);

        for (int i=0;i<10;i++) {
            double sum = c1.dna.getDouble(i) + c2.dna.getDouble(i);
            Assert.assertEquals(sum, 1.0, 0.0001);
        }

    }
}
