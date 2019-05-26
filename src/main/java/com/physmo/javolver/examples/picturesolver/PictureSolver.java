package com.physmo.javolver.examples.picturesolver;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.examples.picturesolver.GenePicSolver;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureSolver {
    public static void main(String[] args) {


        int populationSize = 3; //60; //0;//100;
        int scoreStep = 2;
        int dnaSize = 1200;
        Class drawerClass = DnaDrawerPolys.class;

        BufferedImage targetImage = null;
        try {
            targetImage = ImageIO.read(new File("mona_lisa.jpg"));
            //targetImage = ImageIO.read(new File("odin.jpg"));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }

        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        BasicDisplay dispGraph = new BasicDisplayAwt(400,200);
        BasicGraph graph = new BasicGraph(2000);

        GenePicSolver gps = new GenePicSolver(targetImage, drawerClass, dnaSize, scoreStep);

        Javolver testEvolver = new Javolver(gps, populationSize);
        testEvolver.keepBestIndividualAlive(true).parallelScoring(false)
                .enableCompatability(0.4,0.85)
                .addMutationStrategy(new MutationStrategySimple(0.01, 0.95))
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                //.addMutationStrategy(new MutationStrategySwap(0.01, 2))
                //.addMutationStrategy(new MutationStrategyGeneBased(gps.geneIdMutationFrequency,gps.geneIdMutationAmount))
                .setSelectionStrategy(new SelectionStrategyTournament(0.25))
                .setBreedingStrategy(new BreedingStrategyCrossover());


        double previousScore = 0;

        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            // Call the evolver class to perform one evolution step.
            //testEvolver.doOneCycle();
            testEvolver.doOneCycleOfDescent();

            if (j % 10 == 0) {
                GenePicSolver top = (GenePicSolver) testEvolver.findBestScoringIndividual(null);
                disp.drawImage(targetImage, 0, 0);
                disp.drawImage(top.getImage(), targetImage.getWidth(), 0);
                disp.refresh();
                System.out.println("Score: " + top.getScore());

//				if (previousScore == top.getScore()) {
//					System.out.println("Earthquake!");
//					earthquake(testEvolver.getPool(),0.001);
//				}

                previousScore = top.getScore();

                graph.addData(top.getScore());
                dispGraph.cls(Color.WHITE);
                graph.draw(dispGraph,0,0,400,200, Color.BLUE);
                dispGraph.refresh();
            }

        }

        //disp.dr
    }

    }
