package edu.asu.mwdb.epidemics.fastmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

public class FastMap {
	
	private List<Pivot> pivotList;
	private DistanceMatrix distanceMatrix;
	private ReducedMatrix reducedMatrix;
	private Similarity similarityMeasure;
	private List<String> files;
	float[][] distanceInReducedSpace;
	
	public FastMap(List<String> files, Similarity similarityMeasure, int k) throws Exception {
		this.files = files;
		this.similarityMeasure = similarityMeasure;
		pivotList = new ArrayList<Pivot>();
		distanceMatrix = new DistanceMatrix(files, similarityMeasure);
		reducedMatrix = new ReducedMatrix(files.size(), k, distanceMatrix);
		for(int i = 0; i < k; i++ ) {
			Pivot pivot = new Pivot(distanceMatrix.getCurrentMatrix());
			pivotList.add(pivot);
			reducedMatrix.addColumn(i, pivot);
			distanceMatrix.update(reducedMatrix.getMatrix(), pivot);
		}
		
		int noOfObjects = reducedMatrix.getMatrix().length;
		
		distanceInReducedSpace = new float[noOfObjects][noOfObjects];
		
		for(int i = 0; i < noOfObjects; i++) {
			for (int j = 0; j < noOfObjects; j++) {
				distanceInReducedSpace[i][j] = 0;
				for(int l = 0; l < pivotList.size(); l++) {
					distanceInReducedSpace[i][j] +=  Math.pow(reducedMatrix.getMatrix()[i][l] - reducedMatrix.getMatrix()[j][l], 2);  
				}
				distanceInReducedSpace[i][j] = (float) Math.sqrt(distanceInReducedSpace[i][j]);
			}
		}
		
		
		/*System.out.println("\ncurrent Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getCurrentMatrix());
		System.out.println("\noriginal matrix Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getDistanceMatrixInOriginalSpace());
		System.out.println("\nreduced Matrix Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getDistanceMatrixInReducedSpace());
		for(int i = 0; i < pivotList.size(); i++){
			System.out.println("\na : " + pivotList.get(i).getA() + " b : "+ pivotList.get(i).getB());
		}*/
	}
	
	public float getError() {
		float distanceDifference = 0;
		float sumOfDistanceInOriginalSpace = 0;
		
		float[][] distanceMatrixInOrigialSpace = distanceMatrix.getDistanceMatrixInOriginalSpace();
		
		for(int i = 0; i < distanceMatrixInOrigialSpace.length; i++) {
			for(int j = 0; j < distanceMatrixInOrigialSpace.length; j++) {
				distanceDifference += (float) Math.pow(distanceMatrixInOrigialSpace[i][j] - distanceInReducedSpace[i][j], 2);
				sumOfDistanceInOriginalSpace += (float) Math.pow(distanceMatrixInOrigialSpace[i][j], 2);
			}
		}
		
		return (float) Math.sqrt(distanceDifference/sumOfDistanceInOriginalSpace);
	}
	
	public List<String> getTopKSimilarFiles(String query, int k) throws Exception {
			
		float queryCoordinates[] = new float[pivotList.size()];
		
		PriorityQueue<Wrapper> heap = new PriorityQueue<Wrapper>(k, new MinHeap());

		for(int i = 0; i < pivotList.size(); i++) {
			
			Pivot pivot = pivotList.get(i);
			float qa, qb, ab;
				
			float score = similarityMeasure.getScore(files.get(pivot.getA()), query);
			
			if(similarityMeasure instanceof EuclideanSimilarity || similarityMeasure instanceof DTWSimilarity) {
				if(score == 0) qa = Float.MAX_VALUE;
				else qa = 1.0f/score;
			} else {
				qa = 1.0f/(1 + score);
			}
			
			score = similarityMeasure.getScore(files.get(pivot.getB()), query);
			
			if(similarityMeasure instanceof EuclideanSimilarity || similarityMeasure instanceof DTWSimilarity) {
				if(score == 0) qb = Float.MAX_VALUE;
				else qb = 1.0f/score;
			} else {
				qb = 1.0f/(1 + score);
			}
			
			score = similarityMeasure.getScore(files.get(pivot.getB()), files.get(pivot.getA()));
			
			if(similarityMeasure instanceof EuclideanSimilarity || similarityMeasure instanceof DTWSimilarity) {
				if(score == 0) ab = Float.MAX_VALUE;
				else ab = 1.0f/score;
			} else {
				ab = 1.0f/(1 + score);
			}
			
			queryCoordinates[i] = (qa*qa + ab*ab - qb*qb)/2*ab;
		}
		
		System.out.println("\n\nReduced Matrix\n\n");
		SimilarityMeasureUtils.printMatrix(reducedMatrix.getMatrix());
		
		System.out.println();
		
		for(int i = 0; i < pivotList.size(); i++) {
			System.out.print(queryCoordinates[i] + " ");
		}
		for(int i = 0; i < reducedMatrix.getMatrix().length; i++) {
			float distance = 0;
			for(int j = 0; j < pivotList.size(); j++) {
				distance += (float) Math.pow(queryCoordinates[j] - reducedMatrix.getMatrix()[i][j], 2);
			}
			
			System.out.println(Math.sqrt(distance));
			
			heap.offer(new Wrapper((float)Math.sqrt(distance), i));
			
			if(heap.size() > k) {
				heap.poll();
			}
		}
		
		List<String> results = new ArrayList<String>();
		
		for(int i = 0; i < k; i++) {
			System.out.println("sim file " + i + " : " + files.get(heap.peek().index) + " : " + heap.peek().distance);
			results.add(files.get(heap.poll().getIndex()));
		}
		
		return results;
	}
	
	private class MinHeap implements Comparator<Wrapper> {

		@Override
		public int compare(Wrapper o1, Wrapper o2) {
			return o1.getDistance() <= o2.getDistance() ? 1 : -1;
		}
		
	}
	
	private class Wrapper {

		private float distance;
		private int index;
		
		public Wrapper(float distance, int index) {
			this.distance = distance;
			this.index = index;
		}
		
		public float getDistance() {
			return distance;
		}
		public int getIndex() {
			return index;
		}
	}
}
