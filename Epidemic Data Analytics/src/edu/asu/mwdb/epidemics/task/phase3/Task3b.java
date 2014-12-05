package edu.asu.mwdb.epidemics.task.phase3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.mwdb.epidemics.graph_analysis.SimilarityGraphUtilities;

public class Task3b {
	
	public static void main(String[] args) throws IOException {
		boolean wrongArg = false;
		if(args.length != 5)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task3b <Similarity Graph file> <k> <Input File path) (alpha)>");
			System.exit(0);
		}
		String simGraph = args[0];
		float alpha = Float.parseFloat(args[3]);
		float convergenceFactor = Float.parseFloat(args[4]);
		int k = Integer.parseInt(args[1]);
		List<String> dominantNodes = new ArrayList<String>();
		SimilarityGraphUtilities simObj = new SimilarityGraphUtilities(args[2]);
		dominantNodes = simObj.getKDominantNodes(simGraph, k, alpha, convergenceFactor);
		System.out.println(k + " Dominant nodes are :");
		for(int i = 0; i < dominantNodes.size(); i++)
			System.out.println(dominantNodes.get(i));
	}
}
