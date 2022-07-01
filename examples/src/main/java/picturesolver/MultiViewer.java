package picturesolver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

public class MultiViewer {
    BasicDisplay bd;

    public MultiViewer(int width, int height) {
        bd = new BasicDisplayAwt(width, height);
    }

    public void redraw(List<Individual> pool, DnaDrawer drawer, int width, int height) {

        pool.sort(Comparator.comparingDouble(Individual::getScore).reversed());


        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D dc = image.createGraphics();

        //int scaleFactor = width / (int) Math.sqrt(pool.size());
double scaleFactor = ((double)bd.getWidth() / (double)width) / (int) Math.sqrt(pool.size());
//        int scaledWidth = bd.getWidth() / (int) Math.sqrt(pool.size());
//        int scaledHeight = bd.getHeight() / (int) Math.sqrt(pool.size());

        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);

        int x=0,y=0;
        for (int i=0;i<pool.size();i++) {
            drawer.render(dc, pool.get(i).getDna(), width, height);
            bd.drawImage(image, x,y, scaledWidth, scaledHeight);
            x+=scaledWidth;
            if (x+scaledWidth>bd.getWidth()) {
                y+=scaledHeight;
                x=0;
            }
        }

        bd.repaint();

    }

}
