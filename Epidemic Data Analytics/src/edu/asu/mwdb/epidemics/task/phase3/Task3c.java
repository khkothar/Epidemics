/**
 * 
 */
package edu.asu.mwdb.epidemics.task.phase3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.mwdb.epidemics.graph_analysis.SimilarityGraphUtilities;

/**
 * @author Chandrashekhar
 *
 */
public class Task3c {

	public static void main(String[] args) throws IOException {
		boolean wrongArg = false;
		if(args.length != 6)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task3c <Similarity Graph file> <k> <Input File path) (alpha) (query file 1) (query file 2)>");
			System.exit(0);
		}
		String simGraph = args[0];
		float alpha = Float.parseFloat(args[3]);
		int k = Integer.parseInt(args[1]);
		List<String> dominantNodes = new ArrayList<String>();
		SimilarityGraphUtilities simObj = new SimilarityGraphUtilities(args[2]);
		System.out.println("Retrieving k most relevant files...");
		dominantNodes = simObj.getKRelevantFiles(simGraph, k, alpha, args[4], args[5]);
		System.out.println();
		System.out.println(k + " most relevant simulations are:");
		for(int i = 0; i < dominantNodes.size(); i++)
			System.out.println(dominantNodes.get(i));
	}
}
