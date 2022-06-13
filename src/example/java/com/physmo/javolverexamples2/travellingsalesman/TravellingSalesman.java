package com.physmo.javolverexamples2.travellingsalesman;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravellingSalesman {

    List<City> cityList = new ArrayList<>();
    BasicDisplay basicDisplay;

    public static void main(String[] args) {
        TravellingSalesman travellingSalesman = new TravellingSalesman();
        travellingSalesman.go();
    }

    // TODO: we need to initialise dna so that each element is unique location
    // create a new dna initialiser thing class?

    private void go() {
        basicDisplay = new BasicDisplayAwt(400, 400);
        initCityList(25);
        Javolver javolver = Javolver.builder()
                .dnaSize(cityList.size())
                .populationTargetSize(10)
                .keepBestIndividualAlive(true)
                .setSelectionStrategy(new SelectionStrategyTournament(.2))
                .addMutationStrategy(new MutationStrategySwap(1, 1))
                .setBreedingStrategy(new BreedingStrategyTS())
                .dnaInitializer(this::dnaInitializer)
                .scoreFunction(this::scoreFunction)
                .build();


        for (int i = 0; i < 1000000; i++) {
            javolver.doOneCycle();

            Individual bestScoringIndividual = javolver.findBestScoringIndividual();

            System.out.println(bestScoringIndividual.getScore() + "  " + individualToString(bestScoringIndividual));

            drawIndividual(bestScoringIndividual);
        }
    }

    public void initCityList(int numCities) {
        for (int i = 0; i < numCities; i++) {
            City city = new City(Math.random(), Math.random());
            cityList.add(city);
        }
    }

    public double dnaInitializer(int i) {
        return (double) i;
    }

    public double scoreFunction(Individual individual) {

        Map<Integer, Integer> dupes = new HashMap<>();

        Chromosome dna = individual.getDna();
        double distance = 0;
        for (int i = 0; i < dna.getSize(); i++) {
            int index1 = (int) dna.getDouble(i);
            int index2 = (int) dna.getDouble((i + 1) % dna.getSize());
            City city1 = cityList.get(index1);
            City city2 = cityList.get(index2);
            distance += city1.distance(city2);

            if (dupes.containsKey(index1)) {
                dupes.put(index1, dupes.get(index1) + 1);
            } else {
                dupes.put(index1, 1);
            }
        }

        for (Integer integer : dupes.keySet()) {
            if (dupes.get(integer) > 1) {
                distance += dupes.get(integer) * 10;
            }
        }


        double max = dna.getSize() * 2;

        return Math.max(max - distance, 0);
    }

    public String individualToString(Individual individual) {
        int size = individual.getDna().getSize();
        String str = "";
        for (int i = 0; i < size; i++) {
            str += ", " + (int) individual.getDna().getDouble(i);
        }
        return str;
    }

    public void drawIndividual(Individual individual) {
        basicDisplay.cls(Color.LIGHT_GRAY);

        double scale = 400;
        int size = individual.getDna().getSize();


        basicDisplay.setDrawColor(Color.magenta);
        for (int i = 0; i < size - 1; i++) {
            City city1 = cityList.get((int) individual.getDna().getDouble(i));
            City city2 = cityList.get((int) individual.getDna().getDouble(i + 1));
            basicDisplay.drawLine(city1.x * scale, city1.y * scale, city2.x * scale, city2.y * scale);
        }
        basicDisplay.setDrawColor(Color.white);
        for (City city : cityList) {
            basicDisplay.drawFilledCircle(city.x * scale, city.y * scale, 5);
        }
        basicDisplay.repaint();
    }


}
