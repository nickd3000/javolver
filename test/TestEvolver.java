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

	public static double anneal(double v1, double v2, int maxIterations, int i) {
		if (i>maxIterations) i=maxIterations;
		double p = (double)i / (double)maxIterations;
		return v1+((v2-v1)*p);
	}
	
	public static void testWord() {

		int populationSize = 100;
		Javolver testEvolver = new Javolver(new CWord("HELLOWORLD"), populationSize);
		
		// Configure the engine (Not required).
		testEvolver.setKeepBestIndividualAlive(false);
		testEvolver.setMutationCount(1);
		testEvolver.setMutationAmount(1.0/20.0);
		testEvolver.setSelectionType(SELECTION_TYPE.tournament);
		testEvolver.setSelectionRange(0.25);
		testEvolver.setDiversityAmount(1.0);
		
		for (int j = 0; j < 30; j++) {
			
			testEvolver.doOneCycle();
			
			if ((j%1)==0) // Print report every few iterations.
				System.out.println("Iteration " + j + "  " + testEvolver.report());
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	

	
	
	public static void testSpherePacker() {

		BasicDisplay disp = new BasicDisplay(300, 300);
		int populationSize = 50;
		int numberOfSpheres = 9;
		Javolver testEvolver = new Javolver(new CSpherePacker(numberOfSpheres),populationSize);


		// Configure the engine (Not required).
		testEvolver.setKeepBestIndividualAlive(false);
		testEvolver.setMutationCount(2);
		testEvolver.setMutationAmount(0.15*20);
		testEvolver.setSelectionType(SELECTION_TYPE.tournament);
		testEvolver.setSelectionRange(0.25);
		testEvolver.setDiversityAmount(0.1);
		
		int boxSize = 200;
		Color boxCol = Color.DARK_GRAY;
		
		for (int j = 0; j < 5000000; j++) {
			
			testEvolver.setMutationAmount(anneal(15,0.05,5000,j));
			
			testEvolver.doOneCycle();
			
			CSpherePacker top = (CSpherePacker)testEvolver.findBestScoringIndividual(null);
			
			if ((j%25)==0) {
				disp.cls(new Color(149, 183, 213));
				top.draw(disp, 0,0);
				
				disp.drawRect(0, 0+30, boxSize, boxSize+30, boxCol);
				
				disp.refresh();
			}
			
			if ((j%500)==0) { // Print report every few iterations.
				double coverage = ((CSpherePacker)(testEvolver.findBestScoringIndividual(null))).getCoverage();
				
				System.out.println("Coverage " + coverage / (200.0*200.0));
			}
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
			testEvolver.setKeepBestIndividualAlive(true);
			testEvolver.setMutationCount(1);
			testEvolver.setMutationAmount(0.085);
			testEvolver.setSelectionType(SELECTION_TYPE.tournament);
			testEvolver.setSelectionRange(0.005);
			testEvolver.setDiversityAmount(0);
			
			int iteration=0;
			int runLength=300;
			for (int j = 0; j < runLength; j++) {
				for (int i = 0; i < 1; i++) {
					testEvolver.setMutationAmount(anneal(0.2,0.01,runLength,j));
					testEvolver.setDiversityAmount(anneal(5,0.01,runLength,j));
					
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
	
	
	
	public static void testProgram() {


		int popTargetSize = 100;
		Javolver testEvolver = new Javolver(new GeneProgram(), popTargetSize);

		// Configure the engine (Not required).
		testEvolver.setKeepBestIndividualAlive(true);
		testEvolver.setMutationCount(5);
		testEvolver.setMutationAmount((1.0/250.0)*2060.0);
		testEvolver.setSelectionType(SELECTION_TYPE.tournament);
		testEvolver.setSelectionRange(0.15);
		testEvolver.setAllowSwapMutation(true);
		testEvolver.setDiversityAmount(50);
	
		
		int iteration=0;

		
		long realStart = System.nanoTime();
		long startTime =0, endTime = 0, duration = 0;
		
		for (int j = 0; j < 50000; j++) {
			startTime = System.nanoTime();
			for (int i = 0; i < 100; i++) {
				testEvolver.doOneCycle();
				iteration++;
				
			}
			endTime = System.nanoTime();
			duration = (endTime-startTime);
			long totalTime = (endTime - realStart) / (1000000000);
			System.out.print("iteration: " + iteration + "  Time in ms: " + (duration/1000000) + " total:" + totalTime + "  ");
			testEvolver.report();
			System.out.println(testEvolver.findBestScoringIndividual(null).toString());
			
			// Cataclysmic event!
			/*if (cataclysmCounter>100)
			{
				cataclysmCounter=0;
				testEvolver.reduceSetTo(50);
				testEvolver.addRandomPopulation(100);
			}*/
		}

		System.out.print("END ");
	}
	
	
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
