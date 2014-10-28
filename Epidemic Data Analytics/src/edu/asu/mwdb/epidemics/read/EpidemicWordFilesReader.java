/**
 * Mohan:
 * File for P2T1A
 * Aim: Finding similarity between two Epidemic simulation file using Euclidean Distance
 */
package edu.asu.mwdb.epidemics.read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpidemicWordFilesReader {
	
	private static List<String> stateList;
	
	public double getSimilarityByEuclideanDistance(String inputFile1, String inputFile2) throws IOException { 
		double[] similarityArray = new double[51];
		double partialSimilarity = 0.0;
		String line1, line2;
		int i;
		String[] inputArray1, inputArray2;
		BufferedReader bufReader1 = new BufferedReader(new FileReader(inputFile1));
		BufferedReader bufReader2 = new BufferedReader(new FileReader(inputFile2));
		
		bufReader1.readLine();
		bufReader2.readLine();
		while((line1 = bufReader1.readLine()) != null && (line2 = bufReader2.readLine()) != null) {
			inputArray1 = line1.split(",");
			inputArray2 = line2.split(",");
			for(i=2; i<inputArray1.length && i<inputArray2.length ; i++) {
				similarityArray[i-2] += calculateDifferenceOfSqaures(inputArray1[i], inputArray2[i]);
			}
		}
		for (int j = 0; j < similarityArray.length; j++) {
			partialSimilarity += Math.sqrt(similarityArray[j]);
		}
		bufReader1.close();
		bufReader2.close();
		
		return 1/(1+partialSimilarity/51);
	}
	
	public double calculateDifferenceOfSqaures(String value1, String value2) {
		double in1 = Double.parseDouble(value1);
		double in2 = Double.parseDouble(value2);
		
		return Math.abs(in1*in1 - in2*in2);
	}
	
	public double getSimilarityByDTW(String inputFile1, String inputFile2) throws Exception {
			
		Map<String,List<Double>> simulationMapFile1 = getSimulationFileMap(inputFile1);
		Map<String,List<Double>> simulationMapFile2 = getSimulationFileMap(inputFile2);
		double cumulativeDTW = 0,avgDTWDistance;
		
		//Allocating matrix for calculating DTW for each state.
		double[][] DTWMatrix;
		double[][] LambdaDTW;
		List<Double> stateValueList1, stateValueList2;
		
		//Throwing exception if number of states in first and second simulation file are different.
		if(simulationMapFile1.size() != simulationMapFile2.size()) {
			throw new Exception("Maps do not contain same number of states.");
		}
		
		//Calculating the DTW between two input simulation files.
		for(String state : stateList) {
			stateValueList1 = simulationMapFile1.get(state);
			stateValueList2 = simulationMapFile2.get(state);
			
			DTWMatrix = new double[stateValueList1.size()][stateValueList2.size()];
			LambdaDTW = new double[stateValueList1.size()][stateValueList2.size()];
	
			//Calculating the matrix W matrix
			for(int i = 0 ; i < stateValueList1.size() ; i++) {
				for(int j = 0 ; j < stateValueList2.size() ; j++) {
					DTWMatrix[i][j] = Math.pow((stateValueList1.get(i) - stateValueList2.get(j)),2);
					//Copying the first row and first colume in the LambdaMatrix from DTWMatrix.
					if(i == 0) {
						LambdaDTW[0][j] = DTWMatrix[0][j];
					}
					if(j == 0) {
						LambdaDTW[i][0] = DTWMatrix[i][0];
					}
				}
			}
			
			//Calculating lambda matrix.
			//For start all the values in first row and first colume are considered zero.
			for(int i = 1 ; i < DTWMatrix.length ; ++i) {
				for(int j = 1 ; j < DTWMatrix[0].length ; ++j) {
					LambdaDTW[i][j] = DTWMatrix[i][j] + Math.min(LambdaDTW[i-1][j-1], Math.min(LambdaDTW[i-1][j], LambdaDTW[i][j-1]));
				}
			}
			
			//Adding the last element in DTW array in the cumulative DTW distance.
			cumulativeDTW += Math.sqrt(LambdaDTW[simulationMapFile1.size() - 1][simulationMapFile2.size() - 1]);
			
		}
		avgDTWDistance = (double)cumulativeDTW/stateList.size();
		return (double)1/(1+avgDTWDistance);
	}
	

	public Map<String,List<Double>> getSimulationFileMap(String inputFile) throws Exception {
		String line,stateName;
		String[] lineArray;
		List<Double> valueList;
		
		List<String> stateNameArrayList = new ArrayList<>();
		
		Map<String,List<Double>> simulationFileMap = new HashMap<String, List<Double>>();
		BufferedReader bufReader = new BufferedReader(new FileReader(inputFile));
		
		//Creating Map containing State Name as key and empty Arraylist as value.
		line = bufReader.readLine();
		lineArray = line.split(",");
		for(int i=2; i<lineArray.length; ++i) {
			simulationFileMap.put(lineArray[i], new ArrayList<Double>());
			stateNameArrayList.add(lineArray[i]);
		}
		
		stateList = stateNameArrayList;
		
		while((line = bufReader.readLine()) != null) {
			lineArray = line.split(",");
			for (int i = 2; i < lineArray.length; ++i) {
				stateName = stateNameArrayList.get(i - 2);
				if(simulationFileMap.containsKey(stateName)) {
					valueList = simulationFileMap.get(stateName);
					valueList.add(Double.parseDouble(lineArray[i]));
					simulationFileMap.put(stateName,valueList);
				} else {
					bufReader.close();
					throw new Exception("Error while creating simulation file map : Statename not found!!!");
				}
			}
		}
		
		bufReader.close();
		return simulationFileMap;
	}
	
	public static void main(String[] args) throws Exception{
		
		EpidemicWordFilesReader t = new EpidemicWordFilesReader();
//		double similarity = t.getSimilarityByEuclideanDistance("InputCSVs/1.csv", "InputCSVs/2.csv");
//		System.out.println("Print the similarity : " + similarity);
		
		double similarity = t.getSimilarityByDTW("InputCSVs/1.csv", "InputCSVs/2.csv");
		System.out.println("Print DTW similarity : " + similarity);
		
	}

}
