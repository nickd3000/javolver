package com.physmo.javolverexamples2;

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

    int populationSize = 25;
    int numberOfSpheres = 8;
    int objectSize = 3; // Number of dna elements per sphere
    double overlapPenaltyScale = 0.25;
    Javolver testEvolver;
    Optimizer testOptimizer;

    public static void main(String[] args) {
        MinvioApp app = new SpherePacker();
        app.start(new BasicDisplayAwt(600, 300), "Sphere Packer", 60);
    }

    @Override
    public void init(BasicDisplay bd) {

        testEvolver = Javolver.builder()
                .populationTargetSize(populationSize).dnaSize(numberOfSpheres * objectSize)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(1, 0.5))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(i -> calculateScore(i)).build();

        testOptimizer = Optimizer.builder()
                .dnaSize(numberOfSpheres * objectSize)
                .addMutationStrategy(new MutationStrategySimple(1, 0.5))
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
    public void draw(BasicDisplay bd, double delta) {

        int boxSize = 200;
        int pad = 50;

        for (int i = 0; i < 100; i++) {
            testEvolver.doOneCycle();
            testOptimizer.doOneCycle();

        }

        Individual top = testEvolver.findBestScoringIndividual();
        Individual topB = testOptimizer.findBestScoringIndividual();

        bd.cls(new Color(64, 64, 64));
        drawIndividual(top, bd, pad, pad);
        drawIndividual(topB, bd, pad+300, pad);

        bd.setDrawColor(Color.white);
        bd.drawRect(pad, pad, boxSize, boxSize);

    }

    public void drawIndividual(Individual idv, BasicDisplay disp, float offsx, float offsy) {
        Chromosome dna = idv.getDna();

        for (int i = 0; i < numberOfSpheres * objectSize; i += objectSize) {
            disp.setDrawColor(disp.getDistinctColor(i, 0.8f));
            disp.drawFilledCircle(
                    offsx + dna.getDouble(i),
                    offsy + dna.getDouble(i + 1),
                    dna.getDouble(i + 2));
        }

    }
}



