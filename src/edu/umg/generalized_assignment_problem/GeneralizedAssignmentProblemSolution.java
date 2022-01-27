package edu.umg.generalized_assignment_problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GeneralizedAssignmentProblemSolution implements Solution {
	private final int numberOfItems;
	private final int numberOfKnapsacks;
	private final int[][] profits;
	private final int[][] weights;
	private final int[] capacities;
	private final int[][] assignments;
	private final double value;

	public GeneralizedAssignmentProblemSolution(String fileName) {
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
			this.assignments = assignments;
			this.value = value;
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

	private GeneralizedAssignmentProblemSolution(int numberOfItems, int numberOfKnapsacks, int[][] profits, int[][] weights, int[] capacities, int[][] assignments, double value) {
		this.numberOfItems = numberOfItems;
		this.numberOfKnapsacks = numberOfKnapsacks;

		this.profits = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.profits[i] = Arrays.copyOf(profits[i], numberOfItems);
		}

		this.weights = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.weights[i] = Arrays.copyOf(weights[i], numberOfItems);
		}

		this.capacities = Arrays.copyOf(capacities, numberOfKnapsacks);

		this.assignments = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.assignments[i] = Arrays.copyOf(assignments[i], numberOfItems);
		}

		this.value = value;
	}

	private GeneralizedAssignmentProblemSolution(GeneralizedAssignmentProblemSolution oldSolution){
		this.numberOfItems = oldSolution.numberOfItems;
		this.numberOfKnapsacks = oldSolution.numberOfKnapsacks;

		this.profits = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.profits[i] = Arrays.copyOf(oldSolution.profits[i], numberOfItems);
		}

		this.weights = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.weights[i] = Arrays.copyOf(oldSolution.weights[i], numberOfItems);
		}

		this.capacities = Arrays.copyOf(oldSolution.capacities, numberOfKnapsacks);

		this.assignments = findAssignmentsMatrixFromNeighbourhood(oldSolution);

		this.value = computeValue();
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

	private double computeValue() {
		return computeValue(this.numberOfItems, this.numberOfKnapsacks, this.profits, this.assignments);
	}


	private int[][] findAssignmentsMatrixFromNeighbourhood(GeneralizedAssignmentProblemSolution oldSolution) {
		int[][] assignments = new int[oldSolution.numberOfKnapsacks][];
		boolean retry;

		for (int i = 0; i < oldSolution.numberOfKnapsacks; i++) {
			assignments[i] = Arrays.copyOf(oldSolution.assignments[i], oldSolution.numberOfItems);
		}

		do {
			int item1 = ThreadLocalRandom.current().nextInt(oldSolution.numberOfItems);
			int item2;

			do {
				item2 = ThreadLocalRandom.current().nextInt(oldSolution.numberOfItems);
			} while (item1 == item2);

			int knapsack1 = 0;
			int knapsack2 = 0;

			for (int i = 0; i < oldSolution.numberOfKnapsacks; i++) {
				if (assignments[i][item1] == 1) {
					knapsack1 = i;
				}
			}

			for (int i = 0; i < oldSolution.numberOfKnapsacks; i++) {
				if (assignments[i][item2] == 1) {
					knapsack2 = i;
					break;
				}
			}

			int bagUsage1 = Arrays.stream(assignments[knapsack1]).sum();
			int bagUsage2 = Arrays.stream(assignments[knapsack2]).sum();

			assignments[knapsack1][item1] = 0;
			assignments[knapsack2][item2] = 0;
			assignments[knapsack1][item2] = 1;
			assignments[knapsack2][item1] = 1;

			retry = false;

			if (bagUsage1 > oldSolution.capacities[knapsack1] || bagUsage2 > oldSolution.capacities[knapsack2]) {
				assignments[knapsack1][item2] = 0;
				assignments[knapsack2][item1] = 0;
				assignments[knapsack1][item1] = 1;
				assignments[knapsack2][item2] = 1;

				retry = true;
			}
		}while (retry);

		return assignments;
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
		printMatrix(assignments);
		System.out.println();
		System.out.printf("Value: %f\n", value);
	}

	private void printMatrix(int[][] matrix) {
		for (int i = 0; i < numberOfKnapsacks; i++) {
			for (int j = 0; j < numberOfItems; j++) {
				System.out.printf("%2d ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public Solution findSolutionFromNeighbourhood() {
		return new GeneralizedAssignmentProblemSolution(this);
	}

	@Override
	public Solution newInstance() {
		return new GeneralizedAssignmentProblemSolution(numberOfItems, numberOfKnapsacks, profits, weights, capacities, assignments, value);
	}
}
