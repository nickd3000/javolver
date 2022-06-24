package travellingsalesman;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingstrategy.BreedingStrategy;

import java.util.ArrayList;
import java.util.List;

public class BreedingStrategyTS implements BreedingStrategy {
    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        Individual child1 = parent1.clone();
        int dnaSize = parent1.dna.getData().length;

        boolean[] usedList = new boolean[dnaSize];
        for (int i = 0; i < dnaSize; i++) {
            usedList[i] = false;
        }

        List<Integer> skipList = new ArrayList<>();

        for (int i = 0; i < dnaSize; i++) {
            int c1 = (int) parent1.getDna().getDouble(i);
            int c2 = (int) parent2.getDna().getDouble(i);

            if (!usedList[c1]) {
                child1.getDna().set(i, c1);
                usedList[c1] = true;
            } else if (!usedList[c2]) {
                child1.getDna().set(i, c2);
                usedList[c2] = true;
            } else {
                skipList.add(i);
            }
        }

        for (Integer skipId : skipList) {
            int c1 = findRandomUnusedOne(usedList);
            child1.getDna().set(skipId, c1);
        }

        List<Individual> returnList = new ArrayList<>();
        returnList.add(child1);
        return returnList;
    }

    int findRandomUnusedOne(boolean[] usedList) {
        for (int j = 0; j < 1000; j++) {
            int i = (int) (Math.random() * usedList.length);
            if (!usedList[i]) return i;
        }

        System.out.println("Timed out BreedingStrategy");

        return 0;
    }


}
