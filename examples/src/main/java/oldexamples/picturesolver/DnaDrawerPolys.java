package com.physmo.javolverexamples.picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerPolys implements DnaDrawer {

    int objectSize = 12;

    public void render(Graphics2D dc, Chromosome dna, int width, int height) {

        int numObjects = dna.getSize() / objectSize;
        int [] xl = new int[3];
        int [] yl = new int[3];

        int baseIndex = 0;
        double lowestDepth=-100;
        double lowestDepthRolling;
        int lowestDepthIndex;

        for (int i=0;i<numObjects;i++) {

            lowestDepthRolling=100;
            lowestDepthIndex = -1;

            for (int j=0;j<numObjects;j++) {

                double thisDepth = dna.getDouble((objectSize * j)+11);
                if (thisDepth < lowestDepthRolling) {
                    if (thisDepth>lowestDepth) {
                        lowestDepthRolling = thisDepth;
                        lowestDepthIndex = j;
                    }
                }

            }

            if (lowestDepthIndex!=-1) {
                lowestDepth=lowestDepthRolling;
                renderPoly(dc, dna, width, height, lowestDepthIndex);
            }
        }

    }

    public void renderPoly(Graphics2D dc, Chromosome dna, int width, int height, int i) {

        int numObjects = dna.getSize() / objectSize;
        int [] xl = new int[3];
        int [] yl = new int[3];

        int baseIndex = 0;

        baseIndex = objectSize * i;

        xl[0] = (int) (dna.getDouble(baseIndex + 0) * (double)width);
        yl[0] = (int) (dna.getDouble(baseIndex + 1) * (double)height);
        xl[1] = (int) (dna.getDouble(baseIndex + 2) * (double)width);
        yl[1] = (int) (dna.getDouble(baseIndex + 3) * (double)height);
        xl[2] = (int) (dna.getDouble(baseIndex + 4) * (double)width);
        yl[2] = (int) (dna.getDouble(baseIndex + 5) * (double)height);

        int y = (int)(dna.getDouble(baseIndex+6) * (double)height);
        int r = (int)(dna.getDouble(baseIndex+7) * 255);
        int g = (int)(dna.getDouble(baseIndex+8) * 255);
        int b = (int)(dna.getDouble(baseIndex+9) * 255);
        int a = (int)(dna.getDouble(baseIndex+10) * 255 / 2);

        dc.setColor(new Color(r,g,b,a));
        dc.fillPolygon(xl,yl,3);
    }

    public double getScoreAdjustments() {
        return 0;
    }

    public int getObjectSize() {
        return objectSize;
    }
}
