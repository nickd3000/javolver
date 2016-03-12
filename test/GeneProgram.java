package test;

import java.net.NetworkInterface;
import java.util.ArrayList;

import evo.IGene;

public class GeneProgram implements IGene {

	//public String word = "";
	float score = 0.0f;
	boolean scored = false;
	int generation = 0;
	int age = 0;
	public String targetWord = "GENETICS";
	double cyclePenalty = 0.0; // number of cycles involved in running program.
	
	public ArrayList<Integer> dna = new ArrayList<Integer>();
	String consoleOutput = "";
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		//word = "HELLO";
		int dnaSize = 250;
		for (int i=0;i<dnaSize;i++)
		{
			if (Math.random()<0.7)
				dna.add((int)(Math.random()*20.0)); // Favour op-code range values.
			else
				dna.add((int)(Math.random()*255.0));
		}
	}

	@Override
	public IGene newInstance()
	{
		return new GeneProgram();
	}

	@Override
	public void mutate() {
		double mutationChance=0.1; // 0..1 value - higher means more chance of mutation.
		double bigMutationChance=0.01; // 0..1 value - higher means more chance of mutation.
		for (int i=0;i<dna.size();i++)
		{
			// Mutate slightly
			if (Math.random()<mutationChance) {
				int bit = dna.get(i);
				bit += (int)((Math.random()-0.5)*6.0);
				if (bit<0) bit=0;
				if (bit>255) bit=255;
				dna.set(i, bit);
			}
			
			// Completely mutate.
			if (Math.random()<bigMutationChance) dna.set(i, (int)(Math.random()*255.0));
		}
	}

	// Setup and also run simple machine, return console result.
	public String setupSimpleMachineFromDNA(SimpleMachine sm, ArrayList<Integer> d)
	{
		/* the old way.
		for (int i=0;i<d.size();i+=2)
		{
			sm.memory[d.get(i)] = d.get(i+1);
		}
		*/
		for (int i=0;i<d.size();i++)
		{
			sm.memory[i] = d.get(i);
		}
		
		
		int maxCycles = 1500;
		int cycleCount = 0;
		for (int i=0;i<maxCycles; i++)
		{
			int result = sm.runCycle();
			cycleCount++;
			if (result==1) break;
		}
		
		cyclePenalty = (double)cycleCount / (double)maxCycles;
		
		return sm.console;
	}
	
	@Override
	public void calculateScore() {
		age++;
		if (scored==true) return;
		
		SimpleMachine sm = new SimpleMachine();
		String machineOutput = setupSimpleMachineFromDNA(sm, dna);
		consoleOutput = machineOutput; // record this for reporting.
		

		
		int minWordSize = Math.min(targetWord.length(), machineOutput.length());
		float theScore = 0.0f;
		for (int i=0;i<minWordSize;i++)
		{
			theScore += getScoreForCharacter(targetWord.charAt(i), machineOutput.charAt(i));
		}
		score = theScore;
		scored = true;
		
		
		// Add points for having some output:
		if (machineOutput.length()>0) score+=5;
		
		// Penalty for too much output.
		if (machineOutput.length()>10) score=score*0.8f;
		if (machineOutput.length()>20) score=score*0.5f;
		if (machineOutput.length()>50) score=score*0.2f;
		
		score-= cyclePenalty*2.0;
		
		// Add points for noop commands...
		for (int i=0;i<dna.size();i++)
		{
			if (dna.get(i)==0) score+=0.0001;
		}
		
	}

	@Override
	public float getScore() {
		return score;
	}


	@Override
	public IGene createChild(IGene p1, IGene p2) {
		
		ArrayList<Integer> DNA1 = ((GeneProgram)p1).dna;
		ArrayList<Integer> DNA2 = ((GeneProgram)p2).dna;
		ArrayList<Integer> DNA3 = new ArrayList<Integer>(DNA2);
		//char[] chars = DNA3.toCharArray();
		
		boolean side = false;
		for (int i=0;i<DNA1.size();i++)
		{
			if (Math.random()<0.01) { // Randomly switch side.
				if (side==true) side=false;
				else side = true;
			}
				
			if (side)	DNA3.set(i, DNA1.get(i));
			else		DNA3.set(i, DNA2.get(i));
		}
		
		//DNA3 = new String(chars);
		
		GeneProgram child = new GeneProgram();
		child.dna = DNA3;
		
		child.generation = Math.max(p1.getGeneration(), p2.getGeneration()) + 1;
				
		return child;
	}

	float getScoreForCharacter(char a, char b)
	{
		int maxDiff = 20;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0f;
		return ((maxDiff-diff)/(float)maxDiff);
	}


	@Override
	public String asString() {
		
		StringBuilder sb = new StringBuilder(200);
		sb.append(String.format("Score:%f Gen:%d  Word:%s\n", score, generation, consoleOutput));
		return sb.toString();
		
	}


	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return age;
	}


	@Override
	public boolean canMate(IGene partner, boolean excludeSimilar) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public int getGeneration() {
		return generation;
		
	}


	@Override
	public void randomise() {
		// TODO Auto-generated method stub
		
	}


}
