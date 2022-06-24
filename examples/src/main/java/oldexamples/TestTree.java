package com.physmo.javolverexamples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.Color;

public class TestTree {

	public static void main(String[] args) {

		testTree();

	}

	public static void testTree() {

		BasicDisplay disp = new BasicDisplayAwt(400, 400);
		AnimatedGifCreator gifCreator = new AnimatedGifCreator("tree.gif");

		int populationTargetSize = 250;

		System.out.print("START");
		for (int n = 0; n < 5; n++) {

			// Create the evolver:
			Javolver testEvolver = new Javolver(new GeneTree(),populationTargetSize)
					.keepBestIndividualAlive(false)
					.parallelScoring(false)
					//.enableCompatability(0.3,0.4)
					.addMutationStrategy(new MutationStrategySimple(0.01, 0.5))
					//.setSelectionStrategy(new SelectionStrategyTournament(0.1))
					.setSelectionStrategy(new SelectionStrategyRouletteRanked())
					.setBreedingStrategy(new BreedingStrategyCrossover());

			int iteration = 0;
			int runLength = 100;
			for (int j = 0; j < runLength; j++) {

				//bd.startTimer();

				testEvolver.doOneCycle();
				//Descent.descent2(testEvolver, 4, 0.1);
				iteration++;

				testEvolver.report();

				disp.cls(new Color(149, 183, 213));

				GeneTree best = (GeneTree) testEvolver.findBestScoringIndividual();
				best.draw(disp, 200.0f, 350.0f);

				disp.setDrawColor(Color.GREEN);
				disp.drawLine(j, 50, j, 70);
				disp.repaint();

				System.out.println("Iterations " + iteration  + "  Score:"
						+ best.getScore());

				gifCreator.addFrame(disp.getDrawBuffer());

			}
			System.out.print("END");
		}

		gifCreator.close();

	}

}
