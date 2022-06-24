package travellingsalesman;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.mutationstrategy.MutationStrategyShuffle;
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

    int numCities = 10; // (for brute force, use 11)
    List<City> cityList = new ArrayList<>();
    BasicDisplay basicDisplay;

    public static void main(String[] args) throws Exception {
        TravellingSalesman travellingSalesman = new TravellingSalesman();
        travellingSalesman.go();
    }

    private void go() {
        initCityList(numCities);

        bruteForce(numCities);


        basicDisplay = new BasicDisplayAwt(400, 400);

        Javolver javolver = Javolver.builder()
                .dnaSize(cityList.size())
                .populationTargetSize(10)
                .keepBestIndividualAlive(true)
                .setSelectionStrategy(new SelectionStrategyTournament(.2))
                .addMutationStrategy(new MutationStrategySwap(1, 2))
                .addMutationStrategy(new MutationStrategyShuffle(2))
                .setBreedingStrategy(new BreedingStrategyTS())
                .dnaInitializer(this::dnaInitializer)
                .scoreFunction(this::scoreFunction)
                .build();


        for (int i = 0; i < 1000000; i++) {
            javolver.doOneCycle();

            Individual bestScoringIndividual = javolver.getBestScoringIndividual();

            System.out.println(bestScoringIndividual.getScore() + "  " + individualToString(bestScoringIndividual));

            basicDisplay.cls(Color.LIGHT_GRAY);
            drawIndividual(createIndividualFromArray(bruteForceBestSolution), Color.BLUE);
            drawIndividual(bestScoringIndividual, Color.magenta);


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

    public void drawIndividual(Individual individual, Color lineColor) {


        double scale = 400;
        int size = individual.getDna().getSize();

        basicDisplay.setDrawColor(lineColor);
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

    public void bruteForce(int size) {
        int[] list = new int[size];
        for (int i=0;i<size;i++) {
            list[i]=i;
        }
        permute(list.length, list);

    }

    public Individual createIndividualFromArray(int[] list) {
        Individual individual = new Individual(list.length);
        for (int i=0;i<list.length;i++) {
            individual.getDna().set(i,list[i]);
        }
        return individual;
    }

    public double calculateLengthFromArray(int[] list) {
        Individual individual = createIndividualFromArray(list);
        return scoreFunction(individual);
    }

    double bruteForceMinDistance = -1;
    int[] bruteForceBestSolution = new int[numCities];

    public void checkSolution(int[] list) {
        double length = calculateLengthFromArray(list);
        if (length>bruteForceMinDistance || bruteForceMinDistance==-1) {
            bruteForceMinDistance=length;
            System.arraycopy(list, 0, bruteForceBestSolution,0,list.length);
        }
    }

    public void permute(int k, int[] list) {
        if (k==1) {
            // output a
            checkSolution(list);
        } else {
            permute(k-1, list);
            for (int i=0;i<k-1;i++) {
                if ((k&1)==0) // If even
                {
                    swap(i, k-1, list);
                } else {
                    swap(0, k-1, list);
                }
                permute(k-1, list);
            }
        }
    }

    public void swap(int i, int j, int[] list) {
        int tmp = list[i];
        list[i]=list[j];
        list[j]=tmp;
    }
}
