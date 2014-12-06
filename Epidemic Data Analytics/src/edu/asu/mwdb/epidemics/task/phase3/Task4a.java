package edu.asu.mwdb.epidemics.task.phase3;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import edu.asu.mwdb.epidemics.task.phase2.Task1;
//import edu.asu.mwdb.epidemics.uitlity.PreProcessingKNN;
import edu.asu.mwdb.epidemics.utility.FileMoverUtility;

public class Task4a {

	private static LinkedHashMap<String,String> labelMap;
	private static int similarityMeasure;
	private static int KNN;
	private static String knownLabeledFiles;
	private static String unknownLabeledFiles;
	private static HashSet<String> labelSet;
	private static String directoryPath;
	public static void main(String[] args) {
		//Similarity Types
		/* 
		 * Work on Epidemic File Directly
		 * Euclidean - WOuld be used for this task
		 * DTW
		 * 
		 * Work on Epidemic Word File
		 * WordSimilarity
		 * AverageWordSimilarity
		 * DifferenceWordSimilarity
		 * WeightedWordSimilarity
		 * WeighetdAvgWordSimilarity
		 * WeightedDiffWordSimilarity
		 * */

		//Running Instructions
		/* 
		 * Take the same inputs (window size, alpha, shift length, resolution) for both these pre-req steps
		 * 10 5 4 and alpha = 1.
		 * The following steps is for already known labeled files i.e. 1-26 .csv
		 * Run task 1 phase 1 and phase 2 for the epidemic file set and store the word file,
		 * average file and difference file in Simulation dictionary
		 * 
		 * The following steps are for unknown labeled files 27-120 .csv
		 * Run task 1, phase 1 and phase 2 for the epidemic file set and store the word file, 
		 * average file and difference file in query dictionary 
		 * */

		boolean wrongArg = false;
		if(args.length != 3)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task4a <Directory Path of Files>"
					+ "<Directory path of lables i.e. labels.csv> "
					+ "<K>");
			System.exit(0);
		}
		//Similarity measure and other parameters
		KNN = Integer.parseInt(args[2]);
		knownLabeledFiles = "KnownLabeledFile";
		unknownLabeledFiles = "UnknownLabeledFile";
		directoryPath = args[0];
		File dirPath = new File(directoryPath);
		//emptyDirectories(dirPath);

		//Read the label.csv file and save it in a map.
		labelMap = new LinkedHashMap<String,String>();
		labelSet = new HashSet<String>();
		String labelsFile = args[1];
		Task4a task4a = new Task4a();
		
		//PreProcessingKNN preprocessing = new PreProcessingKNN();
		try {
			FileMoverUtility fileMover = new FileMoverUtility(dirPath, knownLabeledFiles, unknownLabeledFiles);
			fileMover.moveFiles(labelsFile, labelMap, labelSet);
			//Pre-Processing Steps
			//LinkedHashMap<String, Float> euclideanDist = preprocessing.similarityMeasure(labelMap, knownLabeledFiles, 1);
			//LinkedHashMap<String, Float> aMatrixDist = preprocessing.similarityMeasure(labelMap, knownLabeledFiles, 6);
			similarityMeasure = 1;
			task4a.driverProgramTask4a();
		} catch (Exception e) {
			System.out.println("Exception : ");
			e.printStackTrace();
		}
	}

	public void driverProgramTask4a() throws Exception{
		Task1 task1 = new Task1();
		File knownLabelDirectory = new File(knownLabeledFiles);
		File[] knownFiles = knownLabelDirectory.listFiles();
		File unknownLableDIrectory = new File(unknownLabeledFiles);
		File[] unknownFiles = unknownLableDIrectory.listFiles();

		LinkedHashMap<String, LinkedHashMap<String, Float>> resultMap = new LinkedHashMap<String, LinkedHashMap<String, Float>>();
		/*	Take one file from unknown label directory and compute distance 
		 *	between known labeled file. Sort it and assign according to majority in top k similar files
		 *	Repeat the same for rest of the unknown label directory
		 */			
		for(File uFile : unknownFiles){
			LinkedHashMap<String, Float> scoreofFile = new LinkedHashMap<String, Float>();
			for(File kFile: knownFiles){
				scoreofFile.put(kFile.getName(), 
						task1.driverProgramforTask1(kFile.getAbsolutePath(),uFile.getAbsolutePath(), similarityMeasure));
			}				
			resultMap.put(uFile.getName(), sortByValue(scoreofFile,KNN));
		}			
		//Label Map to find the majority label.
		LinkedHashMap<String, Integer> label = new LinkedHashMap<String, Integer>();
		//Displaying the result
		File resultFile = new File("Result.csv");
		FileUtils.writeStringToFile(resultFile, "Files,Label \n", true);
		for(Map.Entry<String, LinkedHashMap<String,Float>> entry: resultMap.entrySet()){
			System.out.println("File : \t" + entry.getKey());
			for(Map.Entry<String, Float> scoreEntry: entry.getValue().entrySet()){
				System.out.println("Similar File(s) : " +scoreEntry.getKey() + "\tScore : "+scoreEntry.getValue());
				if(label.containsKey(labelMap.get(scoreEntry.getKey()))){
					int count = label.get(labelMap.get(scoreEntry.getKey()));
					count++;
					label.put(labelMap.get(scoreEntry.getKey()), count);
				}
				else {
					label.put(labelMap.get(scoreEntry.getKey()),1);
				}
			}
			LinkedHashMap<String, Integer> temp = sortByValue(label, 1);
			for(Map.Entry<String, Integer> labMap : temp.entrySet()){
				System.out.println("Label : " + labMap.getKey());
				//Writing into a file.
				String line = entry.getKey() + "," + labMap.getKey() + "\n";
				FileUtils.writeStringToFile(resultFile, line, true);
				break;
			}
			label = new LinkedHashMap<String, Integer>();
		}		
	}

	public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> 
	sortByValue( Map<K, V> map , int knn)
	{
		List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet());
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
				{
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return -(o1.getValue()).compareTo( o2.getValue());
			}
				} );

		LinkedHashMap<K, V> result = new LinkedHashMap<>();
		int count = 0;
		for (Map.Entry<K, V> entry : list)
		{
			if(count < knn){
				result.put( entry.getKey(), entry.getValue() );
				count++;
			}
		}
		return result;
	}
}
