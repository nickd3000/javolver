package com.physmo.reference;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.OptimizerES;
import com.physmo.javolver.solver.Solver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

import static com.physmo.minvio.Utils.getDistinctColor;

public class SpherePacker extends MinvioApp {

    static String MUTATION_RATE = "mutationRate";
    int populationSize = 50;
    int numberOfSpheres = 9;
    int objectSize = 3; // Number of dna elements per sphere
    double overlapPenaltyScale = 0.25;
    Javolver testEvolver;
    Solver testOptimizer;

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
        attenuator.addParam(MUTATION_RATE, 1, 0.01);
        //attenuator.setIterationRange(1000);

        testEvolver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(numberOfSpheres * objectSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(true)
                .addMutationOperator(new MutationOperatorSimple(2, 0.01))
                .setSelectionOperator(new SelectionOperatorTournament(0.25))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();


//        testOptimizer = Optimizer.builder()
//                .dnaSize(numberOfSpheres * objectSize)
//                .addMutationOperator(MutationOperatorSimple)
//                .scoreFunction(i -> calculateScore(i)).build();

        testOptimizer = new OptimizerES();
        ((OptimizerES) testOptimizer).setDnaSize(numberOfSpheres * objectSize);
        testOptimizer.setScoreFunction(this::calculateScore);
        testOptimizer.init();

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
    public void update(BasicDisplay bd, double delta) {
        for (int i = 0; i < 10; i++) {
            testEvolver.doOneCycle();
            testOptimizer.doOneCycle();
        }
    }

    @Override
    public void draw(double delta) {


        Individual top = testEvolver.getBestScoringIndividual();
        Individual topB = testOptimizer.getBestScoringIndividual();


        //attenuator.s(testEvolver.getIteration());
        double mutationRate = attenuator.getValue(MUTATION_RATE);

        testEvolver.setTemperature(mutationRate);
        testOptimizer.setTemperature(mutationRate);


        System.out.printf("Top score:%5.3f  mutation: %5.4f %n", top.getScore(), attenuator.getValue(MUTATION_RATE));
        //System.out.println("Top score:" + top.getScore() + "  mutation:" + attenuator.getValue(paramName));

        cls(new Color(64, 64, 64));
        drawIndividual(top, padding, padding, boxSize);
        drawIndividual(topB,padding + 300, padding, boxSize);

        setDrawColor(Color.white);
        drawRect(padding, padding, boxSize, boxSize);

    }

    public void drawIndividual(Individual idv, float offsx, float offsy, float scale) {
        Chromosome dna = idv.getDna();

        for (int i = 0; i < numberOfSpheres * objectSize; i += objectSize) {
            setDrawColor(getDistinctColor(i, 0.8f));
            drawFilledCircle(
                    offsx + (dna.getDouble(i) * scale),
                    offsy + (dna.getDouble(i + 1) * scale),
                    dna.getDouble(i + 2) * scale);
        }

    }
}



