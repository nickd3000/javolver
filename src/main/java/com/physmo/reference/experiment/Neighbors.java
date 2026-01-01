package com.physmo.reference.experiment;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorCrossover;
import com.physmo.javolver.mutationoperator.MutationOperatorShuffle;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;
import com.physmo.minvio.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// NOTE: this turned out to be a bit boring.
public class Neighbors extends MinvioApp {

    int gridWidth = 30;
    int gridHeight = 30;
    int numColors = 6;
    List<Rule> rules = new ArrayList<>();
    static final int NORTH = 0;
    static final int EAST = 1;
    static final int SOUTH = 2;
    static final int WEST = 3;
    Solver solver;
    int iteration = 0;
    Individual bestScoringIndividual = null;
    int numRandomRules = 10;
    int iterationsPerTick = 10;

    public static void main(String[] args) {
        MinvioApp app = new Neighbors();
        app.start(new BasicDisplayAwt(400, 400), "Sphere Packer", 20);
    }

    @Override
    public void init(BasicDisplay bd) {
        setRandomRules();

        solver = Javolver.builder().
                populationTargetSize(50).
                dnaSize(gridWidth * gridHeight).
                keepBestIndividualAlive(false).
                addMutationOperator(new MutationOperatorSimple(1, 0.5)).
                addMutationOperator(new MutationOperatorShuffle(1)).
                addMutationOperator(new MutationOperatorSwap(0.1, 2)).
                setSelectionOperator(new SelectionOperatorTournament(0.15)).
                setBreedingOperator(new BreedingOperatorCrossover()).
                scoreFunction(this::calculateScoreDiversity).build();
    }


    public void setRandomRules() {
        for (int i = 0; i < numRandomRules; i++) {
            int self = (int) (Math.random() * numColors);
            int other = (int) (Math.random() * numColors);
            int dir = (int) (Math.random() * 4);
            int adjust = Math.random() < 0.5 ? -1 : 1;
            rules.add(new Rule(self, other, dir, adjust));
        }
    }

    @Override
    public void update(BasicDisplay bd, double delta) {
        for (int i = 0; i < iterationsPerTick; i++) {
            iteration++;
            solver.doOneCycle();
            bestScoringIndividual = solver.getBestScoringIndividual();
        }
        System.out.printf("Iteration: %2d  score: %4.1f   solution: %s %n", iteration, bestScoringIndividual.getScore(), "");
    }

    @Override
    public void draw(double delta) {
        if (bestScoringIndividual == null) return;
        render(bestScoringIndividual,  getBasicDisplay().getDrawingContext().getWidth(), getBasicDisplay().getDrawingContext().getHeight());
    }


    public double calculateScore(Individual individual) {
        double score = 0;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                for (Rule rule : rules) {
                    int thisCol = getWrappedDnaValue(individual, x, y);
                    if (thisCol != rule.self()) continue;
                    int otherCol = getWrappedDnaValueInDirection(individual, x, y, rule.dir());
                    if (otherCol == rule.target()) {
                        score += rule.adjust();
                    }
                }
            }
        }


        return score;
    }

    public double calculateScoreDiversity(Individual individual) {
        double totalScore = 0;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                Set<Integer> uniqueColors = new HashSet<>();
                int thisCol = getWrappedDnaValue(individual, x, y);

                // Tighten window to 3x3 (dx/dy from -1 to 1)
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int otherCol = getWrappedDnaValue(individual, x + dx, y + dy);
                        uniqueColors.add(otherCol);

                        // Solidarity Bonus: Reward cells that are the same as their neighbors
                        // This forces the "blobs" to become solid instead of noisy.
                        if (thisCol == otherCol) totalScore += 0.5;
                    }
                }

                int count = uniqueColors.size();
                if (count == 2) {
                    totalScore += 20.0; // Significant reward for exactly 2 colors
                } else if (count == 1) {
                    totalScore += 5.0;  // Reward for pure color patches
                } else {
                    totalScore -= 10.0; // Heavy penalty for noise (>2 colors)
                }
            }
        }

        return totalScore;
    }

    public void render(Individual individual, int width, int height) {
        int xSpan = width / gridWidth;
        int ySpan = height / gridHeight;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                int val = getWrappedDnaValue(individual, x, y);
                Color col = Utils.getDistinctColor(val, 1);
                setDrawColor(col);
                drawFilledRect(x * xSpan, y * ySpan, xSpan, ySpan);
            }
        }
    }

    public int getWrappedDnaValue(Individual individual, int inX, int inY) {
        int x = (inX % gridWidth + gridWidth) % gridWidth;
        int y = (inY % gridHeight + gridHeight) % gridHeight;


        return mapDnaToColorInt(individual.getDna().getDouble(x + (y * gridWidth)));
    }

    public int getWrappedDnaValueInDirection(Individual individual, int inX, int inY, int dir) {
        int x = inX;
        int y = inY;
        switch (dir) {
            case NORTH -> y--;
            case EAST -> x++;
            case SOUTH -> y++;
            case WEST -> x--;
        }
        return getWrappedDnaValue(individual, x, y);
    }

    public int mapDnaToColorInt(double val) {
        int remapped = (int) (val * numColors);
        if (remapped < 0) return 0;
        return Math.min(remapped, numColors-1);
    }
}

record Rule(int self, int target, int dir, double adjust) {
};
