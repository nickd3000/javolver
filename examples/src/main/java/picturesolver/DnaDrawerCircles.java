package picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerCircles implements DnaDrawer {

    int objectSize = 8;
    boolean enableTransparency = true;
    double radMin = 5;
    double radMax = 80;

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
            rad = (int) (dna.getDouble(baseIndex + 2) * radMax); // 0.75

            if (rad < radMin) rad = (int) radMin;
            if (rad > radMax) rad = (int) radMax;
            //rad = 40;

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
    public double getScoreAdjustments(Chromosome dna, int width, int height) {
        double penalty = 0;
        int numObjects = dna.getSize() / objectSize;
        for (int i = 0; i < numObjects; i++) {
            int baseIndex = objectSize * i;

            double xpos = (dna.getDouble(baseIndex + 0));
            double ypos = (dna.getDouble(baseIndex + 1));
            double rad = (dna.getDouble(baseIndex + 2) );

            penalty += getWallPenalty(xpos, ypos, rad);

        }

        return penalty*0.001;
    }

    public double getWallPenalty(double x, double y, double r) {

        double penalty = 0;

        if (x < r) penalty += Math.abs((r - x));
        if (y < r) penalty += Math.abs((r - y));
        if (x-r > 1 ) penalty += Math.abs(1-(x-r));
        if (y-r > 1 ) penalty += Math.abs(1-(y-r));

        return penalty;
    }

    @Override
    public int getObjectSize() {
        return objectSize;
    }
}
