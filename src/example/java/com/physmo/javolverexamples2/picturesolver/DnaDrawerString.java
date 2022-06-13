package com.physmo.javolverexamples2.picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerString implements DnaDrawer {

    int objectSize = 2;

    @Override
    public void render(Graphics2D dc, Chromosome dna, int objectCount, int width, int height) {

        int numObjects = dna.getSize() / objectSize;
        if (objectCount < numObjects) numObjects = objectCount;

        int baseIndex;
        double radius = Math.max(width, height) * 0.5;
        dc.setColor(new Color(0, 0, 0, 100));
        dc.setStroke(new BasicStroke(3));
        for (int i = 0; i < numObjects; i++) {
            baseIndex = objectSize * i;
            double a1 = dna.getDouble(baseIndex + 0);
            double a2 = dna.getDouble(baseIndex + 1);

            double x1 = Math.sin(6.0 * a1) * radius;
            double y1 = Math.cos(6.0 * a1) * radius;
            double x2 = Math.sin(6.0 * a2) * radius;
            double y2 = Math.cos(6.0 * a2) * radius;

            dc.drawLine(width / 2 + (int) x1, height / 2 + (int) y1, width / 2 + (int) x2, height / 2 + (int) y2);

        }
    }


    @Override
    public double getScoreAdjustments(Chromosome dna, int objectCount, int width, int height) {
        return 0;
    }

    @Override
    public int getObjectSize() {
        return objectSize;
    }
}
