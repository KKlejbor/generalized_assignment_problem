package edu.umg.generalized_assignment_problem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
	private static final int[] optimalSolutions = new int[] {336, 434, 580, 656, 563, 761, 942, 1133, 709, 958, 1139, 1451};

	public static void main(String[] args) throws IOException {
		File resultsDirectory = new File("results/");

		if (!resultsDirectory.exists()) {
			resultsDirectory.mkdir();
		}

		PrintWriter resultsCSV = new PrintWriter(new FileWriter("results/results.csv"));
		resultsCSV.println("File name;Optimal solution;Iteration number;Alpha;Initial temperature;Best found solution;Relative error");

		for (int i = 0; i < optimalSolutions.length; i++) {
			for (int j = 0; j < 10; j++) {
				SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(1,5,100,0.95,"testData/gap"+(i + 1)+".txt");
				simulatedAnnealing.printSolution();
				simulatedAnnealing.solve();
				simulatedAnnealing.printSolution();
				GeneralizedAssignmentProblemSolution bestSolution = simulatedAnnealing.getBestSolution();
				System.out.println();
				System.out.println();
				System.out.println();
				resultsCSV.println(String.format("gap%d.txt;%d;%d;%.2f;%d;%.2f;%.2f", i+1,optimalSolutions[i],j+1, 0.95, 100, bestSolution.value(), (Math.abs(optimalSolutions[i] - bestSolution.value())/optimalSolutions[i])*100));
			}
		}

		resultsCSV.close();
	}
}
