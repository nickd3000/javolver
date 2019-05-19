package com.physmo.javolver.examples;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

/**
 * @author nick
 * A gene representing the parameters of a javolver session used to evolve words.
 * For javolver paramatrization optimization.
 */
public class GeneWordMetaSolver extends Individual {

	public String targetWord = "EVOLUTION";
	int numGenes = 20; // is gene the correct word for a dna element?

	int DNA_COMP_LOWER=1;
	int DNA_COMP_UPPER=2;
	int DNA_MUT_FREQ=3;
	int DNA_MUT_AMOUNT=4;
	int DNA_SEL_RANGE=5;

	public GeneWordMetaSolver() {
		//targetWord = target;
		dna.init(numGenes);
	}
	
	public Individual clone()
	{
		return (Individual)(new GeneWordMetaSolver());
	}

	public String toString()
	{
		String str = "";

		for (int i=0;i<6;i++)
		{
			//str = str + dna.getDouble(i)+ " ";
		}

		String BRK = "\n";
		str+=BRK;
		str+="DNA_COMP_LOWER  "+dna.getDouble(DNA_COMP_LOWER)+BRK;
		str+="DNA_COMP_UPPER  "+dna.getDouble(DNA_COMP_UPPER)+BRK;
		str+="DNA_MUT_FREQ    "+dna.getDouble(DNA_MUT_FREQ)+BRK;
		str+="DNA_MUT_AMOUNT  "+dna.getDouble(DNA_MUT_AMOUNT)+BRK;
		str+="DNA_SEL_RANGE   "+dna.getDouble(DNA_SEL_RANGE)+BRK+BRK;

		//str += " score:"+score;
		return str;
	}
	
	// Compare each character in the string to the target string and return a score.
	// Each character gets a higher score the closer it is to the target character.
	@Override
	public double calculateScore() {
		double total = 0.0;

		total = buildJavolverFromDnaAndRun();

		return total;
	}

	public double buildJavolverFromDnaAndRun() {

		int numRunsForAverage = 5;
		int sumOfEvolutionCycles=0;
		int populationSize = 20;

		Javolver javolver = null;



        for (int i=0;i<numRunsForAverage;i++) {
			javolver = new Javolver(new GeneWord(targetWord), populationSize)
					.keepBestIndividualAlive(false)
					.enableCompatability(dna.getDouble(DNA_COMP_LOWER),dna.getDouble(DNA_COMP_UPPER))
					.parallelScoring(false)
					.addMutationStrategy(new MutationStrategySimple(dna.getDouble(DNA_MUT_FREQ), dna.getDouble(DNA_MUT_AMOUNT)))
					.setSelectionStrategy(new SelectionStrategyTournament(dna.getDouble(DNA_SEL_RANGE)))
					.setBreedingStrategy(new BreedingStrategyUniform());

			sumOfEvolutionCycles += evolveToSolution(javolver);
			//System.out.println(">>>"+sumOfEvolutionCycles);
		}

		//double averageCycled = (double)sumOfEvolutionCycles/(double)numRunsForAverage;

		sumOfEvolutionCycles = sumOfEvolutionCycles / numRunsForAverage;
		System.out.println("Average cycles to find solution "+sumOfEvolutionCycles);

        //System.out.print("Average = "+averageCycled);
		int maxCycles=100;

		if (sumOfEvolutionCycles>maxCycles) sumOfEvolutionCycles=maxCycles;
		double score = (double)(maxCycles-sumOfEvolutionCycles)/(double)maxCycles;

        return score*score;
	}

	public int evolveToSolution(Javolver javolver) {
		int maxIterations = 50;
		for (int j = 0; j < maxIterations; j++) {
			javolver.doOneCycle();
			GeneWord gene = (GeneWord)javolver.findBestScoringIndividual(javolver.getPool());
			if (gene.toString().trim().equals(targetWord)) return j;
		}
		return maxIterations;
	}
	
	// Helper methods
	// Returns a higher value the closer the characters are.
	double getScoreForCharacter(char a, char b)
	{
		int maxDiff = 15;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0;
		return (double)((maxDiff-diff)/10.0);
	}
	
		
		
}
