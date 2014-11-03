package edu.asu.mwdb.epidemics.fastmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class FastMap {
	
	private List<Pivot> pivotList;
	private DistanceMatrix distanceMatrix;
	private ReducedMatrix reducedMatrix;
	private Similarity similarityMeasure;
	private List<String> files;
	
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
		/*System.out.println("\ncurrent Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getCurrentMatrix());
		System.out.println("\noriginal matrix Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getDistanceMatrixInOriginalSpace());
		System.out.println("\nreduced Matrix Matrix");
		SimilarityMeasureUtils.printMatrix(distanceMatrix.getDistanceMatrixInReducedSpace());
		for(int i = 0; i < pivotList.size(); i++){
			System.out.println("\na : " + pivotList.get(i).getA() + " b : "+ pivotList.get(i).getB());
		}*/
		System.out.println("\nerror between reduced space and original space : "+ getError());
	}
	
	public float getError() {
		float distanceDifference = 0;
		float sumOfDistanceInOriginalSpace = 0;
		
		float[][] distanceMatrixInOrigialSpace = distanceMatrix.getDistanceMatrixInOriginalSpace();
		float[][] distanceMatrixInReducedSpace = distanceMatrix.getDistanceMatrixInReducedSpace();
		
		for(int i = 0; i < distanceMatrixInOrigialSpace.length; i++) {
			for(int j = 0; j < distanceMatrixInOrigialSpace.length; j++) {
				distanceDifference += (float) Math.pow(distanceMatrixInOrigialSpace[i][j] - distanceMatrixInReducedSpace[i][j], 2);
				sumOfDistanceInOriginalSpace += (float) Math.pow(distanceMatrixInOrigialSpace[i][j], 2);
			}
		}
		
		return (float) Math.sqrt(distanceDifference/sumOfDistanceInOriginalSpace);
	}
	
	public List<String> getTopKSimilarFiles(String query, int k) throws Exception {
			
		float modifiedDistance = 0;
		
		PriorityQueue<Wrapper> heap = new PriorityQueue<Wrapper>(k, new MinHeap());

		for(int i = 0; i < pivotList.size(); i++) {
			Pivot pivot = pivotList.get(i);
			float qa, qb, ab;
			if(i == 0) {
				qa = (1.0f/1.0f + similarityMeasure.getScore(files.get(pivot.getA()), query));
				qb = (1.0f/1.0f + similarityMeasure.getScore(files.get(pivot.getB()), query));
				ab = pivot.getDistance();
			} else {
				qa = reducedMatrix.getMatrix()[pivot.getA()][i - 1] - modifiedDistance;
				qb = reducedMatrix.getMatrix()[pivot.getB()][i - 1] - modifiedDistance;
				ab = pivot.getDistance();
			}
			
			modifiedDistance = (qa*qa + ab*ab - qb*qb)/2*ab;
		}
		
		for(int i = 0; i < pivotList.size(); i++) {
			if(heap.size() == k) {
				heap.poll();
			}
			heap.offer(new Wrapper(Math.abs(reducedMatrix.getMatrix()[i][pivotList.size() - 1] - modifiedDistance), i));
		}
		
		List<String> results = new ArrayList<String>();
		
		for(int i = 0; i < k; i++) {
			System.out.println("sim file " + i + " : " + files.get(heap.peek().index));
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
