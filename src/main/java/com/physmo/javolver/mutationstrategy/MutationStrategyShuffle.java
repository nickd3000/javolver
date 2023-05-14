package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MutationStrategyShuffle implements MutationStrategy {

    Random random = new Random();

    public MutationStrategyShuffle(int count) {
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        double jiggle, value;
        int index;
        int dnaSize = individual.getDna().getSize();
        List<Double> list = new LinkedList<>();

        for (int i = 0; i < dnaSize; i++) {
            list.add(list.size(), individual.getDna().getDouble(i));
        }

        int p1 = random.nextInt(dnaSize);
        int p2 = random.nextInt(dnaSize - 1);

        double val = individual.getDna().getDouble(p1);
        list.remove(p1);
        list.add(p2, val);

        for (int i = 0; i < dnaSize; i++) {
            individual.getDna().set(i, list.get(i));
        }
    }

}
