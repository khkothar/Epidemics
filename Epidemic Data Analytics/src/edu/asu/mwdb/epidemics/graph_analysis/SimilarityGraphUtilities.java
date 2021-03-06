package edu.asu.mwdb.epidemics.graph_analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.task.phase3.DominantNodesWrapper;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class SimilarityGraphUtilities {
	int dimension;
	File[] listOfFiles;
	int divFactor;
	private Map<Integer, String> fileToIndexMap = new HashMap<Integer, String>();
	
	public SimilarityGraphUtilities(String inputFilePath){
		File folder = new File(inputFilePath);
		this.listOfFiles = folder.listFiles();
		this.dimension = folder.listFiles().length;
		for(int i = 0 ; i < listOfFiles.length; i++){
			getFileToIndexMap().put(i, listOfFiles[i].getName());
		}
	}
	public void createSimilarityGraph(float threshold) throws Exception{
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
				if(sim > threshold && i != j)
					simMatrix[i][j] = 1;
				buffWriter.write(Float.toString(simMatrix[i][j]));
				if(j < dimension-1)
					buffWriter.write(",");
			}
			buffWriter.write("\n");
		}
		buffWriter.close();
		SimilarityMeasureUtils.printMatrix(simMatrix);
	}

	public List<String> getKDominantNodes(String simGraph, int k, float alpha, float convergenceFactor) throws IOException {
		float[][] AMatrix = new float[dimension][dimension];
		List<String> dominantNodes = new ArrayList<String>();
		AMatrix = getAmatrix(simGraph);
		float neighbourWalk[] = new float[dimension];
		float randomWalk[] = new float[dimension];
		for(int i = 0 ; i < dimension; i++){
			randomWalk[i] = (float)1/dimension;
			neighbourWalk[i] = (float)1/dimension;
		}
		randomWalk = multiplyByFactor(1 - alpha, randomWalk);
		float temp[][] = new float[dimension][dimension];
		temp = multiplyByFactor(alpha, AMatrix);
		float[] prevUq = new float[dimension];
		int iteration = 0;
		while(true){
			prevUq = neighbourWalk;
			neighbourWalk = multiplyAllMatrices(temp, neighbourWalk, randomWalk);
			if(checkForConvergence(prevUq, neighbourWalk, convergenceFactor))
				break;
			iteration++;
		}
		System.out.println("\n Converged after "+ iteration + " iterations");
		System.out.println("\nFinal page rank!!!\n");
		SimilarityMeasureUtils.printArray(neighbourWalk, getFileToIndexMap());
		dominantNodes = retrieveDominantNodes(neighbourWalk, k, null, null);
		return dominantNodes;
	}

	private List<String> retrieveDominantNodes(float[] neighbourWalk, int k, String qFile1, String qFile2) {
		RankComparator comparator = new RankComparator();
		PriorityQueue<DominantNodesWrapper> priorityQue = new PriorityQueue<DominantNodesWrapper>(k, comparator);
		List<String> domNodes = new ArrayList<String>();
		for(int i = 0; i< neighbourWalk.length; i++){
			DominantNodesWrapper wrapper = new DominantNodesWrapper();
			wrapper.setIndex(i);
			wrapper.setRankValue(neighbourWalk[i]);
				if(qFile1 != null && (getFileToIndexMap().get(wrapper.getIndex()).equals(qFile1) || getFileToIndexMap().get(wrapper.getIndex()).equals(qFile2)))
				{}
				else
					priorityQue.add(wrapper);
			if(priorityQue.size() > k)
				priorityQue.poll();
		}
		DominantNodesWrapper dom = priorityQue.poll();
		while(dom != null){
			domNodes.add(getFileToIndexMap().get(dom.getIndex()));
			dom = priorityQue.poll();
		}
		return domNodes;
	}
	
	private class RankComparator implements Comparator<DominantNodesWrapper> {
	    /**
	     * compare overrides the compare function of Comparator class
	     */
	    public int compare(DominantNodesWrapper node1, DominantNodesWrapper node2) {
	        if (node1.getRankValue() <= node2.getRankValue()) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
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
		String[] line = buffRdr.readLine().split(splitBy);
		for(int i = 0; i < dimension; i++){
			line = buffRdr.readLine().split(splitBy);
			for(int j = 0 ; j < dimension; j++){
					result[i][j] = Float.parseFloat(line[j+1]);
			}
		}
		buffRdr.close();
		System.out.println("\nA Matrix Before Normalize...");
		SimilarityMeasureUtils.printMatrix(result);
		result = columnNormalize(result);
		System.out.println("\nA matrix After Normalize...");
		SimilarityMeasureUtils.printMatrix(result);
		return result;
	}
	
	private float[][] columnNormalize(float[][] result) {
		int[] divFactors = new int[dimension];
		float[][] res = new float[dimension][dimension];
		float[] col = new float[dimension];
		for(int i = 0; i < dimension;i++){
			for(int j = 0 ; j < dimension; j++){
				col[j] = result[j][i];
			}
			divFactors[i] = getFactor(col);
			//System.out.println("factor is "+ divFactors[i] + " : for line : " + i);
		}
		
		for(int i = 0; i < dimension;i++){
			for(int j = 0 ; j < dimension; j++){
				if(divFactors[i] != 0)
					res[j][i] = result[j][i]/divFactors[i];
				else
					res[j][i] = (float) 1/dimension;
			}
		}
		return res;
	}
	
	
	private float[][] columnNormalizeOld(float[][] result) {
		float[] colMax = new float[dimension];
		float[] colMin = new float[dimension];
		float[] colSum = new float[dimension];
		float[] tempCol = new float[dimension];
		float[][] res = new float[dimension][dimension];
		float[] col = new float[dimension];
		for(int i = 0; i < dimension;i++){
			for(int j = 0 ; j < dimension; j++){
				col[j] = result[j][i];
				colSum[i] = colSum[i] + result[j][i];
			}
			colMax[i] = getMax(col);
			colMin[i] = getMin(col);
		}
		
		for(int i = 0; i < dimension;i++){
			for(int j = 0 ; j < dimension; j++){
				tempCol[j] = result[j][i];
			}
			tempCol = normalizeCol(tempCol, colSum[i]);
			
			for(int j = 0; j < dimension; j++){
				res[j][i] = tempCol[j];
			}
		}
		return res;
	}
	
	
	
	private float[] normalizeCol(float[] tempCol, float f) {
		float diff, sum;
		sum = f;
		float res[] = new float[dimension];
		for(int i = 0 ; i < dimension; i++){
			res[i] = tempCol[i];
		}
		if(sum == 1)
			return tempCol;
		if(f < 1){
			diff = 1 - sum;
			while(true)
			{
				for(int i = 0 ; i < dimension; i++)
					res[i] = res[i] + (res[i] * diff);
				sum = getSum(res);
				if(sum >= 0.99 && sum <= 1)
					return res;
				else
					diff = 1 - sum;
			}
		}
		else{
			diff = sum - 1;
			while(true)
			{
				for(int i = 0 ; i < dimension; i++){
					res[i] = res[i] - (res[i] * diff);
				}
				sum = getSum(res);
				if(sum >= 0.99 && sum <= 1)
					return res;
				else
					diff = sum - 1;
			}
		}
	}
	private float getSum(float[] tempCol) {
		float res = 0;
		for(int i = 0; i < tempCol.length; i++){
			res = res + tempCol[i];
		}
		return res;
	}
	private float getMax(float[] col) {
		float max = Float.MIN_VALUE;
		for(int i = 0; i < col.length; i++){
			if(col[i] > max)
				max = col[i];
		}
		return max;
	}
	private float getMin(float[] col) {
		float min = Float.MAX_VALUE;
		for(int i = 0; i < col.length; i++){
			if(col[i] < min)
				min = col[i];
		}
		return min;
	}
	
	
	private int getFactor(float[] col) {
		int factor = 0;
		for(int i = 0; i < dimension; i++){
			if(col[i] == 1)
				factor++;
		}
		return factor;
	}
	public List<String> getKRelevantFiles(String simGraph, int k, float alpha, String qFile1, String qFile2, float convergenceFactor) throws IOException {
		float[][] AMatrix = new float[dimension][dimension];
		List<String> dominantNodes = new ArrayList<String>();
		AMatrix = getAmatrix(simGraph);
		float neighbourWalk[] = new float[dimension];
		float randomWalk[] = new float[dimension];
		for(int i = 0 ; i < dimension; i++){
			if(getFileToIndexMap().get(i).equals(qFile1) || getFileToIndexMap().get(i).equals(qFile2)){
				randomWalk[i] = (float)0.5;
				neighbourWalk[i] = (float)0.5;
			}
		}
		randomWalk = multiplyByFactor(1 - alpha, randomWalk);
	
		float temp[][] = new float[dimension][dimension];
		temp = multiplyByFactor(alpha, AMatrix);
		float[] prevUq = new float[dimension];
		int iteration = 1;
		while(true){
			prevUq = neighbourWalk;
			neighbourWalk = multiplyAllMatrices(temp, neighbourWalk, randomWalk);
			if(checkForConvergence(prevUq, neighbourWalk, convergenceFactor))
				break;
			iteration++;
		}
		System.out.println("\n Converged after "+iteration + " iterations");
		System.out.println("\nFinal page rank!!!\n");
		SimilarityMeasureUtils.printArray(neighbourWalk, getFileToIndexMap());
		dominantNodes = retrieveDominantNodes(neighbourWalk, k, qFile1, qFile2);
		return dominantNodes;
	}
	private boolean checkForConvergence(float[] prevUq, float[] neighbourWalk, float convergenceFactor) {
		float[] res = new float[dimension];
		for(int i = 0; i < dimension; i++){
			res[i] = (float)(Math.pow(neighbourWalk[i], 2) - Math.pow(prevUq[i], 2));
		}
		float sumAll = 0;
		for(int i = 0; i < dimension; i++){
			sumAll = sumAll + res[i];
		}
		sumAll = (float)Math.sqrt(sumAll);
		if(sumAll < convergenceFactor)
			return true;
		return false;
	}
	/**
	 * @return the fileToIndexMap
	 */
	public Map<Integer, String> getFileToIndexMap() {
		return fileToIndexMap;
	}
	/**
	 * @param fileToIndexMap the fileToIndexMap to set
	 */
	public void setFileToIndexMap(Map<Integer, String> fileToIndexMap) {
		this.fileToIndexMap = fileToIndexMap;
	}
}
