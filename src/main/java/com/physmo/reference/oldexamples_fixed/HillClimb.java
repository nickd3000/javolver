package com.physmo.reference.oldexamples_fixed;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSingle;
import com.physmo.javolver.selectionoperator.SelectionOperatorRoulette;
import com.physmo.javolver.solver.Javolver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.MinvioApp;

import java.awt.Color;

/**
 * This example demonstrates how to use the Javolver library in combination with a 
 * graphical application to simulate a genetic algorithm that solves a problem visually.
 * 
 * Javolver is a library designed for evolutionary computation. It processes a population
 * of "individuals" (potential solutions) using genetic algorithms. Through mutation, selection, 
 * and breeding, it evolves the population until optimal solutions emerge.
 */
public class HillClimb extends MinvioApp {
    private static final int POPULATION_SIZE = 30; // Number of individuals in the population.
    private static final double TICK_INTERVAL = 0.2; // Time interval between simulation updates.
    private static final int FIELD_STEPS = 40; // Resolution of the score field grid.
    private static final double MUTATION_RATE = 0.02; // Mutation chance for DNA evolution.

    // Constants for color settings.
    private static final Color BEST_INDIVIDUAL_COLOR = new Color(255, 0, 0, 100); // Red for the best individual.
    private static final Color OTHER_INDIVIDUAL_COLOR = new Color(255, 255, 255, 100); // Semi-transparent white for others.

    private Javolver javolver; // Core genetic algorithm solver.
    private double tickTimer = 0; // Timer to control update frequency.

    public static void main(String[] args) {
        MinvioApp app = new HillClimb();
        app.start(new BasicDisplayAwt(400, 400), "HillClimb", 10);
    }

    @Override
    public void init(BasicDisplay bd) {
        // Initialize the genetic algorithm solver (Javolver) with necessary configurations.
        javolver = Javolver.builder()
                .dnaSize(2) // Define DNA size. In this case, 2 genes per individual.
                .populationTargetSize(POPULATION_SIZE) // Define the size of the population.
                .keepBestIndividualAlive(false) // Disallow elitism (best individual is not guaranteed to survive).
                .addMutationOperator(new MutationOperatorSingle(MUTATION_RATE)) // Apply mutation with the given rate.
                .setSelectionOperator(new SelectionOperatorRoulette()) // Use roulette selection strategy.
                .setBreedingOperator(new BreedingOperatorUniform()) // Use uniform crossover for breeding.
                .scoreFunction(this::calculateScore) // Specify a custom scoring function.
                .build();
    }

    @Override
    public void update(double delta) {
        // Control simulation step timing.
        tickTimer -= delta;
        if (tickTimer <= 0) {
            // Perform one cycle of evolution (score individuals, select parents, breed, mutate).
            javolver.doOneCycle();
            tickTimer = TICK_INTERVAL;
        }
    }

    @Override
    public void draw(BasicDisplay bd, double delta) {
        // Render the scoring field and individuals.
        drawField(bd); // Draw the grid representing the scoring field.
        drawIndividuals(bd); // Draw all individuals and highlight the best one.
    }

    /**
     * Draw the scoring field in a grid with color-coded scores.
     * Higher scores are represented with brighter green squares.
     */
    private void drawField(BasicDisplay bd) {
        int width = bd.getWidth();
        double stepSize = (double) width / FIELD_STEPS;

        for (int y = 0; y < FIELD_STEPS; y++) {
            for (int x = 0; x < FIELD_STEPS; x++) {
                double normalizedX = (double) x / FIELD_STEPS;
                double normalizedY = (double) y / FIELD_STEPS;
                double score = Math.min(getScoreForPosition(normalizedX, normalizedY), 1);

                bd.setDrawColor(new Color(0, (int) (score * 255), 0));
                bd.drawFilledRect(
                        (int) (x * stepSize),
                        (int) (y * stepSize),
                        (int) stepSize,
                        (int) stepSize
                );
            }
        }
    }

    /**
     * Draw all individuals from the population. The top individual is highlighted in red.
     *
     * @param bd The display context.
     */
    private void drawIndividuals(BasicDisplay bd) {
        Individual topIndividual = javolver.getBestScoringIndividual(); // Best scoring individual.

        for (Individual individual : javolver.getPool()) {
            // Skip the top individual, it will be drawn with its own color.
            if (!topIndividual.equals(individual)) {
                drawIndividual(individual, bd, OTHER_INDIVIDUAL_COLOR, 3);
            }
        }

        // Highlight the best individual with a red marker.
        drawIndividual(topIndividual, bd, BEST_INDIVIDUAL_COLOR, 5);
    }

    /**
     * Draw a single individual as a filled circle on the display.
     * 
     * @param individual The individual (solution) to draw.
     * @param bd         The display context.
     * @param color      The color to use for the individual.
     * @param radius     The radius of the circle representing the individual.
     */
    private void drawIndividual(Individual individual, BasicDisplay bd, Color color, double radius) {
        double x = individual.getDna().getDouble(0) * bd.getWidth();
        double y = individual.getDna().getDouble(1) * bd.getHeight();
        bd.setDrawColor(color);
        bd.drawFilledCircle(x, y, radius);
    }

    /**
     * Calculate the fitness score of an individual based on their position (DNA values).
     * Higher scores represent solutions closer to the target result.
     * 
     * @param individual The individual whose fitness score is calculated.
     * @return The calculated score for the individual.
     */
    private double calculateScore(Individual individual) {
        return getScoreForPosition(
                individual.getDna().getDouble(0),
                individual.getDna().getDouble(1)
        );
    }

    /**
     * Calculate the score for a given position (x, y).
     * This represents the fitness function for the genetic algorithm. In this example,
     * the function evaluates the closeness of a position to predefined points.
     * 
     * @param x The x-coordinate (normalized between 0.0 and 1.0).
     * @param y The y-coordinate (normalized between 0.0 and 1.0).
     * @return The score for the position.
     */
    private double getScoreForPosition(double x, double y) {
        double[] points = {
                0.5, 0.5, 0.5, 0.5,
                0.25, 0.25, 0.25, 0.75,
                0.75, 0.85, 0.15, 0.75,
                0.15, 0.75, 0.25, 0.35
        };
        int elementsPerObject = 4;
        double score = 0;

        for (int i = 0; i < points.length / elementsPerObject; i++) {
            int baseIndex = i * elementsPerObject;
            double dx = x - points[baseIndex];
            double dy = y - points[baseIndex + 1];
            score += convertPoint(dx, dy, points[baseIndex + 2], points[baseIndex + 3]);
        }
        return score;
    }

    /**
     * Calculate the contribution of a single point to the total score for the given position.
     * 
     * @param dx     Delta x (distance in x-direction).
     * @param dy     Delta y (distance in y-direction).
     * @param radius Radius of influence for the point.
     * @param height Maximum contribution score of the point.
     * @return The calculated contribution to the score.
     */
    private double convertPoint(double dx, double dy, double radius, double height) {
        double distanceSquared = dx * dx + dy * dy;
        if (distanceSquared == 0) return height; // Prevent division by zero.
        double distance = Math.sqrt(distanceSquared);
        if (distance > radius) return 0; // Outside the radius of influence.
        return ((radius - distance) / radius) * height; // Linear decay based on distance.
    }
}