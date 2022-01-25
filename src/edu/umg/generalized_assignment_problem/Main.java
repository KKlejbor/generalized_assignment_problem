package edu.umg.generalized_assignment_problem;

public class Main {
	public static void main(String[] args) {
		GeneralizedAssignmentProblemSolution GAPS = new GeneralizedAssignmentProblemSolution("testData/gap1.txt");
		GAPS.printSolution();
		GAPS = new GeneralizedAssignmentProblemSolution("testData/gap12.txt");
		System.out.println();
		System.out.println();
		System.out.println();
		GAPS.printSolution();
	}
}
