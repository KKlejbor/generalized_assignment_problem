package edu.umg.generalized_assignment_problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing {
	private final int maximalNumberOfOuterIterations;
	private final int numberOfInnerIterations;
	private final double alpha;
	private double temperature;
	private final int numberOfItems;
	private final int numberOfKnapsacks;
	private final int[][] profits;
	private final int[][] weights;
	private final int[] capacities;
	private GeneralizedAssignmentProblemSolution bestSolution;

	public SimulatedAnnealing(int maximalNumberOfOuterIterations, int numberOfInnerIterations, double temperature, double alpha, String fileName) {
		this.maximalNumberOfOuterIterations = maximalNumberOfOuterIterations;
		this.numberOfInnerIterations = numberOfInnerIterations;
		this.alpha = alpha;
		this.temperature = temperature;
		int numberOfItems = 0;
		int numberOfKnapsacks = 0;
		int[][] profits = null;
		int[][] weights = null;
		int[] capacities = null;
		int[][] assignments = null;
		double value = 0;

		try (Scanner scanner = new Scanner(new File(fileName))) {
			scanner.next();

			numberOfKnapsacks = Integer.parseInt(scanner.next());
			numberOfItems = Integer.parseInt(scanner.next());

			profits = new int[numberOfKnapsacks][numberOfItems];
			weights = new int[numberOfKnapsacks][numberOfItems];
			capacities = new int[numberOfKnapsacks];

			for (int i = 0; i < numberOfKnapsacks; i++) {
				for (int j = 0; j < numberOfItems; j++) {
					profits[i][j] = Integer.parseInt(scanner.next());
				}
			}

			for (int i = 0; i < numberOfKnapsacks; i++) {
				for (int j = 0; j < numberOfItems; j++) {
					weights[i][j] = Integer.parseInt(scanner.next());
				}
			}

			for (int i = 0; i < numberOfKnapsacks; i++) {
				capacities[i] = Integer.parseInt(scanner.next());
			}

			assignments = generateInitialAssignmentsMatrix(numberOfKnapsacks, numberOfItems, weights, capacities);
			value = computeValue(numberOfItems, numberOfKnapsacks, profits, assignments);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			this.numberOfItems = numberOfItems;
			this.numberOfKnapsacks = numberOfKnapsacks;
			this.profits = profits;
			this.weights = weights;
			this.capacities = capacities;
			bestSolution = new GeneralizedAssignmentProblemSolution(assignments, value);
		}
	}

	private int[][] generateInitialAssignmentsMatrix(int numberOfKnapsacks, int numberOfItems, int[][] weights, int[] capacities) {
		int[][] assignments = new int[numberOfKnapsacks][numberOfItems];
		int[] bagUsage = new int[numberOfKnapsacks];

		for (int i = 0; i < numberOfItems; i++) {
			int minIndex = -1;
			int minWeight = Integer.MAX_VALUE;

			for (int j = 0; j < numberOfKnapsacks; j++) {
				if(weights[j][i] < minWeight && bagUsage[j] + weights[j][i] <= capacities[j]){
					minIndex = j;
					minWeight = weights[j][i];
				}
			}

			assignments[minIndex][i] = 1;
			bagUsage[minIndex] += minWeight;
		}

		return assignments;
	}

	private double computeValue(int numberOfItems, int numberOfKnapsacks, int[][] profits, int[][] assignments){
		if (numberOfItems == 0) {
			return 0;
		}

		double value = 0;

		for (int column = 0; column < numberOfItems; column++) {
			for (int row = 0; row < numberOfKnapsacks; row++) {
				if(assignments[row][column] == 1){
					value += profits[row][column] * assignments[row][column];
					break;
				}
			}
		}

		return value;
	}

	private double computeValue(int[][] assignments) {
		return computeValue(numberOfItems, numberOfKnapsacks, profits, assignments);
	}

	private GeneralizedAssignmentProblemSolution findSolutionFromNeighbourhood() {
		int[][] assignments = new int[numberOfKnapsacks][];
		boolean retry;

		for (int i = 0; i < numberOfKnapsacks; i++) {
			assignments[i] = Arrays.copyOf(bestSolution.assignments()[i], numberOfItems);
		}

		do {
			int item1;
			int item2;
			int knapsack1 = 0;
			int knapsack2 = 0;

			item1 = ThreadLocalRandom.current().nextInt(numberOfItems);

			do {
				item2 = ThreadLocalRandom.current().nextInt(numberOfItems);
			} while (item1 == item2);


			for (int i = 0; i < numberOfKnapsacks; i++) {
				if (assignments[i][item1] == 1) {
					knapsack1 = i;
				}
			}

			for (int i = 0; i < numberOfKnapsacks; i++) {
				if (assignments[i][item2] == 1) {
					knapsack2 = i;
					break;
				}
			}

			assignments[knapsack1][item1] = 0;
			assignments[knapsack2][item2] = 0;
			assignments[knapsack1][item2] = 1;
			assignments[knapsack2][item1] = 1;

			int bagUsage1 = 0;
			int bagUsage2 = 0;

			for (int i = 0; i < numberOfItems; i++) {
				bagUsage1 += assignments[knapsack1][i] * weights[knapsack1][i];
				bagUsage2 += assignments[knapsack2][i] * weights[knapsack2][i];
			}

			retry = false;

			if (bagUsage1 > capacities[knapsack1] || bagUsage2 > capacities[knapsack2]) {
				assignments[knapsack1][item2] = 0;
				assignments[knapsack2][item1] = 0;
				assignments[knapsack1][item1] = 1;
				assignments[knapsack2][item2] = 1;

				retry = true;
			}
		}while (retry);

		return new GeneralizedAssignmentProblemSolution(assignments, computeValue(assignments));
	}


	public void printSolution(){
		System.out.printf("Number of Knapsacks: %d\n", numberOfKnapsacks);
		System.out.printf("Number of Items: %d\n", numberOfItems);
		System.out.println();

		System.out.println("Profits: ");
		printMatrix(profits);
		System.out.println();

		System.out.println("Weights: ");
		printMatrix(weights);
		System.out.println();

		System.out.println("Capacities: ");
		for (int i = 0; i < numberOfKnapsacks; i++) {
			System.out.printf("%2d ",capacities[i]);
		}
		System.out.println();
		System.out.println();

		System.out.println("Assignments:");
		printMatrix(bestSolution.assignments());
		System.out.println();
		System.out.printf("Value: %f\n", bestSolution.value());
	}

	private void printMatrix(int[][] matrix) {
		for (int i = 0; i < numberOfKnapsacks; i++) {
			for (int j = 0; j < numberOfItems; j++) {
				System.out.printf("%2d ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	public void solve() {
		for (int i = 0; i < this.maximalNumberOfOuterIterations; i++) {
			for (int j = 0; j < this.numberOfInnerIterations; j++) {
				GeneralizedAssignmentProblemSolution newSolution = findSolutionFromNeighbourhood();

				if (newSolution.value() > bestSolution.value()) {
					bestSolution = newSolution;
				} else if (ThreadLocalRandom.current().nextDouble(1) < Math.exp((bestSolution.value() - newSolution.value()) / this.temperature)) {
					bestSolution = newSolution;
				}
			}

			this.temperature *= this.alpha;
		}
	}

	public GeneralizedAssignmentProblemSolution getBestSolution() {
		return bestSolution;
	}
}
