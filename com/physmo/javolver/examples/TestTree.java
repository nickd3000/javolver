package com.physmo.javolver.examples;

import java.awt.Color;

import com.physmo.javolver.Javolver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class TestTree {

	public static void main(String[] args) {

		testTree();

	}

	public static void testTree() {

		BasicDisplay disp = new BasicDisplayAwt(400, 400);

		int populationTargetSize = 10;

		System.out.print("START");
		for (int n = 0; n < 500; n++) {

			// Create the evolver:
			Javolver testEvolver = new Javolver(new GeneTree(),populationTargetSize)
					.keepBestIndividualAlive(true)
					.parallelScoring(false)
					.setDefaultStrategies();

			int iteration = 0;
			int runLength = 400;
			for (int j = 0; j < runLength; j++) {

				disp.startTimer();

				testEvolver.doOneCycle();
				iteration++;

				testEvolver.report();

				disp.cls(new Color(149, 183, 213));

				GeneTree best = (GeneTree) testEvolver.findBestScoringIndividual(null);
				best.draw(disp, 200.0f, 350.0f);

				disp.setDrawColor(Color.GREEN);
				disp.drawLine(j, 50, j, 70);
				disp.refresh();

				System.out.println("Iterations " + iteration + "   Timer: " + disp.getEllapsedTime() + "  Score:"
						+ best.getScore());

			}
			System.out.print("END");
		}

	}

}
