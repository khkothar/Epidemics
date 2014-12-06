package edu.asu.mwdb.epidemics.task.phase3;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import edu.asu.mwdb.epidemics.domain.Directory;
import edu.asu.mwdb.epidemics.domain.Resolution;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.lsh.GlobalBitVector;
import edu.asu.mwdb.epidemics.lsh.QueryGlobalBitVector;
import edu.asu.mwdb.epidemics.read.EpidemicSimulationFileReaderThread;
import edu.asu.mwdb.epidemics.utility.FileMoverUtility;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;
import edu.asu.mwdb.epidemics.write.csv.Dictionary;
import edu.asu.mwdb.epidemics.write.csv.DictionaryWriter;

public class Task4b {

	private static Map<String, int[]> knownCountVectorMap;
	private static Map<String,int[]> unknownCountVectorMap;
	private static String knownLabeledFiles;
	private static String unknownLabeledFiles;
	private static HashSet<String> labelSet;
	private static LinkedHashMap<String,String> labelMap;
	private static String epidemicKnownLabelWordFile;
	private static String epidemicUnknownLabelWordFile;

	public static void main(String[] args) {		
		try {
			preProcessing(args);
			computeObjectFeatureMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void preProcessing(String[] args) throws Exception{
		//Read the label.csv file and save it in a map.
		labelMap = new LinkedHashMap<String,String>();
		labelSet = new HashSet<String>();
		//Assigning Constants
		knownLabeledFiles = "KnownLabeledFile";
		unknownLabeledFiles = "UnknownLabeledFile";
		epidemicKnownLabelWordFile = "EpidemicInputWordFile";
		epidemicUnknownLabelWordFile = "EpidemicQueryWordFile";

		File dirPath = new File(args[0]);
		String labelsFile = args[1];
		FileMoverUtility fileMover = new FileMoverUtility(dirPath,knownLabeledFiles,unknownLabeledFiles);
		fileMover.moveFiles(labelsFile, labelMap, labelSet);
		//Calling the Phase1 - Task1 Internally for Phase3 for KnownLabelebFiles and unKnownLabeledFiles Separately
		runPhase1Task1Internally(args, true);
		runPhase1Task1Internally(args, false);
	}

	private static void runPhase1Task1Internally(String args[], boolean knownLabel) throws IOException,
	InterruptedException, MatlabConnectionException,
	MatlabInvocationException {
		if (args.length >= 4) {
			String folderDirectory = "";
			String outputDirectory = "";
			if(knownLabel){
				folderDirectory = knownLabeledFiles + "\\";
				outputDirectory = epidemicKnownLabelWordFile + "\\";
			}
			else {
				folderDirectory = unknownLabeledFiles + "\\";
				outputDirectory = epidemicUnknownLabelWordFile +  "\\";
			}
			String directory = folderDirectory;
			int windowLength = Integer.parseInt(args[2]);
			int shiftLength = Integer.parseInt(args[3]);
			int levels = Integer.parseInt(args[4]);

			int numberOfCores = Runtime.getRuntime().availableProcessors();
			Resolution resolution = new Resolution(levels);
			BufferPool bufferPool = new BufferPool();
			Directory dir = new Directory(directory);
			Dictionary dictionary = new Dictionary(outputDirectory + "epidemic_word_file.csv");

			//numberOfCores = 2;
			Thread threads[] = new Thread[numberOfCores - 1];

			for (int i = 0; i < numberOfCores - 1; i++) {
				threads[i] = new Thread(new EpidemicSimulationFileReaderThread(
						dir, resolution, bufferPool, windowLength, shiftLength));
				threads[i].start();
			}

			Thread dictionaryWriter = new Thread(new DictionaryWriter(
					bufferPool, dictionary));
			dictionaryWriter.start();

			for (int i = 0; i < numberOfCores - 1; i++) {
				threads[i].join();
			}

			bufferPool.setFinished(true);

		}
	}

	@SuppressWarnings("unused")
	public static void computeObjectFeatureMatrix() throws Exception{
		//Call the GlobalBitVector for the KnownLabeldFiles and UnknownLabeledFiles
		knownCountVectorMap = new LinkedHashMap<String, int[]>();
		unknownCountVectorMap = new LinkedHashMap<String, int[]>();
		LinkedHashMap<Integer,Float> avgWordMap = new LinkedHashMap<Integer, Float>();
		GlobalBitVector globalbitKnownLabels = new GlobalBitVector(epidemicKnownLabelWordFile + "\\" + "epidemic_word_file.csv");
		knownCountVectorMap = globalbitKnownLabels.getCountVectorMapForInputFiles();
		//Populate the Word with averages
		LinkedHashSet<Window> tempWindow = (LinkedHashSet<Window>) globalbitKnownLabels.getUniqueWindows();
		int count =0;
		for(Window w : tempWindow){			
			avgWordMap.put(count, (float)0);
			count++;
		}
		count = 0;
		System.out.println("Before Known :");
		Map<String, int[]> tempKnownCount= new LinkedHashMap<String, int[]>(); 
		tempKnownCount =  knownCountVectorMap;
		for(Map.Entry<String, int[]> entry: tempKnownCount.entrySet()){
			int[] arr = entry.getValue();
			System.out.print("File : " + entry.getKey() + "\t");
			for(int i = 0; i<arr.length; i++){
				System.out.print(arr[i] + "\t");
				if(avgWordMap.containsKey(i)){
					float sum =  avgWordMap.get(i);
					avgWordMap.put(i, sum + arr[i]);
				}
			}
			System.out.println("");
		}
		
		//Setting the average values of the file.
		System.out.println("\n\n Avg Word Values");
		for(Map.Entry<Integer, Float> entry: avgWordMap.entrySet()){
			float ftemp = entry.getValue()/tempKnownCount.size();
			avgWordMap.put(entry.getKey(), ftemp);
			System.out.print(""+entry.getKey()+ "\t");
		}
		System.out.println("");
		for(Map.Entry<Integer, Float> entry: avgWordMap.entrySet()){
			System.out.print(""+entry.getValue() + "\t");
		}

		//Setting the KnownCountVectorMap with Averages
		for(Map.Entry<String, int[]> entry: knownCountVectorMap.entrySet()){
			int arr[] = entry.getValue();
			for(int i=0;i<arr.length;i++){				
				if(arr[i] >= avgWordMap.get(i)){
					arr[i] = 1;
				}
				else arr[i] = 0;
			}
			knownCountVectorMap.put(entry.getKey(), arr);
		}
		//Printing Result:
		System.out.println("\n\n After Known:\n\n\n");
		for(Map.Entry<String, int[]> entry: knownCountVectorMap.entrySet()){
			System.out.print("File : "+ entry.getKey() + "\t");
			int arr[] = entry.getValue();
			for(int i=0;i<arr.length;i++){				
				System.out.print(arr[i] + "\t");
			}
			System.out.println("");
		}

		//Get the files present in UnknownLabeledFiles and obtain the map.
		QueryGlobalBitVector globalUnknownLabels = new QueryGlobalBitVector(globalbitKnownLabels.getUniqueWindows(),epidemicUnknownLabelWordFile + "\\epidemic_word_file.csv");
		File unknownFileDirectory = new File(unknownLabeledFiles+"\\");
		for(File file: unknownFileDirectory.listFiles()){
			unknownCountVectorMap.put(file.getName(), globalUnknownLabels.getCountVectorForQueryFile());
		}

		System.out.println("\n\n\n Unkown Before :");
		for(Map.Entry<String, int[]> entry: unknownCountVectorMap.entrySet()){
			int arr[] = entry.getValue();
			System.out.print("File : " + entry.getKey()+ "\t");
			for(int i=0;i<arr.length;i++){
				System.out.print(arr[i] + "\t");
			}
			System.out.println("");
		}
		
		for(Map.Entry<String, int[]> entry: unknownCountVectorMap.entrySet()){
			int arr[] = entry.getValue();
			for(int i=0;i<arr.length;i++){
				if(arr[i] >= avgWordMap.get(i)){
					arr[i] = 1;
				}
				else arr[i] = 0;
			}
			unknownCountVectorMap.put(entry.getKey(), arr);
		}

		//Printing Result:
		System.out.println("\n\n After Unknown:\n\n\n");
		for(Map.Entry<String, int[]> entry: unknownCountVectorMap.entrySet()){
			System.out.print("File : "+ entry.getKey() + "\t");
			int arr[] = entry.getValue();
			for(int i=0;i<arr.length;i++){				
				System.out.print(arr[i] + "\t");
			}
			System.out.println("");
		}
	}
}
