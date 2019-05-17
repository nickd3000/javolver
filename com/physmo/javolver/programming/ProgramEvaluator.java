package com.physmo.javolver.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.minvio.BasicDisplay;

public interface ProgramEvaluator {
    void preEvaluateStep(SimpleMachine sm, Chromosome dna, double step);
    double evaluate(SimpleMachine sm, Chromosome dna, double step);
    int getNumberOfSteps();
    void render(SimpleMachine sm, Chromosome dna, BasicDisplay bd,double step);
}
