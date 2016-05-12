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
	double scorePenalty = 0;
	public ArrayList<Integer> dna = new ArrayList<Integer>();
	String consoleOutput = "";
	
	public double location = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		//word = "HELLO";
		int dnaSize = 160;
		for (int i=0;i<dnaSize;i++)
		{
			if (Math.random()<0.7)
				dna.add((int)(Math.random()*20.0)); // Favour op-code range values.
			else
				dna.add((int)(Math.random()*255.0));
		}
		
		location = Math.random();
	}

	@Override
	public IGene newInstance()
	{
		return new GeneProgram();
	}

	@Override
	public void mutate() {
		//double mutationChance=0.8; // 0..1 value - higher means more chance of mutation.
		double bigMutationChance=0.2; // 0..1 value - higher means more chance of mutation.
		
		double mutationPercent = 0.25;
		int numMutations = (int)(dna.size()*mutationPercent);
		
		for (int m=0;m<numMutations;m++)
		{
			int i = (int)(dna.size()*Math.random());
		
			// Mutate slightly
			if (Math.random()>bigMutationChance) {
				int bit = dna.get(i);
				bit += (int)((Math.random()-0.5)*8.0);
				if (bit<0) bit=0;
				if (bit>255) bit=255;
				dna.set(i, bit);
			}else{
			
				// Completely mutate.
				if (Math.random()<bigMutationChance) dna.set(i, (int)(Math.random()*255.0));
			}
			
		}
		
		if (Math.random()<0.3) dna = mutateInsert(dna);
		if (Math.random()<0.3) dna = mutateDelete(dna);
		for (int j=0;j<3;j++) dna = mutateSwap(dna);
	}
	
	public ArrayList<Integer> mutateInsert(ArrayList<Integer> d)
	{
		int skip = (int)(d.size()*Math.random());
		ArrayList<Integer> nd = new ArrayList<>(d);
		for (int i=0;i<d.size()-1;i++)
		{
			int pos = i;
			if (i>skip) pos=i+1;
			if (i==skip) nd.set(pos, 0);
			else nd.set(pos, d.get(i));
			
		}
			
		return nd;
	}

	public ArrayList<Integer> mutateDelete(ArrayList<Integer> d)
	{
		int skip = (int)(d.size()*Math.random());
		ArrayList<Integer> nd = new ArrayList<>(d);
		for (int i=0;i<d.size()-1;i++)
		{
			int pos = i;
			if (i>skip) pos=i+1;
			if (i==skip) nd.set(i, 0);
			else 	nd.set(i, d.get(pos));
			
		}
			
		return nd;
	}
	
	public ArrayList<Integer> mutateSwap(ArrayList<Integer> d)
	{
		ArrayList<Integer> nd = new ArrayList<>(d);
		int pos1 = (int)(Math.random()*(double)(d.size()-1));
		int pos2 = (int)(Math.random()*(double)(d.size()-1));
				
		nd.set(pos1, d.get(pos1));
		nd.set(pos2, d.get(pos2));
			
		return nd;
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
		
		scorePenalty = 0;
		int maxCycles = 200;
		int cycleCount = 0;
		for (int i=0;i<maxCycles; i++)
		{
			cycleCount++;
			int result = sm.runCycle();
			if (sm.getMaxHits()>10) {
				scorePenalty = 20;
				break;
			}
				
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
		if (machineOutput.length()>0) score+=1f;
		
		// Penalty for too much output.
		if (machineOutput.length()>10) score=score*0.8f;
		if (machineOutput.length()>20) score=score*0.5f;
		if (machineOutput.length()>50) score=score*0.2f;
		
		//score-= cyclePenalty*2.0;
		
		// Add points for noop commands...
		for (int i=0;i<dna.size();i++)
		{
			if (dna.get(i)==0) score+=0.0001f;
		}
		
		//score += Math.random()*0.1f;
		
		if (cyclePenalty<0.01) score*=0.1f;
		if (cyclePenalty>0.8) score*=0.8f;
		
		score-=scorePenalty;
				
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
		
		boolean[] crossPoints = new boolean [DNA1.size()];
		int numCrossoverPoints=10;
		for (int i=0;i<crossPoints.length;i++) crossPoints[i]=false;
		
		// Calculate requested number of crossover points.
		for (int i=0;i<numCrossoverPoints;i++)
		{
			crossPoints[(int)(Math.random()*(double)DNA1.size())]=true;
		}
		
		boolean side = Math.random()<0.5?false:true;
		
		for (int i=0;i<DNA1.size();i++)
		{
			//if (Math.random()<0.01) { // Randomly switch side.
			if (crossPoints[i]) {
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
		
		child.location = (((GeneProgram)p1).location+((GeneProgram)p2).location)/2.0;
		
		return child;
	}

	float getScoreForCharacter(char a, char b)
	{
		int maxDiff = 20;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0f;
		float s = ((maxDiff-diff)/(float)maxDiff);
		
		if (diff<3) s*=2.0f;
		if (diff<2) s*=5.0f;
		if (diff==0) s*=5.0f;
		
		
		return s;
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

		// Compare dna
		int differences = 0;
		for (int i=0;i<this.dna.size();i++)
		{
			if (this.dna.get(i)!=((GeneProgram)partner).dna.get(i)) differences++;
		}
		
		if (differences<3) return false;
		
		double distance = Math.abs(this.location-((GeneProgram)partner).location);
		if (distance<0.5) return true;
		
		return false;
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
