package picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Color;
import java.awt.Graphics2D;

public class DnaDrawerSimpleSquares implements DnaDrawer {

    int objectSize = 7;
    double squareSize = 40;

    @Override
    public void render(Graphics2D dc, Chromosome dna, int width, int height) {

        int numObjects = dna.getSize() / objectSize;

        int baseIndex = 0;
        for (int i = 0; i < numObjects; i++) {
            baseIndex = objectSize * i;
            int x = (int) (dna.getDouble(baseIndex + 0) * (double) width);
            int y = (int) (dna.getDouble(baseIndex + 1) * (double) height);
            int r = (int) (dna.getDouble(baseIndex + 2) * 255);
            int g = (int) (dna.getDouble(baseIndex + 3) * 255);
            int b = (int) (dna.getDouble(baseIndex + 4) * 255);
            int a = (int) (dna.getDouble(baseIndex + 5) * 255);
            int size = (int) (dna.getDouble(baseIndex + 6) * squareSize);
            dc.setColor(new Color(clampByte(r), clampByte(g), clampByte(b), clampByte(a)));
            dc.fillRect(x, y, size, size);

        }

    }

    public int clampByte(int val) {
        if (val < 0) return 0;
        if (val > 0xff) return 0xff;
        return val;
    }

    @Override
    public double getScoreAdjustments(Chromosome dna, int width, int height) {
        int numObjects = dna.getSize() / objectSize;

        double penalty = 0;
        int baseIndex = 0;
        for (int i = 0; i < numObjects; i++) {
            baseIndex = objectSize * i;
            int x = (int) (dna.getDouble(baseIndex + 0) * (double) width);
            int y = (int) (dna.getDouble(baseIndex + 1) * (double) height);
            int size = (int) (dna.getDouble(baseIndex + 6) * squareSize);
            penalty += calculatePositionPenalty(x, size, width);
            penalty += calculatePositionPenalty(y, size, height);
        }
        return penalty / width;
    }

    public double calculatePositionPenalty(double pos, double size, double fieldSize) {
        double scale = 0.5;
        if (pos < 0) return (0 - pos) * scale;
        if (pos + size > fieldSize) return (fieldSize - (pos + size)) * scale * -1;
        return 0;
    }

    @Override
    public int getObjectSize() {
        return objectSize;
    }
}
