package programming;

import com.physmo.javolver.Chromosome;
import com.physmo.minvio.BasicDisplay;

public class WordEvaluator implements ProgramEvaluator {

    String targetWord = "EVOLVE";

    @Override
    public double evaluate(SimpleMachine sm, Chromosome dna, double step) {

        double score = 0;
        String machineOutput = sm.console;

        int minWordSize = Math.min(targetWord.length(), machineOutput.length());
        float theScore = 0.0f;
        for (int i = 0; i < minWordSize; i++) {
            theScore += getScoreForCharacter(targetWord.charAt(i), machineOutput.charAt(i));
        }
        score = theScore;


        // Add points for having some output:
        if (machineOutput.length() > 0) score += 1f;

        // Penalty for too much output.
        if (machineOutput.length() > 10) score = score * 0.8f;
        if (machineOutput.length() > 20) score = score * 0.5f;
        if (machineOutput.length() > 50) score = score * 0.2f;

        //score-= cyclePenalty*2.0;

        // Add points for noop commands...
        for (int i = 0; i < dna.getData().length; i++) {
            if (dna.getDouble(i) < 0.01) score += 0.0001f;
        }

        //score += Math.random()*0.1f;

//        if (cyclePenalty<0.01) score*=0.1f;
//        if (cyclePenalty>0.8) score*=0.8f;
//
//        score-=scorePenalty;

        return score;
    }


    float getScoreForCharacter(char a, char b) {
        int maxDiff = 20;
        int diff = Math.abs(a - b);
        if (diff > maxDiff) return 0.0f;
        float s = ((maxDiff - diff) / (float) maxDiff);

        if (diff < 5) s *= 2.0f;
        if (diff < 3) s *= 3.0f;
        if (diff == 0) s *= 5.0f;


        return s;
    }

    @Override
    public void preEvaluateStep(SimpleMachine sm, Chromosome dna, double step) {

    }

//    @Override
//    public double evaluate(SimpleMachine sm, Chromosome dna, int step) {
//        return 0;
//    }

    @Override
    public int getNumberOfSteps() {
        return 1;
    }

    @Override
    public void render(SimpleMachine sm, Chromosome dna, BasicDisplay bd, double step) {

    }

}
