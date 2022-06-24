import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.Optimizer;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

public class SpherePacker extends MinvioApp {

    static String paramName = "mutationRate";
    int populationSize = 50;
    int numberOfSpheres = 9;
    int objectSize = 3; // Number of dna elements per sphere
    double overlapPenaltyScale = 0.25;
    Javolver testEvolver;
    Optimizer testOptimizer;
    MutationStrategySimple mutationStrategySimple;
    Attenuator attenuator;
    int boxSize = 200;
    int padding = 50;

    public static void main(String[] args) {
        MinvioApp app = new SpherePacker();
        app.start(new BasicDisplayAwt(600, 300), "Sphere Packer", 60);
    }

    @Override
    public void init(BasicDisplay bd) {

        attenuator = new Attenuator();
        attenuator.addParameter(paramName, 0.2, 0.002);
        attenuator.setIterationRange(1000);

        mutationStrategySimple = new MutationStrategySimple(2, 0.01);

        testEvolver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(numberOfSpheres * objectSize)
                .keepBestIndividualAlive(false)
                .parallelScoring(true)
                .addMutationStrategy(mutationStrategySimple)
                .setSelectionStrategy(new SelectionStrategyTournament(0.25))
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(i -> calculateScore(i))
                .build();


        testOptimizer = Optimizer.builder()
                .dnaSize(numberOfSpheres * objectSize)
                .addMutationStrategy(mutationStrategySimple)
                .scoreFunction(i -> calculateScore(i)).build();

    }

    public double calculateScore(Individual idv) {
        double total = 0.0;

        double x1, y1, r1, x2, y2, r2, d;
        double penalty = 0;
        Chromosome dna = idv.getDna();
        for (int i = 0; i < numberOfSpheres * objectSize; i += objectSize) {    // Sphere loop 1
            x1 = dna.getDouble(i);
            y1 = dna.getDouble(i + 1);
            r1 = dna.getDouble(i + 2);
            penalty += getWallPenalty(x1, y1, r1);

            for (int j = 0; j < numberOfSpheres * objectSize; j += objectSize) {    // Sphere loop 2
                if (i == j) continue;    // Don't compare against self.

                x2 = dna.getDouble(j);
                y2 = dna.getDouble(j + 1);
                r2 = dna.getDouble(j + 2);
                d = getDistance(x1, y1, x2, y2);

                if (d < (r1 + r2)) penalty += ((r1 + r2) - d) * overlapPenaltyScale;

            }
            //cover += Math.PI * (r1 * r1);
        }

        for (int i = 0; i < numberOfSpheres * 3; i += 3) {
            total += dna.getDouble(i + 2); // add radii to score.
        }

        total -= (penalty * 1.0);

        return total;
    }

    public double getWallPenalty(double x, double y, double r) {
        double w = 200;
        x *= w;
        y *= w;
        r *= w;
        double penalty = 0;
        double scale = 5; //12.5;

        if (x < r) penalty += Math.abs((r - x) * scale);
        if (y < r) penalty += Math.abs((r - y) * scale);
        if (x > w - r) penalty += Math.abs(((r + w) - x) * scale);
        if (y > w - r) penalty += Math.abs(((r + w) - y) * scale);

        return penalty;
    }

    double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double d = (dx * dx) + (dy * dy);
        if (d < 0.001) return 0.0;
        return Math.sqrt(d);
    }

    @Override
    public void update(double delta) {
        for (int i = 0; i < 10; i++) {
            testEvolver.doOneCycle();
            testOptimizer.doOneCycle();
        }
    }

    @Override
    public void draw(BasicDisplay bd, double delta) {


        Individual top = testEvolver.getBestScoringIndividual();
        Individual topB = testOptimizer.getBestScoringIndividual();

        attenuator.setCurrentIteration(testEvolver.getIteration());
        mutationStrategySimple.setAmount(attenuator.getValue(paramName));
        System.out.println("Top score:" + top.getScore() + "  mutation:" + attenuator.getValue(paramName));

        bd.cls(new Color(64, 64, 64));
        drawIndividual(top, bd, padding, padding, boxSize);
        drawIndividual(topB, bd, padding + 300, padding, boxSize);

        bd.setDrawColor(Color.white);
        bd.drawRect(padding, padding, boxSize, boxSize);

    }

    public void drawIndividual(Individual idv, BasicDisplay disp, float offsx, float offsy, float scale) {
        Chromosome dna = idv.getDna();

        for (int i = 0; i < numberOfSpheres * objectSize; i += objectSize) {
            disp.setDrawColor(disp.getDistinctColor(i, 0.8f));
            disp.drawFilledCircle(
                    offsx + (dna.getDouble(i) * scale),
                    offsy + (dna.getDouble(i + 1) * scale),
                    dna.getDouble(i + 2) * scale);
        }

    }
}



