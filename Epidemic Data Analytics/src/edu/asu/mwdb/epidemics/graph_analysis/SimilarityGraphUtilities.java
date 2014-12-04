package edu.asu.mwdb.epidemics.graph_analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class SimilarityGraphUtilities {
	int dimension;
	File[] listOfFiles;
	int divFactor;
	
	public SimilarityGraphUtilities(String inputFilePath){
		File folder = new File(inputFilePath);
		this.listOfFiles = folder.listFiles();
		this.dimension = folder.listFiles().length;
	}
	public void createSimilarityGraph(float threshold) throws Exception{
		//DecimalFormat df = new DecimalFormat("#.#########");
		float simMatrix[][] = new float[dimension][dimension];
		File simMatrixCSV = new File("simGraph.csv");
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(simMatrixCSV));
		buffWriter.write("files,");
		for(int i = 0; i < listOfFiles.length; i++){
			buffWriter.write(listOfFiles[i].getName());
			if(i < listOfFiles.length-1)
				buffWriter.write(",");
		}
		buffWriter.write("\n");
		for(int i = 0; i < dimension; i++){
			buffWriter.write(listOfFiles[i].getName()+ ",");
			for(int j = 0 ; j < dimension; j++){
				float sim = new EuclideanSimilarity().getScore(listOfFiles[i].getAbsolutePath(),listOfFiles[j].getAbsolutePath());
				if(sim > threshold)
					simMatrix[i][j] = 1;
				buffWriter.write(Float.toString(simMatrix[i][j]));
				if(j < dimension-1)
					buffWriter.write(",");				
			}
			buffWriter.write("\n");
		}
		
		buffWriter.close();
/*		for(int i = 0; i < listOfFiles.length; i++){
			System.out.println(listOfFiles[i].getName());
			System.out.println();
		}
*/		SimilarityMeasureUtils.printMatrix(simMatrix);
	}

	public List<String> getKDominantNodes(String simGraph, int k, float alpha) throws IOException {
		float[][] AMatrix = new float[dimension][dimension];

		AMatrix = getAmatrix(simGraph);
		float neighbourWalk[] = new float[dimension];
		float randomWalk[] = new float[dimension];
		for(int i = 0 ; i < dimension; i++){
			//System.out.println(1/dimension);
			randomWalk[i] = (float)1/dimension;
			neighbourWalk[i] = (float)1/dimension;
		}
		randomWalk = multiplyByFactor(1 - alpha, randomWalk);
		float temp[][] = new float[dimension][dimension];
		temp = multiplyByFactor(alpha, AMatrix);
		for(int i = 0 ; i < 5; i++){
			neighbourWalk = multiplyAllMatrices(temp, neighbourWalk, randomWalk);
			System.out.println("Iteration : "+i);
			SimilarityMeasureUtils.printArray(neighbourWalk);
		}
		System.out.println("Final Array!!!");
		SimilarityMeasureUtils.printArray(neighbourWalk);
		return null;
	}
	private float[] multiplyAllMatrices(float[][] temp,
			float[] neighbourWalk, float[] randomWalk) {
		float nbrWalk[] = new float[dimension];
		neighbourWalk = multiplyMatrx(temp, neighbourWalk);
		nbrWalk = addMatrx(neighbourWalk, randomWalk);
		return nbrWalk;
	}
	
	private float[] addMatrx(float[] nbrWalk, float[] rndmWalk) {
		float[] result = new float[dimension];
		for(int i = 0 ; i < dimension; i++){
			result[i] = nbrWalk[i] + rndmWalk[i];
		}
		return result;
	}
	private float[] multiplyMatrx(float[][] temp, float[] neighbourWalk) {
		float[] result = new float[dimension];
		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < dimension; j++){
                	result[i] += temp[i][j] * neighbourWalk[j];
			}
		}
		return result;
	}
	private float[] multiplyByFactor(float alpha, float[] aMatrix) {
		float[] result = new float[dimension];
			for(int j = 0; j < dimension; j++){
				result[j] = aMatrix[j] * alpha;
			}
		return result;
	}
	private float[][] multiplyByFactor(float alpha, float[][] aMatrix) {
		float[][] result = new float[dimension][dimension];
		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < dimension; j++){
				result[i][j] = aMatrix[i][j] * alpha;
			}
		}
		return result;
	}
	private float[][] getAmatrix(String simGraph) throws IOException {
		File simGraphFile = new File(simGraph);
		BufferedReader buffRdr = new BufferedReader(new FileReader(simGraphFile));
		String splitBy = ",";
		float result[][] = new float[dimension][dimension];
		//read header
		String[] line = buffRdr.readLine().split(splitBy);
		for(int i = 0; i < dimension; i++){
			line = buffRdr.readLine().split(splitBy);
			divFactor = getFactor(line);
			System.out.println("factor is "+ divFactor);
			for(int j = 0 ; j < dimension; j++){
				result[i][j] = Float.parseFloat(line[j+1])/divFactor;
			}
		}
		buffRdr.close();
		SimilarityMeasureUtils.printMatrix(result);
		return result;
	}
	private int getFactor(String[] line) {
		int factor = 0;
		for(int i = 0; i < dimension; i++){
			if(Float.parseFloat(line[i+1]) == 1)
				factor++;
		}
		return factor;
	}
}
