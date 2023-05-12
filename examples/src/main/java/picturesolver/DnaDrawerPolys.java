package picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerPolys implements DnaDrawer {

    int objectSize = 12;
    double penaltyScale = 0.0050;

    @Override
    public void render(Graphics2D dc, Chromosome dna, int width, int height) {

        int numObjects = dna.getSize() / objectSize;
//        int[] xl = new int[3];
//        int[] yl = new int[3];

        int baseIndex = 0;
        double lowestDepth = -100;
        double lowestDepthRolling;
        int lowestDepthIndex;

        for (int i = 0; i < numObjects; i++) {

            lowestDepthRolling = 100;
            lowestDepthIndex = -1;

            for (int j = 0; j < numObjects; j++) {

                double thisDepth = dna.getDouble((objectSize * j) + 11);
                if (thisDepth < lowestDepthRolling) {
                    if (thisDepth > lowestDepth) {
                        lowestDepthRolling = thisDepth;
                        lowestDepthIndex = j;
                    }
                }

            }

            if (lowestDepthIndex != -1) {
                lowestDepth = lowestDepthRolling;
                renderPoly(dc, dna, width, height, lowestDepthIndex);
            }
        }

    }

    public void renderPoly(Graphics2D dc, Chromosome dna, int width, int height, int i) {

        int[] xl = new int[3];
        int[] yl = new int[3];
        int baseIndex = objectSize * i;
        xl[0] = (int) (dna.getDouble(baseIndex + 0) * (double) width);
        yl[0] = (int) (dna.getDouble(baseIndex + 1) * (double) height);
        xl[1] = (int) (dna.getDouble(baseIndex + 2) * (double) width);
        yl[1] = (int) (dna.getDouble(baseIndex + 3) * (double) height);
        xl[2] = (int) (dna.getDouble(baseIndex + 4) * (double) width);
        yl[2] = (int) (dna.getDouble(baseIndex + 5) * (double) height);

        int y = (int) (dna.getDouble(baseIndex + 6) * (double) height);
        int r = (int) (dna.getDouble(baseIndex + 7) * 255);
        int g = (int) (dna.getDouble(baseIndex + 8) * 255);
        int b = (int) (dna.getDouble(baseIndex + 9) * 255);
        int a = (int) (dna.getDouble(baseIndex + 10) * 255 / 2);

        if (r > 0xff) r = 0xff;
        if (g > 0xff) g = 0xff;
        if (b > 0xff) b = 0xff;
        if (a > 0xff) a = 0xff;
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        if (a < 0) a = 0;

        dc.setColor(new Color(r, g, b, a));
        dc.fillPolygon(xl, yl, 3);
    }

    @Override
    public double getScoreAdjustments(Chromosome dna, int width, int height) {
        int numObjects = dna.getSize() / objectSize;
        double[] xl = new double[3];
        double[] yl = new double[3];
        double penalty = 0;
        for (int i = 0; i < numObjects; i++) {
            int baseIndex = objectSize * i;
            xl[0] = (dna.getDouble(baseIndex + 0));
            yl[0] = (dna.getDouble(baseIndex + 1));
            xl[1] = (dna.getDouble(baseIndex + 2));
            yl[1] = (dna.getDouble(baseIndex + 3));
            xl[2] = (dna.getDouble(baseIndex + 4));
            yl[2] = (dna.getDouble(baseIndex + 5));
            for (int j = 0; j < 3; j++) {
                penalty += calculatePositionPenalty(xl[j], 1);
                penalty += calculatePositionPenalty(yl[j], 1);
            }
        }

        return penalty;
    }

    public double calculatePositionPenalty(double pos, double limit) {

        if (pos < 0) return (0 - pos) * penaltyScale;
        if (pos > limit) return (limit - pos) * penaltyScale * -1;
        return 0;
    }

    public int getObjectSize() {
        return objectSize;
    }
}
