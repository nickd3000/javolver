package com.physmo.javolver.examples.picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.*;

public class DnaDrawerCircles implements DnaDrawer {

    int objectSize = 8;
    boolean enableTransparency = true;
    double radiusDivider = 6.0;


    @Override
    public void render(Graphics2D dc, Chromosome dna, int width, int height) {
        int numObjects = dna.getSize() / objectSize;
        int xpos = 0, ypos = 0, rad = 0;
        float[] cols = new float[4];
        int baseIndex = 0;
        for (int i = 0; i < numObjects; i++) {
            baseIndex = objectSize * i;

            xpos = (int) (dna.getDouble(baseIndex + 0) * width);
            ypos = (int) (dna.getDouble(baseIndex + 1) * height);
            rad = (int) (dna.getDouble(baseIndex + 2) * (width / radiusDivider) * 3.75); // 0.75

            if (rad < 20) rad = 20;
            rad = 40;

            for (int c = 0; c < 4; c++) {
                cols[c] = (float) dna.getDouble(baseIndex + 3 + c);

                if (cols[c] < 0.0) cols[c] = 0.0f;
                if (cols[c] > 1.0) cols[c] = 1.0f;
            }

            Color c = null;
            if (enableTransparency) {
                c = new Color(cols[0], cols[1], cols[2], cols[3] / 2); ///2.0f);
            } else {
                c = new Color(cols[0], cols[1], cols[2]);
            }

            dc.setColor(c);

            dc.fillOval(xpos - (rad / 2), ypos - (rad / 2), rad, rad);

        }

    }

    @Override
    public double getScoreAdjustments() {
        return 0;
    }
}
