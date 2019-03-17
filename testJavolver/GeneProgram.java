package testJavolver;

import com.physmo.javolver.Individual;

//import evo.IGene;


public class GeneProgram extends Individual  {

	//public String word = "";
	
	public String targetWord = "GENETICS";
	double cyclePenalty = 0.0; // number of cycles involved in running program.
	double scorePenalty = 0;
	String consoleOutput = "";
	
	public double location = 0;
	
		
	public GeneProgram() {
		dna.init(50); // 200
	}
	
	@Override
	public Individual clone() {
		return (Individual)(new GeneProgram());
	}

	
	// Setup and also run simple machine, return console result.
	public String setupSimpleMachineFromDNA(SimpleMachine sm)
	{

		for (int i=0;i<dna.getData().size();i++)
		{
			int val = (int)(dna.getDouble(i) * 250.0);
			sm.memory[i] = val;
		}
		
		scorePenalty = 0;
		int maxCycles = 200;
		int cycleCount = 0;
		for (int i=0;i<maxCycles; i++)
		{
			cycleCount++;
			int result = sm.runCycle();
			if (sm.getMaxHits()>20) {
				scorePenalty = 20;
				break;
			}
				
			if (result==1) break;
		}
		
		cyclePenalty = (double)cycleCount / (double)maxCycles;
		
		return sm.console;
	}
	
	@Override
	public double calculateScore() {
				
		SimpleMachine sm = new SimpleMachine();
		String machineOutput = setupSimpleMachineFromDNA(sm);
		consoleOutput = machineOutput; // record this for reporting.
				
		int minWordSize = Math.min(targetWord.length(), machineOutput.length());
		float theScore = 0.0f;
		for (int i=0;i<minWordSize;i++)
		{
			theScore += getScoreForCharacter(targetWord.charAt(i), machineOutput.charAt(i));
		}
		score = theScore;
				
		
		// Add points for having some output:
		if (machineOutput.length()>0) score+=1f;
		
		// Penalty for too much output.
		if (machineOutput.length()>10) score=score*0.8f;
		if (machineOutput.length()>20) score=score*0.5f;
		if (machineOutput.length()>50) score=score*0.2f;
		
		//score-= cyclePenalty*2.0;
		
		// Add points for noop commands...
		for (int i=0;i<dna.getData().size();i++)
		{
			if (dna.getDouble(i)<0.01) score+=0.0001f;
		}
		
		//score += Math.random()*0.1f;
		
		if (cyclePenalty<0.01) score*=0.1f;
		if (cyclePenalty>0.8) score*=0.8f;
		
		score-=scorePenalty;
		
		return score;
				
	}



	
	
	float getScoreForCharacter(char a, char b)
	{
		int maxDiff = 20;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0f;
		float s = ((maxDiff-diff)/(float)maxDiff);
		
		if (diff<5) s*=2.0f;
		if (diff<3) s*=3.0f;
		if (diff==0) s*=5.0f;
		
		
		return s;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append(String.format("Score:%f  Word:%s\n", score, consoleOutput));
		return sb.toString();
		
	}

}


