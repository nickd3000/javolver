package com.physmo.javolver.examples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategyGeneBased;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureSolver {
    public static void main(String[] args) {


        int populationSize = 30;//100;
        BufferedImage targetImage = null;
        try {
            //targetImage = ImageIO.read(new File("mona_lisa.jpg"));
            targetImage = ImageIO.read(new File("odin.jpg"));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }

        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        GenePicSolver gps = new GenePicSolver(targetImage);

        Javolver testEvolver = new Javolver(gps, populationSize);
        testEvolver.keepBestIndividualAlive(true).parallelScoring(false)
                //.enableCompatability(0.3,0.8)
                .addMutationStrategy(new MutationStrategySimple(0.001, 0.95))
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                //.addMutationStrategy(new MutationStrategySwap(0.01, 2))
                //.addMutationStrategy(new MutationStrategyGeneBased(gps.geneIdMutationFrequency,gps.geneIdMutationAmount))
                .setSelectionStrategy(new SelectionStrategyTournament(0.25))
                .setBreedingStrategy(new BreedingStrategyCrossover());


        double previousScore = 0;

        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            // Call the evolver class to perform one evolution step.
            testEvolver.doOneCycle();
            //testEvolver.doOneCycleOfDescent();

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
            }

        }

        //disp.dr
    }

    }
