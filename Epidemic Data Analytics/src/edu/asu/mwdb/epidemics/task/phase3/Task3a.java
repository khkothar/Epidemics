package edu.asu.mwdb.epidemics.task.phase3;

import edu.asu.mwdb.epidemics.graph_analysis.SimilarityGraphUtilities;

public class Task3a {
	public static void main(String args[]) throws Exception{
		
		float threshold = Float.parseFloat(args[0]);
		SimilarityGraphUtilities simObj = new SimilarityGraphUtilities(args[1]);
		simObj.createSimilarityGraph(threshold);
	}
}
