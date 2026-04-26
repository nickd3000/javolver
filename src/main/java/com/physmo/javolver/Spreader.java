package com.physmo.javolver;

import java.util.List;

public class Spreader {
    double force;
    double min;
    double max;

    public Spreader(double force, double min, double max) {
        this.force = force;
        this.max = max;
        this.min = min;
    }


    public void spread(List<Individual> pool) {

        int dnaSize = pool.get(0).getDna().getSize();
        double[] deltas = new double[dnaSize];
        double miniDist = 100;
        double distanceSum = 0;
        for (int i = 0; i < pool.size(); i++) {
            for (int j = 0; j < pool.size(); j++) {
                if (i == j) continue;

                double distance = calculateDifference(pool.get(i), pool.get(j), deltas);
                shift(pool.get(i), deltas, distance);

                if (distance < miniDist) miniDist = distance;
                distanceSum += distance;
            }
        }
        System.out.println("Mindist:" + miniDist + "  avg:"+(distanceSum/(pool.size()*pool.size())));
    }

    public void shift(Individual i1, double[] deltas, double distance) {
        double[] d1 = i1.getDna().getData();
        int dnaSize = d1.length;

        double maxD = 0.5;
        if (distance > maxD) return;
        double nd = 1.0 - (distance / maxD);

        for (int i = 0; i < dnaSize; i++) {
            if (Math.abs(deltas[i])<0.000001) deltas[i]=(Math.random()-0.5)*0.0001;
            d1[i] += (deltas[i]) * nd * force;
            if (d1[i] < min) d1[i] = min;
            if (d1[i] > max) d1[i] = max;
        }


    }


    public double calculateDifference(Individual i1, Individual i2, double[] deltas) {

        double[] d1 = i1.getDna().getData();
        double[] d2 = i2.getDna().getData();
        int dnaSize = d1.length;

        double accumulator = 0;
        double dist = 0;

        for (int i = 0; i < dnaSize; i++) {
            deltas[i] = d1[i] - d2[i];
            accumulator += (deltas[i] * deltas[i]);
        }

        dist = Math.sqrt(accumulator);
        if (dist<0.0001) dist=0.0001;

        for (int i = 0; i < dnaSize; i++) {
            deltas[i] = deltas[i] / dist;
        }

        return dist;
    }
}
