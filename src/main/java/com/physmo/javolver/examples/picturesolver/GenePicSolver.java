package com.physmo.javolver.examples.picturesolver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.physmo.javolver.Individual;

/**
 * Created by nick on 20/07/2016.
 */
public class GenePicSolver extends Individual {

    BufferedImage img = null;
    BufferedImage targetImage;

    int scoreStep = 0;
    int imgWidth = 200;
    int imgHeight = 200;
    int dnaSize = 0;

    Class drawerClass =  null;

    public GenePicSolver(BufferedImage targetImage, Class drawerClass, int dnaSize, int scoreStep) {

        dna.init(dnaSize);

        this.targetImage = targetImage;
        imgWidth = targetImage.getWidth();
        imgHeight = targetImage.getHeight();

        this.drawerClass = drawerClass;
        this.scoreStep = scoreStep;
        this.dnaSize=dnaSize;

        img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    }


    public BufferedImage getImage() {
        return img;
    }

    public void clampValues() {
        for (int i = 0; i < dna.getData().size(); i++) {
            dna.clamp(i, 0.0, 1.0);
        }
    }

    @Override
    public Individual clone() {
        return new GenePicSolver(targetImage,drawerClass,dnaSize,scoreStep);
    }

    @Override
    public String toString() {
        return String.format("%.4f", score);
    }

    @Override
    public double calculateScore() {
        clampValues();

        Graphics2D dc = img.createGraphics();

        dc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        dc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

        //dc.setColor(Color.GRAY);
        dc.setColor(Color.WHITE);

        dc.fillRect(0, 0, imgWidth, imgHeight);

        DnaDrawer drawer = null;
        try {
            drawer = (DnaDrawer) drawerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        drawer.render(dc, dna, imgWidth, imgHeight);

        double score = 0;
        score = testGridOfPoints(scoreStep);

        double averaged = score * 500.0;

        return averaged;
    }


    public double testRandomPoints(int numPoints) {
        double total = 0;
        int count = 0;
        for (int i = 0; i < numPoints; i++) {
            total += getScoreForPosition(
                    (int) (Math.random() * imgWidth),
                    (int) (Math.random() * imgHeight));
            count++;
        }
        return (total / (double) count);
    }

    public double testGridOfPoints(int step) {
        double total = 0;
        int count = 0;
        int ystart = (int) (Math.random() * step);
        int xstart = (int) (Math.random() * step);
        for (int y = ystart; y < imgHeight; y += step) {
            for (int x = xstart; x < imgWidth; x += step) {
                total += Math.abs(getScoreForPosition(x, y));
                count++;
            }
        }
        return (total / (double) count);
    }

    public double getScoreForPosition(int x, int y) {
        int col1 = targetImage.getRGB(x, y);
        int col2 = img.getRGB(x, y);

        int r1 = (col1 >> 16) & 0xff;
        int g1 = (col1 >> 8) & 0xff;
        int b1 = col1 & 0xff;
        int r2 = (col2 >> 16) & 0xff;
        int g2 = (col2 >> 8) & 0xff;
        int b2 = col2 & 0xff;

        r1 = r1 - r2;
        g1 = g1 - g2;
        b1 = b1 - b2;

        double max = 450;
        // double max = 4072; //sqrt(255*255*255)=4072;
        double dist = Math.sqrt((double) ((r1 * r1) + (g1 * g1) + (b1 * b1)));
        if (dist < 0) dist = 0;
        if (dist > max) dist = max;

        dist = (max - dist) / max;
        //dist = dist * dist;
        return dist;
    }
}

