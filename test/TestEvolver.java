package test;

import java.awt.Color;

import javolver.*;

//import evo.BasicDisplay;
//import evo.Evolver;
//import evo.IGene;

public class TestEvolver {

	public static void main(String[] args) {

		//testSpherePacker();
		testWord();
		//testTree();
		
		//testProgram();
		//Utils.test();
	}

	public static void testWord() {

		Javolver testEvolver = new Javolver(new CWord());

		testEvolver.increasePopulation(100);
		
		
		for (int j = 0; j < 30; j++) {
			
			testEvolver.doOneCycle();
			
			if ((j%5)==0) // Print report every few iterations.
				System.out.println(testEvolver.report());
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	
	public static void testSpherePacker() {

		BasicDisplay disp = new BasicDisplay(300, 300);
		
		Javolver testEvolver = new Javolver(new CSpherePacker());

		testEvolver.increasePopulation(200);
		
		
		for (int j = 0; j < 5000000; j++) {
			
			testEvolver.doOneCycle();
			
			CSpherePacker top = (CSpherePacker)testEvolver.findBestScoringIndividual(null);
			
			if ((j%25)==0) {
				disp.cls(new Color(149, 183, 213));
				top.draw(disp, 0,0);
				disp.refresh();
			}
			
			//if ((j%15)==0) // Print report every few iterations.
			//	System.out.println(testEvolver.report());
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	
	/*
	public static void testProgram() {

		// Create the evolver:
		Evolver testEvolver = new Evolver(new GeneProgram());

		testEvolver.addRandomPopulation(150);
		int iteration=0;
		int cataclysmCounter=0;
		
		long realStart = System.nanoTime();
		long startTime =0, endTime = 0, duration = 0;
		
		for (int j = 0; j < 50000; j++) {
			startTime = System.nanoTime();
			for (int i = 0; i < 100; i++) {
				testEvolver.doOneCycle();
				iteration++;
				cataclysmCounter++;
			}
			endTime = System.nanoTime();
			duration = (endTime-startTime);
			long totalTime = (endTime - realStart) / (1000000000);
			System.out.print("iteration: " + iteration + "  Time in ms: " + (duration/1000000) + " total:" + totalTime);
			testEvolver.report();
			
			// Cataclysmic event!
			if (cataclysmCounter>100)
			{
				cataclysmCounter=0;
				testEvolver.reduceSetTo(50);
				testEvolver.addRandomPopulation(100);
			}
		}

		System.out.print("END ");
	}
	*/
	
	/*
	public static void testTextPic() {
		
		
		BasicDisplay disp = new BasicDisplay(800, 400);
		GeneTextPic testGene = new GeneTextPic();

		int count = 0;
		for (int i = 0; i < 10000000; i++) {
			testGene.init();
			disp.cls(new Color(149, 183, 213));
			testGene.draw(disp, 0, i / 10);
			disp.refresh();
			count++;
			if (count > 300) {
				disp.refresh();
				count = 0;
			}
		}
		disp.close();

	}
*/
	
	/*
	public static void testTree() {
		BasicDisplay disp = new BasicDisplay(800, 400);
		// disp.drawLine(20,200,200,20);

		int popTargetSize=200;
		
		System.out.print("START");
		for (int n = 0; n < 500; n++) {
			// Create the evolver:
			Evolver testEvolver = new Evolver(new GeneTree());

			testEvolver.addRandomPopulation(popTargetSize);
			
		
			int iteration=0;
			int cataclysmCounter=0;
			
			
			for (int j = 0; j < 500000000; j++) {
				for (int i = 0; i < 1; i++) {
					testEvolver.doOneCycle();
					iteration++;
					cataclysmCounter++;
					
				}
				
				// testEvolver.cullHalf();
				if (testEvolver.getGenePool().size() < 1)
					continue;
				testEvolver.report();
				GeneTree best = (GeneTree) testEvolver.findBestScoringGene(testEvolver.getGenePool());
				GeneTree secondBest = null;
				float runningScore = 0.0f;
				for (IGene gene : testEvolver.getGenePool()) {
					if (gene.canMate(best, false) == false && gene.getScore() > runningScore) {
						secondBest = (GeneTree) gene;
						runningScore = gene.getScore();
						break;
					}
				}

				disp.cls(new Color(149, 183, 213));

				if (secondBest != null)
					secondBest.draw(disp, 200.0f + 300.0f, 350.0f);

				best.draw(disp, 200.0f, 350.0f);

				disp.drawLine(j, 50, j, 70, Color.GREEN);
				disp.refresh();
				//

				System.out.println("Iterations "+iteration);
				
				// Cataclysmic event!
				if (cataclysmCounter>200)
				{
					cataclysmCounter=0;
					testEvolver.reduceSetTo(50);
					testEvolver.addRandomPopulation(popTargetSize-50);
				}
			}
			System.out.print("END");
		}

	}
*/

}
