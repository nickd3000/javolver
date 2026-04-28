package com.physmo.reference;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorCrossover;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Optimizer;
import com.physmo.javolver.solver.OptimizerES;
import com.physmo.javolver.solver.Solver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

import static com.physmo.minvio.Utils.getDistinctColor;

public class SpherePacker extends MinvioApp {

    static String MUTATION_RATE = "mutationRate";
    int populationSize = 500;
    int numberOfSpheres = 20;
    int genesPerSphere = 3; // Each sphere uses three DNA values: x position, y position, and radius.
    double overlapPenaltyScale = 0.25;
    double wallPenaltyScale = 20.0;
    double maxRadius = 0.2;
    Javolver javolverSolver;
    Solver optimizerSolver;

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

        javolverSolver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(numberOfSpheres * genesPerSphere)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationOperator(new MutationOperatorSimple(2, 0.5))
                .setSelectionOperator(new SelectionOperatorTournament(0.25))
                .setBreedingOperator(new BreedingOperatorCrossover())
                .scoreFunction(this::calculateScore)
                .build();


//        testOptimizer = Optimizer.builder()
//                .dnaSize(numberOfSpheres * genesPerSphere)
//                .addMutationOperator(MutationOperatorSimple)
//                .scoreFunction(i -> calculateScore(i)).build();

        optimizerSolver = new OptimizerES();
        ((OptimizerES) optimizerSolver).setDnaSize(numberOfSpheres * genesPerSphere);
        optimizerSolver.setScoreFunction(this::calculateScore);
        optimizerSolver.setTemperature(0.001);
        optimizerSolver.init();

    }

    public double calculateScore(Individual individual) {
        double total = 0.0;

        double x1, y1, r1, x2, y2, r2, d;
        double penalty = 0;
        Chromosome dna = individual.getDna();
        for (int i = 0; i < numberOfSpheres * genesPerSphere; i += genesPerSphere) {    // Sphere loop 1
            x1 = dna.getDouble(i);
            y1 = dna.getDouble(i + 1);
            r1 = dna.getDouble(i + 2);
            penalty += getWallPenalty(x1, y1, r1);
            penalty += getRadiusPenalty(r1);

            for (int j = i + genesPerSphere; j < numberOfSpheres * genesPerSphere; j += genesPerSphere) {    // Sphere loop 2
                x2 = dna.getDouble(j);
                y2 = dna.getDouble(j + 1);
                r2 = dna.getDouble(j + 2);
                d = getDistance(x1, y1, x2, y2);

                if (d < (r1 + r2)) {
                    penalty += Math.pow((r1 + r2) - d, 2) * boxSize * overlapPenaltyScale;
                }

            }
        }

        for (int i = 0; i < numberOfSpheres * genesPerSphere; i += genesPerSphere) {
            double radius = Math.max(0, dna.getDouble(i + 2));
            total += Math.PI * radius * radius; // add circle area to score.
        }

        total -= penalty;

        return total;
    }

    public double calculateCoverage(Individual individual) {
        Chromosome dna = individual.getDna();
        double coverage = 0.0;

        for (int i = 0; i < numberOfSpheres * genesPerSphere; i += genesPerSphere) {
            double radius = Math.max(0, dna.getDouble(i + 2));
            coverage += Math.PI * radius * radius;
        }

        return coverage;
    }

    public double getRadiusPenalty(double r) {
        double penalty = 0.0;

        if (r < 0) {
            penalty += Math.pow(-r, 2) * wallPenaltyScale;
        }

        if (r > maxRadius) {
            penalty += Math.pow(r - maxRadius, 2) * wallPenaltyScale;
        }

        return penalty;
    }

    public double getWallPenalty(double x, double y, double r) {
        if (r <= 0) return wallPenaltyScale;

        double penalty = 0;

        if (x - r < 0) penalty += Math.pow(r - x, 2) * wallPenaltyScale;
        if (y - r < 0) penalty += Math.pow(r - y, 2) * wallPenaltyScale;
        if (x + r > 1) penalty += Math.pow((x + r) - 1, 2) * wallPenaltyScale;
        if (y + r > 1) penalty += Math.pow((y + r) - 1, 2) * wallPenaltyScale;

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
            javolverSolver.doOneCycle();
            optimizerSolver.doOneCycle();
        }
    }

    @Override
    public void draw(double delta) {


        Individual top = javolverSolver.getBestScoringIndividual();
        Individual topB = optimizerSolver.getBestScoringIndividual();


        //attenuator.s(javolverSolver.getIteration());
        double mutationRate = attenuator.getValue(MUTATION_RATE);

        javolverSolver.setTemperature(mutationRate);
        optimizerSolver.setTemperature(mutationRate);


        System.out.printf("Top score:%5.3f  mutation: %5.4f %n", top.getScore(), attenuator.getValue(MUTATION_RATE));
        //System.out.println("Top score:" + top.getScore() + "  mutation:" + attenuator.getValue(paramName));

        cls(new Color(64, 64, 64));
        drawIndividual(top, padding, padding, boxSize);
        drawIndividual(topB,padding + 300, padding, boxSize);

        setDrawColor(Color.white);
        drawRect(padding, padding, boxSize, boxSize);

    }

    public void drawIndividual(Individual individual, float offsetX, float offsetY, float scale) {
        Chromosome dna = individual.getDna();

        for (int i = 0; i < numberOfSpheres * genesPerSphere; i += genesPerSphere) {
            setDrawColor(getDistinctColor(i, 0.8f));
            drawFilledCircle(
                    offsetX + (dna.getDouble(i) * scale),
                    offsetY + (dna.getDouble(i + 1) * scale),
                    dna.getDouble(i + 2) * scale);
        }

        double coverage = calculateCoverage(individual) * 100.0;
        setDrawColor(Color.white);
        drawText(String.format("Coverage: %.2f%%", coverage), (int) (offsetX + 10), (int) (boxSize + offsetY + 20));
    }
}



