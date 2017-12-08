package javolver.selectionstrategy;

import java.util.List;
import javolver.Individual;

public interface SelectionStrategy {
	public Individual select(List<Individual> pool);
}

