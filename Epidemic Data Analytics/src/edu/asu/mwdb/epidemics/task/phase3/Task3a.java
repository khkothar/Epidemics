package edu.asu.mwdb.epidemics.task.phase3;

import edu.asu.mwdb.epidemics.graph_analysis.SimilarityGraphUtilities;

public class Task3a {
	public static void main(String args[]) throws Exception{
		boolean wrongArg = false;
		if(args.length != 2)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task3a <Threshold> <Input File path)>");
			System.exit(0);
		}
		System.out.println("Creating similarity graph...");
		float threshold = Float.parseFloat(args[0]);
		SimilarityGraphUtilities simObj = new SimilarityGraphUtilities(args[1]);
		simObj.createSimilarityGraph(threshold);
		System.out.println("\nsimGraph.csv graph file created.");
	}
}
