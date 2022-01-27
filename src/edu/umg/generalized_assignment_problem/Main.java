package edu.umg.generalized_assignment_problem;

public class Main {
	public static void main(String[] args) {
		GeneralizedAssignmentProblemSolution GAPS = new GeneralizedAssignmentProblemSolution("testData/gap1.txt");
		GAPS.printSolution();
		SimulatedAnnealing SA = new SimulatedAnnealing(1000,10,100,0.95,GAPS);
		SA.solve();
		GAPS = (GeneralizedAssignmentProblemSolution) SA.getCurrentSolution();
		System.out.println();
		System.out.println();
		System.out.println();
		GAPS.printSolution();
	}
}
