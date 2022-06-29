package picturesolver;

import com.physmo.javolver.Chromosome;

import java.awt.Graphics2D;

/**
 * Draw an image to a Graphics2D device context using a Cromosome
 */
public interface DnaDrawer {
    /**
     * @param dc  Graphics context to draw to
     * @param dna Chromosome to base drawing from
     */
    void render(Graphics2D dc, Chromosome dna, int width, int height);

    /**
     *
     */
    double getScoreAdjustments(Chromosome dna, int width, int height);

    /**
     * @return Number of dna elements that make up an object.
     */
    int getObjectSize();
}
