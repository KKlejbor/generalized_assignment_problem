package edu.umg.generalized_assignment_problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

			assignments = generateRandomAssignmentsMatrix(numberOfKnapsacks, numberOfItems, weights, capacities);

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

	private int[][] generateRandomAssignmentsMatrix(int numberOfKnapsacks, int numberOfItems, int[][] weights, int[] capacities) {
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

		this.assignments = new int[numberOfKnapsacks][];

		for (int i = 0; i < numberOfKnapsacks; i++) {
			this.assignments[i] = Arrays.copyOf(oldSolution.assignments[i], numberOfItems);
		}

		this.value = oldSolution.value;
	}

	private double computeValue() {
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


//	private int[][] findAssignmentsMatrixFromNeighbourhood() {
//		int[][] assignments = new int[size][];
//
//		for (int i = 0; i < size; i++) {
//			assignments[i] = Arrays.copyOf(this.assignments[i], size);
//		}
//
//		//Random random = new Random();
//		int job1 = ThreadLocalRandom.current().nextInt(size);
//		int job2;
//
//		do {
//			job2 = ThreadLocalRandom.current().nextInt(size);
//		} while (job1 == job2);
//
//		int worker1 = 0;
//		int worker2 = 0;
//
//		for (int i = 0; i < assignments.length; i++) {
//			if (assignments[i][job1] == 1) {
//				worker1 = i;
//				break;
//			}
//		}
//
//		for (int i = 0; i < assignments.length; i++) {
//			if (assignments[i][job2] == 1) {
//				worker2 = i;
//				break;
//			}
//		}
//
//		assignments[worker1][job1] = 0;
//		assignments[worker2][job2] = 0;
//		assignments[worker1][job2] = 1;
//		assignments[worker2][job1] = 1;
//
//		return assignments;
//	}

	public void printSolution(){
		System.out.printf("Number of Knapsacks: %d\n", numberOfKnapsacks);
		System.out.printf("Number of Items: %d\n", numberOfItems);
		System.out.println("Profits: ");
		for (int i = 0; i < numberOfKnapsacks; i++) {
			for (int j = 0; j < numberOfItems; j++) {
				System.out.printf("%2d ",profits[i][j]);
			}
			System.out.println();
		}

		System.out.println("Weights: ");
		for (int i = 0; i < numberOfKnapsacks; i++) {
			for (int j = 0; j < numberOfItems; j++) {
				System.out.printf("%2d ",weights[i][j]);
			}
			System.out.println();
		}

		System.out.println("Capacities: ");
		for (int i = 0; i < numberOfKnapsacks; i++) {
			System.out.printf("%2d ",capacities[i]);
		}
		System.out.println();

		System.out.println("Assignments:");
		for (int i = 0; i < numberOfKnapsacks; i++) {
			for (int j = 0; j < numberOfItems; j++) {
				System.out.printf("%2d ",assignments[i][j]);
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
		return null;
	}

	@Override
	public Solution newInstance() {
		return null;
	}
}
