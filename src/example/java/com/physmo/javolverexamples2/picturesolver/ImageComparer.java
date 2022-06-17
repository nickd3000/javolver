package com.physmo.javolverexamples2.picturesolver;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageComparer {
    static BufferedImage workImage = null;
    static Graphics2D dc = null;
    static double max = Math.sqrt((0xff * 0xff) + (0xff * 0xff) + (0xff * 0xff));
    BufferedImage targetImage;

    public ImageComparer(BufferedImage targetImage) {
        this.targetImage = targetImage;
        workImage = new BufferedImage(targetImage.getWidth() * 4, targetImage.getHeight() * 3, BufferedImage.TYPE_INT_RGB); // For scaled compare
    }

    public double compareScaled(BufferedImage testImage, int size, int skip) {

        if (dc == null) dc = workImage.createGraphics();

        double total = 0;

        dc.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        dc.drawImage(targetImage, 0, 0, size, size, null);
        dc.drawImage(testImage, size, 0, size, size, null);
        int col1, col2, count = 0;
        for (int y = 0; y < size; y += skip) {
            for (int x = 0; x < size; x += skip) {
                col1 = workImage.getRGB(x, y);
                col2 = workImage.getRGB(x + size, y);
                double diff = Math.abs(getScoreFromColours(col1, col2));
                diff = diff * diff;
                total += diff;
                count++;
            }
        }

        return (total / (double) (count));
    }

    public static double getScoreFromColours(int col1, int col2) {
        int r1 = (col1 >> 16) & 0xff;
        int g1 = (col1 >> 8) & 0xff;
        int b1 = col1 & 0xff;
        int r2 = (col2 >> 16) & 0xff;
        int g2 = (col2 >> 8) & 0xff;
        int b2 = col2 & 0xff;

        r1 = r1 - r2;
        g1 = g1 - g2;
        b1 = b1 - b2;

        double dist = Math.sqrt(((r1 * r1) + (g1 * g1) + (b1 * b1)));
        if (dist < 0) dist = 0;
        if (dist > max) dist = max;

        dist = (max - dist) / max;

        return dist;
    }
}
