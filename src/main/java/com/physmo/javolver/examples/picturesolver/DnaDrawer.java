package com.physmo.javolver.examples.picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.*;

/**
 * Draw an image to a Graphics2D deveice context using a Cromosome
 */
public interface DnaDrawer {
    /**
     *
     * @param dc    Graphics context to draw to
     * @param dna   Chromosome to base drawing from
     */
    public void render(Graphics2D dc, Chromosome dna, int width, int height);

    /**
     *
     */
    public double getScoreAdjustments();
}
