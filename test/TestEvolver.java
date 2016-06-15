package test;

import java.awt.Color;

import javolver.*;
import javolver.Javolver.SELECTION_TYPE;


//import evo.BasicDisplay;
//import evo.Evolver;
//import evo.IGene;

public class TestEvolver {

	public static void main(String[] args) {

		testSpherePacker();
		//testWord();
		//testTree();
		
		//testProgram();
		//Utils.test();
	}

	public static void testWord() {

		int populationSize = 100;
		Javolver testEvolver = new Javolver(new CWord(), populationSize);
		
		// Configure the engine (Not required).
		testEvolver.setKeepBestIndividualAlive(true);
		testEvolver.setMutationCount(1);
		testEvolver.setMutationAmount(1.0/20.0);
		testEvolver.setSelectionType(SELECTION_TYPE.tournament);
		testEvolver.setSelectionRange(0.25);
		
		for (int j = 0; j < 30; j++) {
			
			testEvolver.doOneCycle();
			
			if ((j%1)==0) // Print report every few iterations.
				System.out.println(testEvolver.report());
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	
	public static void testSpherePacker() {

		BasicDisplay disp = new BasicDisplay(300, 300);
		
		Javolver testEvolver = new Javolver(new CSpherePacker(20),50);


		// Configure the engine (Not required).
		testEvolver.setKeepBestIndividualAlive(false);
		testEvolver.setMutationCount(1);
		testEvolver.setMutationAmount(0.25);
		testEvolver.setSelectionType(SELECTION_TYPE.tournament);
		testEvolver.setSelectionRange(0.125);
		
		int boxSize = 200;
		Color boxCol = Color.DARK_GRAY;
		
		for (int j = 0; j < 5000000; j++) {
			
			testEvolver.doOneCycle();
			
			CSpherePacker top = (CSpherePacker)testEvolver.findBestScoringIndividual(null);
			
			if ((j%25)==0) {
				disp.cls(new Color(149, 183, 213));
				top.draw(disp, 0,0);
				
				disp.drawRect(0, 0+30, boxSize, boxSize+30, boxCol);
				
				disp.refresh();
			}
			
			//if ((j%15)==0) // Print report every few iterations.
			//	System.out.println(testEvolver.report());
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	
	

	public static void testTree() {
		BasicDisplay disp = new BasicDisplay(800, 400);
		disp.drawCircle(100, 100, 50, new Color(255,0,128));

		int popTargetSize=20;
		
		System.out.print("START");
		for (int n = 0; n < 500; n++) {
			// Create the evolver:
			Javolver testEvolver = new Javolver(new GeneTree(), popTargetSize);
		
			// Configure the engine (Not required).
			testEvolver.setKeepBestIndividualAlive(false);
			testEvolver.setMutationCount(4);
			testEvolver.setMutationAmount(0.051);
			testEvolver.setSelectionType(SELECTION_TYPE.tournament);
			testEvolver.setSelectionRange(0.025);
		
			int iteration=0;
			
			for (int j = 0; j < 500000000; j++) {
				for (int i = 0; i < 1; i++) {
					testEvolver.doOneCycle();
					iteration++;
				}
				
				testEvolver.report();
				
				GeneTree best = (GeneTree) testEvolver.findBestScoringIndividual(null);

				disp.cls(new Color(149, 183, 213));

				best.draw(disp, 200.0f, 350.0f);

				disp.drawLine(j, 50, j, 70, Color.GREEN);
				disp.refresh();
				//

				System.out.println("Iterations "+iteration);

			}
			System.out.print("END");
		}

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
	
	


}
