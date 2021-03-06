package edu.asu.mwdb.epidemics.task.phase2;

import java.io.File;
import java.util.List;

import edu.asu.mwdb.epidemics.time_series_search.DrawHeatMap;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;
import edu.asu.mwdb.epidemics.time_series_search.TimeSeriesSearch;

public class Task2 {
	public static void main(String args[]) throws Exception{
		boolean wrongArg = false;
		if(args.length != 4)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task2 <Query File Path> <Simulation files path)> <value of k> <tne similarity measure(enter 1 to 8 for tasks a-h)>");
			System.exit(0);
		}
		TimeSeriesSearch timeSeriesSearch = new TimeSeriesSearch();
		String queryFile = args[0];
		String simFilesPath = args[1];
		int k = Integer.parseInt(args[2]);
		int similarityType = Integer.parseInt(args[3]);
		System.out.println("Executing Task2...");
		long startTime = System.currentTimeMillis();
		System.out.println(k + " most Similar simulations to " + queryFile.substring(queryFile.lastIndexOf(File.separator)+1, queryFile.length())
				+ " Using " + SimilarityMeasureUtils.getSimilarityMeasure(similarityType) + " Similarity Measure");
		System.out.println("Getting k most-similar simulations...");
		List<String> similarSimulationFiles = timeSeriesSearch.getKSimilarSimulations(queryFile, simFilesPath, k, SimilarityMeasureUtils.getSimilarityMeasure(similarityType));
		System.out.println("Plotting heatmaps...");
		DrawHeatMap.drawHeatMap(new File(queryFile));
		for(String file : similarSimulationFiles){
			String path = simFilesPath + "\\" + file;
			DrawHeatMap.drawHeatMap(new File(path));
		}
		System.out.println("Heatmaps Plotted");
		long endTime = System.currentTimeMillis();
		System.out.println("Task 2 : Time taken by Similarity type: "+ SimilarityMeasureUtils.getSimilarityMeasure(similarityType) + " is : "+ (endTime - startTime)+ " ms");
	}
}
