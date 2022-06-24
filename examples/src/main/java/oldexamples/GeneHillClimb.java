package com.physmo.javolverexamples;

import com.physmo.javolver.Individual;
import com.physmo.minvio.BasicDisplay;

import java.awt.Color;

public class GeneHillClimb extends Individual {
    int dnaSize = 2;

    public GeneHillClimb() {
        dna.init(dnaSize);
        for (int i = 0; i < dnaSize; i += 3) {
            dna.set(i, Math.random() * 2);
        }
    }


    public Individual clone() {
        return (Individual) (new GeneHillClimb());
    }

    public String toString() {
        return "com.physmo.javolverexamples.GeneHillClimb.toString not implemented.";
    }

    public double calculateScore() {
        return getScoreForPosition(dna.getDouble(0), dna.getDouble(1));
    }

    public double getScoreForPosition(double x, double y) {
        int elementsPerObject = 4;
        double[] pts = {0.5, 0.5, 0.5, 0.5,
                0.25, 0.25, 0.25, 0.75,
                0.75, 0.85, 0.15, 0.75,
                0.15, 0.75, 0.25, 0.35};
        double s = 0;

        for (int i = 0; i < pts.length/elementsPerObject; i++) {
            int baseElement = i * elementsPerObject;
            double dx = x - pts[baseElement + 0];
            double dy = y - pts[baseElement + 1];
            s += convertPoint(dx,dy,pts[baseElement + 2],pts[baseElement + 3]);
        }
        return s;
    }

    // Calculate height from position deltas.
    public double convertPoint(double dx, double dy, double rad, double height)
    {
        dx = dx * dx;
        dy = dy * dy;
        if (dx+dy==0) return height;
        double dist = Math.sqrt(dx+dy);
        if (dist>rad) return 0;
        double val = ((rad-dist)/rad)*height;
        return val;

    }

    public void draw(BasicDisplay disp, double scale)
    {
        int steps=40;
        int width = disp.getWidth();
        int height = disp.getHeight();
        double stepSize = (double)width/(double)steps;

        for (int y=0;y<steps;y++) {
            for (int x=0;x<steps;x++) {
                double s = getScoreForPosition(
                        (double)(x)/(steps),
                        (double)(y)/(steps) );

                if (s>1) s=1;

                disp.setDrawColor(new Color(0,(int)(s*255),0));
                disp.drawFilledRect(
                        (int)(x*stepSize),
                        (int)(y*stepSize),
                        (int)((x+1)*stepSize),
                        (int)((y*1)*stepSize));
            }
        }
    }


}
