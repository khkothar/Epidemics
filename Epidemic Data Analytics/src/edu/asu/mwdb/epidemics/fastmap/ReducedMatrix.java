package edu.asu.mwdb.epidemics.fastmap;

public class ReducedMatrix {
	
	private float matrix[][];
	private DistanceMatrix distanceMatrix;

	public ReducedMatrix(int n, int k, DistanceMatrix distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
		matrix = new float[n][k];
	}
	
	public void addColumn(int current, Pivot pivot) {
		if(pivot.getDistance() == 0) return;
		for(int i = 0; i < matrix.length; i++) {
			matrix[i][current] = distanceMatrix.getProjectedDistance(pivot, i);
		}
	}

	public float[][] getMatrix() {
		return matrix;
	}
	
}
