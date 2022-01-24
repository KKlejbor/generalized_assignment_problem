package edu.umg.generalized_assignment_problem;

import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {
	private final int maximalNumberOfOuterIterations;
	private final int numberOfInnerIterations;
	private final double alpha;
	private double temperature;
	private Solution currentSolution;

	public SimulatedAnnealing(int maximalNumberOfOuterIterations, int numberOfInnerIterations, double temperature, double alpha, Solution initialSolution) {
		this.maximalNumberOfOuterIterations = maximalNumberOfOuterIterations;
		this.numberOfInnerIterations = numberOfInnerIterations;
		this.alpha = alpha;
		this.temperature = temperature;
		this.currentSolution = initialSolution;
	}

	public Solution getCurrentSolution() {
		return currentSolution;
	}

	public int getMaximalNumberOfOuterIterations() {
		return maximalNumberOfOuterIterations;
	}

	public int getNumberOfInnerIterations() {
		return numberOfInnerIterations;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getTemperature() {
		return temperature;
	}

	public void solve() {
		for (int i = 0; i < this.maximalNumberOfOuterIterations; i++) {
			for (int j = 0; j < this.numberOfInnerIterations; j++) {
				Solution newSolution = currentSolution.findSolutionFromNeighbourhood();

				if (currentSolution.getValue() > newSolution.getValue()) {
					currentSolution = newSolution;
				} else if (ThreadLocalRandom.current().nextDouble(1) < Math.exp((currentSolution.getValue() - newSolution.getValue()) / this.temperature)) {
					currentSolution = newSolution;
				}
			}

			this.temperature *= this.alpha;
		}
	}
}
