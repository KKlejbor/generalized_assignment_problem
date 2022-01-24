package edu.umg.generalized_assignment_problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GeneralizedAssignmentProblemSolution implements Solution{
	private final int numberOfItems;
	private final int numberOfKnapsacks;
	private final int[][] profits;
	private final int[][] weights;
	private final int[] capacities;
	private final int[][] assignments;

	public GeneralizedAssignmentProblemSolution(String fileName) {
		int numberOfItems = 0;
		int numberOfKnapsacks = 0;
		int[][] profits = null;
		int[][] weights = null;
		int[] capacities = null;
		int[][] assignments = null;

		try (Scanner scanner = new Scanner(new File(fileName))) {
			scanner.next();

			numberOfKnapsacks = Integer.parseInt(scanner.next());
			numberOfItems = Integer.parseInt(scanner.next());

			profits = new int[numberOfKnapsacks][numberOfItems];
			weights = new int[numberOfKnapsacks][numberOfItems];
			capacities = new int[numberOfKnapsacks];
			assignments = new int[numberOfKnapsacks][numberOfItems];

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			this.numberOfItems = numberOfItems;
			this.numberOfKnapsacks = numberOfKnapsacks;
			this.profits = profits;
			this.weights = weights;
			this.capacities = capacities;
			this.assignments = assignments;
		}

	}

	@Override
	public double getValue() {
		return 0;
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
