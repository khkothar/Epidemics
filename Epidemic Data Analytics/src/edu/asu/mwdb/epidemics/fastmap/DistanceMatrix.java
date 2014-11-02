package edu.asu.mwdb.epidemics.fastmap;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class DistanceMatrix {
	
	private float distanceInOriginalSpace;
	private float distanceInReducedSpace;
	
	public DistanceMatrix(int n, Similarity similarityMeasure) {
		
	}
	
	public void update(float[][] reducedMatrix, Pivot pivot) {
		
	}
	
	public float getProjectedDistance(Pivot pivot, int object) {
		return 0;
	}
	
	public float[][] getCurrentMatrix() {
		return null;
	}

	public float getDistanceMatrixInOriginalSpace() {
		return distanceInOriginalSpace;
	}

	public float getDistanceMatrixInReducedSpace() {
		return distanceInReducedSpace;
	}
	
}
