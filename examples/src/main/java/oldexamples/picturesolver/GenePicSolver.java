package com.physmo.javolverexamples.picturesolver;

import com.physmo.javolver.Individual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Created by nick on 20/07/2016.
 */
public class GenePicSolver extends Individual {

    BufferedImage img = null;
    BufferedImage targetImage;
    static BufferedImage workImage = null;

    static int scoreStep = 0;
    int imgWidth = 200;
    int imgHeight = 200;
    int dnaSize = 0;
    int numberOfDrawingElements = 0;

    Class drawerClass =  null;

    public GenePicSolver(BufferedImage targetImage, Class drawerClass, int numberOfDrawingElements, int scoreStep) {

        if (workImage==null) workImage = new BufferedImage(400,400,BufferedImage.TYPE_INT_RGB); // For scaled compare
        DnaDrawer dnaDrawer = null;
        try {
            dnaDrawer = (DnaDrawer) drawerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.numberOfDrawingElements = numberOfDrawingElements;
        //System.out.println("debug: numberOfDrawingElements: "+numberOfDrawingElements + " dnaDrawer.getObjectSize(): "+ dnaDrawer.getObjectSize());
        dnaSize = numberOfDrawingElements * dnaDrawer.getObjectSize();
        dna.init(dnaSize);

        this.targetImage = targetImage;
        imgWidth = targetImage.getWidth();
        imgHeight = targetImage.getHeight();

        this.drawerClass = drawerClass;
        this.scoreStep = scoreStep;
        this.dnaSize=dnaSize;

        if (img==null) {
            img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
        }
    }


    public BufferedImage getImage() {
        return img;
    }

    public void clampValues() {
        for (int i = 0; i < dna.getData().length; i++) {
            dna.clamp(i, 0.0, 1.0);
        }
    }

    @Override
    public Individual clone() {
        GenePicSolver cloned = new GenePicSolver(targetImage,drawerClass,numberOfDrawingElements,scoreStep);
        for (int i =0;i< dna.getSize();i++) {
            cloned.dna.set(i,dna.getDouble(i));
        }
        return cloned;
    }

    public void resetDna() {
        dna.init(dnaSize);
    }

    public void setDnaFromOther(GenePicSolver other) {
        for (int i =0;i< dna.getSize();i++) {
            dna.set(i,other.dna.getDouble(i));
        }
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

        int compareScaledSize = 10*scoreStep;
        if (compareScaledSize>200) compareScaledSize=200;
if (Math.random()<0.001) System.out.println("compareScaledSize "+compareScaledSize);
        double score = 0;
        //score = testGridOfPoints(scoreStep);
        //score = testRandomPoints(5000);
        score = compareScaled(compareScaledSize);

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
        return getScoreFromColours(col1,col2);
    }

    public double getScoreFromColours(int col1, int col2) {
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

    public double compareScaled(int size) {

        Graphics2D dc = workImage.createGraphics();
        double total=0;
        dc.drawImage(targetImage,0,0,size,size,null);
        dc.drawImage(img,size,0,size,size,null);
        int col1=0,col2=0;
        for (int y=0;y<size;y++) {
            for (int x=0;x<size;x++) {
                col1 = workImage.getRGB(x, y);
                col2 = workImage.getRGB(x+size, y);
                double diff = Math.abs(getScoreFromColours(col1,col2));
                diff=diff*diff;
                total+=diff;
            }
        }

        return (total / (double) (size*size));
    }

}

