package com.physmo.javolverexamples;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

public class HillClimb extends MinvioApp {
    int populationSize = 30;
    Javolver javolver;

    public static void main(String[] args) {
        MinvioApp app = new HillClimb();
        app.start(new BasicDisplayAwt(400, 400), "HillClimb", 60);
    }

    @Override
    public void init(BasicDisplay bd) {
        javolver = new Javolver(new GeneHillClimb(), populationSize)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySingle(0.02))
                .setSelectionStrategy(new SelectionStrategyRoulette())
                .setBreedingStrategy(new BreedingStrategyUniform());
    }

    @Override
    public void draw(BasicDisplay bd, double delta) {

        javolver.doOneCycle();

        // Find the best individual for drawing.
        GeneHillClimb top = (GeneHillClimb) javolver.findBestScoringIndividual();

        bd.cls(new Color(64, 64, 64));
        top.draw(bd, 1);

        // Draw each individual in pool.
        bd.setDrawColor(Color.white);
        for (Individual i : javolver.getPool()) {
            double x = i.dna.getDouble(0) * (double) bd.getWidth();
            double y = i.dna.getDouble(1) * (double) bd.getWidth();
            bd.drawFilledCircle(x, y, 3);
        }

    }

}
