package com.physmo.reference.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.minvio.BasicDisplay;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;


public interface ProgramEvaluator {
    void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double x);

    double evaluate(SimpleMachine2 sm, Chromosome dna, double x);

    int getNumberOfSteps();

    void render(SimpleMachine2 sm, Chromosome dna, BasicDisplay bd, double x);

    int getOutputValueHash(SimpleMachine2 sm, Chromosome dna);
    // Return string showing inputs and outputs
    String report(SimpleMachine2 sm, Chromosome dna);
}
