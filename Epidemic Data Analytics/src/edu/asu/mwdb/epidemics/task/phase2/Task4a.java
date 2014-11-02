package edu.asu.mwdb.epidemics.task.phase2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.mwdb.epidemics.fastmap.FastMap;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class Task4a {
	public static void main(String args[]) throws Exception{
		boolean wrongArg = false;
		if(args.length != 3)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task2 <Simulation files path)><similarity measure(enter 1 to 8 for tasks a-h)> <k>");
			System.exit(0);
		}
		String simFilesPath = args[0];
		int simMeasureType = Integer.parseInt(args[1]);
		int k = Integer.parseInt(args[2]);
		List<String> files = Arrays.asList(new File(simFilesPath).list());
		FastMap fastMap = new FastMap(files, SimilarityMeasureUtils.getSimilarity(SimilarityMeasureUtils.getSimilarityMeasure(simMeasureType)), k);
		
		/*
		 * temporary result print
		 */
		for(int i = 0; i < files.size(); i++){
			System.out.println();
			for(int j = 0; j < k; j++){
				System.out.println(fastMap.getReducedMatrix().getMatrix()[i][j]);
			}
		}
	}
}
