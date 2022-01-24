package edu.umg.generalized_assignment_problem;

public interface Solution {
	double getValue();

	Solution findSolutionFromNeighbourhood();

	Solution newInstance();
}
