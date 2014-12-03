package edu.asu.mwdb.epidemics.task.phase3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.mwdb.epidemics.graph_analysis.SimilarityGraphUtilities;

public class Task3b {
	
	public static void main(String[] args) throws IOException {
		String simGraph = args[0];
		float alpha = Float.parseFloat(args[3]);
		int k = Integer.parseInt(args[1]);
		List<String> dominantNodes = new ArrayList<String>();
		SimilarityGraphUtilities simObj = new SimilarityGraphUtilities(args[2]);
		dominantNodes = simObj.getKDominantNodes(simGraph, k, alpha);
	}

}
