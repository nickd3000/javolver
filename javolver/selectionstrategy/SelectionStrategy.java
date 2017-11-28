package javolver.selectionstrategy;

import java.util.List;
import javolver.Individual;
import javolver.JavolverConfig;

public interface SelectionStrategy {
	public Individual select(JavolverConfig config, List<Individual> pool);
}

