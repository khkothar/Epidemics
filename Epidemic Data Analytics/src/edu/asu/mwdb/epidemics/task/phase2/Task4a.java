package edu.asu.mwdb.epidemics.task.phase2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.mwdb.epidemics.fastmap.FastMap;
import edu.asu.mwdb.epidemics.time_series_search.DrawHeatMap;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class Task4a {
	public static void main(String args[]) throws Exception{
		boolean wrongArg = false;
		if(args.length != 3)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task4 <Simulation files path)><similarity measure(enter 1 to 8 for tasks a-h)> <r>");
			System.exit(0);
		}
		String simFilesPath = args[0];
		int simMeasureType = Integer.parseInt(args[1]);
		int r = Integer.parseInt(args[2]);
		List<String> files = Arrays.asList(new File(simFilesPath).list());
		List<String> filesWithPath = new ArrayList<String>();
		long startTime = System.currentTimeMillis();
		System.out.println("Executing Task 4a...");
		System.out.println("Projecting...");
		for(int i = 0; i < files.size(); i++){
			filesWithPath.add(simFilesPath + "\\" + files.get(i));
		}
		FastMap fastMap = new FastMap(filesWithPath, SimilarityMeasureUtils.getSimilarity(SimilarityMeasureUtils.getSimilarityMeasure(simMeasureType)), r);
		System.out.println("\nMapping Error between reduced space and original space : "+ fastMap.getError());
		long endTime = System.currentTimeMillis();
		System.out.println("Fastmap Mapping error : Time taken by Similarity type: "+ simMeasureType + " is : "+ (endTime - startTime)+ " ms");
		
	}
}
