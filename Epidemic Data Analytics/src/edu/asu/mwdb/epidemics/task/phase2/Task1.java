package edu.asu.mwdb.epidemics.task.phase2;

import java.io.File;
import java.text.DecimalFormat;

import edu.asu.mwdb.epidemics.similarity.AverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.DifferenceWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;
import edu.asu.mwdb.epidemics.similarity.WeightedAverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedDiffWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WordSimilarity;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class Task1 {
	public static void main(String args[]){
		try {			
			boolean wrongArg = false;
			if(args.length != 3)
				wrongArg = true;
			if(wrongArg){
				System.err.println("Please enter correct command line arguments:");
				System.err.println("Usage: Task1 <File 1 Path> <File 2 Path)> <The Similarity Measure(Enter 1 to 8 for tasks a-h)>");
				System.exit(0);
			}
			String file1 = args[0];
			String file2 = args[1];
			int similarityMeasure = Integer.parseInt(args[2]);
			long startTime = System.currentTimeMillis();
			System.out.println("Executing Task 1...");
			Task1 task1 = new Task1();
			task1.driverProgramforTask1(file1, file2, similarityMeasure);
			long endTime = System.currentTimeMillis();
			System.out.println("Task1 : Time taken by SImilarity type: "+ SimilarityMeasureUtils.getSimilarityMeasure(similarityMeasure) + " is : "+ (endTime - startTime) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void driverProgramforTask1(String file1, String file2, int simMeasure) throws Exception{
		Similarity similarity = null;		
		String similarityMeasure = "";
		switch(simMeasure) {
		case 1: 
			similarity = new EuclideanSimilarity();
			similarityMeasure = "Euclidean";
			break;
		case 2:
			similarity = new DTWSimilarity();
			similarityMeasure = "DTW";
			break;
		case 3: 
			similarity = new WordSimilarity();
			similarityMeasure = "Word";
			break;
		case 4:
			similarity = new AverageWordSimilarity();
			similarityMeasure = "Average Word";
			break;
		case 5: 
			similarity = new DifferenceWordSimilarity();
			similarityMeasure = "Difference Word";
			break;
		case 6:
			similarity = new WeightedWordSimilarity();
			similarityMeasure = "Weighted Word";
			break;
		case 7: 
			similarity = new WeightedAverageWordSimilarity();
			similarityMeasure = "Weighted Average Word";
			break;
		case 8:
			similarity = new WeightedDiffWordSimilarity();
			similarityMeasure = "Weighted Difference Word";
			break;
		}
		float similarityScore  = similarity.getScore(file1,file2);
		DecimalFormat df = new DecimalFormat("#.#########");
		System.out.println(similarityMeasure + " Similarity between " +
		getFileName(file1) + " and " +getFileName(file2) +" : "+ df.format(similarityScore));
	}
	
	//Get Files names from Absolute paths.
	private String getFileName(String inputfile) throws Exception{
		File file = new File(inputfile);
		String absolutePath = file.getAbsolutePath();
		String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separator)+1, absolutePath.length());
		return fileName;
	}
}
