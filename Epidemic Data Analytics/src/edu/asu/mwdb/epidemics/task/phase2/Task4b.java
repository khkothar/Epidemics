package edu.asu.mwdb.epidemics.task.phase2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.asu.mwdb.epidemics.fastmap.FastMap;
import edu.asu.mwdb.epidemics.time_series_search.DrawHeatMap;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class Task4b {
	public static void main(String args[]) throws Exception{
		boolean wrongArg = false;
		if(args.length != 5)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task4b <Simulation files path)><similarity measure(enter 1 to 8 for tasks a-h)> <k> <Query file> <r>");
			System.exit(0);
		}
		String simFilesPath = args[0];
		int simMeasureType = Integer.parseInt(args[1]);
		int r = Integer.parseInt(args[2]);
		int k = Integer.parseInt(args[4]);
		File queryFile = new File(args[3]);
		List<String> files = Arrays.asList(new File(simFilesPath).list());
		List<String> filesWithPath = new ArrayList<String>();
		for(int i = 0; i < files.size(); i++){
			filesWithPath.add(simFilesPath + "\\" + files.get(i));
		}
		long startTime = System.currentTimeMillis();
		System.out.println("Executing Task 4b...");
		System.out.println("Projecting...");
		FastMap fastMap = new FastMap(filesWithPath, SimilarityMeasureUtils.getSimilarity(SimilarityMeasureUtils.getSimilarityMeasure(simMeasureType)), r);
		System.out.println("Getting k-similar simulations...");
		List<String> similarSimulationFiles = fastMap.getTopKSimilarFiles(queryFile.getName(), k);
		System.out.println("Plotting heatMaps...");
		DrawHeatMap.drawHeatMap(new File(queryFile.getName()));
		for(String file : similarSimulationFiles){
			DrawHeatMap.drawHeatMap(new File(file));
		}
		System.out.println("HeatMaps plotted");
		long endTime = System.currentTimeMillis();
		System.out.println("Fastmap quering : Time taken by Similarity type: "+ SimilarityMeasureUtils.getSimilarityMeasure(simMeasureType) + " is : "+ (endTime - startTime));
		
	}
}
