package com.physmo.javolver.selectionoperator;

import com.physmo.javolver.Individual;

import java.util.List;

/**
 * An interface representing a selection operator, used to select an {@code Individual}
 * from a given pool based on a specific selection strategy.
 * This interface is commonly utilized in evolutionary algorithms to determine
 * which individuals proceed to the next generation or are used in genetic operations.
 * <p>
 * Implementers of this interface define different selection schemes, such as
 * - Random selection.
 * - Tournament-based selection.
 * - Roulette wheel selection.
 * <p>
 * The {@code select} method should be implemented to perform the selection logic
 * specific to the concrete strategy.
 */
public interface SelectionOperator {
    /**
     * Selects an {@code Individual} from the provided pool based on a specific selection strategy
     * implemented by the concrete class. The selected individual is typically chosen to participate
     * in the next generation of an evolutionary or genetic algorithm.
     *
     * @param pool the list of {@code Individual} instances representing the pool of potential candidates
     *             for selection. This collection must not be null or empty.
     * @return the selected {@code Individual} from the pool according to the implemented selection logic.
     */
    Individual select(List<Individual> pool);
}

