package com.physmo.reference.picturesolver;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Spreader;
import com.physmo.javolver.breedingoperator.BreedingOperatorCrossover;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.OptimizerES;
import com.physmo.javolver.solver.Solver;
import com.physmo.javolver.solver.Warmup;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.utils.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PictureSolver {

    BufferedImage targetImage = null;
    BufferedImage workImage = null;
    int populationSize = 55;
    ImageComparer imageComparer;
    Graphics2D dc;
    int numObjects = 50; //50;
    DnaDrawer drawerClass;
    Color graphBG = new Color(36, 68, 23);
    Color graphFG = new Color(171, 206, 64);

    Attenuator attenuator = createConfiguredAttenuator();

    private Attenuator createConfiguredAttenuator() {
        return new Attenuator()
                .setScoreRange(0.70, 1.0)
                .addParam("testImageSize", 5, 150)
                .addParam("mutationAmount", 1, 0.2)
                .addParam("renderObjectCount", 5, numObjects);
    }

    public static void main(String[] args) {
        PictureSolver pictureSolver = new PictureSolver();
        pictureSolver.run();
    }

    public void run() {
        drawerClass = DrawerType.CIRCLES.create();

        try {
            targetImage = ImageIO.read(new File(String.valueOf(PictureSolver.class.getResource("/parrot.jpg").getFile())));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }

        workImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dc = workImage.createGraphics();
        imageComparer = new ImageComparer(targetImage);

        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        BasicGraph graph = new BasicGraph(500);
        BasicGraph graph2 = new BasicGraph(500);

        MultiViewer multiViewer = new MultiViewer(800, 800);

        // Removed direct configuration, as now the attenuator is preconfigured upon field initialization

        Javolver solver;

        solver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(numObjects * drawerClass.getObjectSize())
                .keepBestIndividualAlive(true)
                .addMutationOperator(new MutationOperatorSimple(2, 0.5))
                .setSelectionOperator(new SelectionOperatorTournament(0.5))
                .setBreedingOperator(new BreedingOperatorCrossover())
                .scoreFunction(this::calculateScore)
                .build();

        Warmup.warmup(solver, 1);


        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            solver.doOneCycle();

            Individual top = solver.getBestScoringIndividual();
            double topScore = top.getScore();
            attenuator.setScore(topScore);
            double mutationAmount = attenuator.getValue("mutationAmount");
            solver.setTemperature(mutationAmount);


//            if (j % 100 == 0) {
//                for (int i = 0; i < 100; i++) spreader.spread(solver.getPool());
//            }

            if (j % 10 == 0) {

                disp.getDrawingContext().drawImage(targetImage, 0, 0);
                Graphics2D dc = workImage.createGraphics();
                int objectLimit = (int) attenuator.getValue("renderObjectCount");

                drawerClass.render(dc, top.getDna(), workImage.getWidth(), workImage.getHeight(), objectLimit);

                disp.getDrawingContext().drawImage(workImage, targetImage.getWidth(), 0);
                disp.repaint();

                String str2 = String.format("i:%d topScore:%5.3f  testImageSize: %d   mutationAmount: %5.3f objectLimit: %d",
                        solver.getIteration(), topScore, (int) attenuator.getValue("testImageSize"), attenuator.getValue("mutationAmount"), objectLimit);

                System.out.println(str2);

                if (multiViewer != null)
                    multiViewer.redraw(solver.getPool(), drawerClass, workImage.getWidth(), workImage.getHeight(), objectLimit);

            }

//            if (j % 50 == 0) {
//                CullSimilar cullSimilar = new CullSimilar();
//                cullSimilar.run((Javolver) solver);
//                ((Javolver) solver).increasePopulation(populationSize);
//            }

            if (j % 100 == 0) {
                graph2.addData(Math.pow(top.getScore(), 2));
            }
            if (j % 10 == 0) {
                graph.addData(Math.pow(top.getScore(), 2));
                dispGraph.getDrawingContext().cls(graphBG);
                graph.draw(dispGraph, 0, 0, 200, 200, graphFG);
                graph2.draw(dispGraph, 200, 0, 200, 200, graphFG);
                dispGraph.repaint();
            }


//            if (j % 30 == 0) {
//                System.out.println("Shake things up");
//                shakeThingsUp(((Javolver) evolver).getPool(), 0.02);
//            }
        }

    }

    public void shakeThingsUp(List<Individual> pool, double amount) {
        for (Individual individual : pool) {
            double[] data = individual.getDna().getData();
            for (int i = 0; i < data.length; i++) {
                data[i] += (Math.random() - 0.5) * amount;
            }
        }

    }

    public double calculateScore(Individual i) {
        dc.setBackground(Color.GRAY);
        dc.clearRect(0, 0, workImage.getWidth(), workImage.getHeight());

        int objectLimit = (int) attenuator.getValue("renderObjectCount");
        drawerClass.render(dc, i.getDna(), workImage.getWidth(), workImage.getHeight(), objectLimit);
        double score = imageComparer.compareScaled(workImage, (int) attenuator.getValue("testImageSize"), 1);

        double penalty = drawerClass.getScoreAdjustments(i.getDna(), workImage.getWidth(), workImage.getHeight());

        return Math.max(score - penalty, 0);
    }

    private boolean isSameSpecies(Individual i1, Individual i2) {

        double d1[] = i1.getDna().getData();
        double d2[] = i2.getDna().getData();

        int differences = 0;
        double differenceThreshold = 0.001;
        int allowedDifferences = 3;

        for (int i = 0; i < d1.length; i++) {
            if (Math.abs(d1[i] - d2[i]) > differenceThreshold) differences++;
            if (differences > allowedDifferences) return false;
        }

        return true;
    }
}

enum DrawerType {
    STRING {
        @Override
        public DnaDrawer create() { return new DnaDrawerString(); }
    },
    CIRCLES {
        @Override
        public DnaDrawer create() { return new DnaDrawerCircles(); }
    },
    SQUARES {
        @Override
        public DnaDrawer create() { return new DnaDrawerSimpleSquares(); }
    },
    POLYS {
        @Override
        public DnaDrawer create() { return new DnaDrawerPolys(); }
    };

    public abstract DnaDrawer create();
}
