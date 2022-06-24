package com.physmo.javolverexamples;

import com.physmo.javolver.Descent;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

public class SpherePacker extends MinvioApp {

    int populationSize = 25;
    int numberOfSpheres = 8;
    Javolver testEvolver;

    public static void main(String[] args) {
        MinvioApp app = new SpherePacker();
        app.start(new BasicDisplayAwt(300, 300), "Sphere Packer", 60);
    }

    @Override
    public void init(BasicDisplay bd) {
        testEvolver = new Javolver(new GeneSpherePacker(numberOfSpheres), populationSize)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(0.01, 1.5))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                //.setSelectionStrategy(new SelectionStrategyRouletteRanked())
                .setBreedingStrategy(new BreedingStrategyUniform());
    }


    @Override
    public void draw(BasicDisplay bd, double delta) {

        int boxSize = 200;
        int pad = 50;

        for (int i=0;i<10;i++) {
            Descent.descent2(testEvolver, 3, 0.1);
        }

        GeneSpherePacker top = (GeneSpherePacker) testEvolver.findBestScoringIndividual();

        bd.cls(new Color(64, 64, 64));
        top.draw(bd, pad, pad);

        bd.setDrawColor(Color.white);
        bd.drawRect(pad, pad, boxSize, boxSize);

    }


}



