package com.physmo.javolverexamples2.picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerSimpleSquares implements DnaDrawer {

    int objectSize = 7;

    @Override
    public void render(Graphics2D dc, Chromosome dna, int width, int height) {

        int numObjects = dna.getSize() / objectSize;

        int baseIndex = 0;
        for (int i=0;i<numObjects;i++) {
            baseIndex = objectSize * i;
            int x = (int)(dna.getDouble(baseIndex+0) * (double)width);
            int y = (int)(dna.getDouble(baseIndex+1) * (double)height);
            int r = (int)(dna.getDouble(baseIndex+2) * 255);
            int g = (int)(dna.getDouble(baseIndex+3) * 255);
            int b = (int)(dna.getDouble(baseIndex+4) * 255);
            int a = (int)(dna.getDouble(baseIndex+5) * 255);
            int size =(int)(dna.getDouble(baseIndex+6) * 40);
            dc.setColor(new Color(r,g,b,a));
            dc.fillRect(x,y,size,size);

        }

    }

    @Override
    public double getScoreAdjustments() {
        return 0;
    }

    @Override
    public int getObjectSize() {
        return objectSize;
    }
}
