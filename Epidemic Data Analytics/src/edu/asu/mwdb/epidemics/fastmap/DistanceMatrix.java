package edu.asu.mwdb.epidemics.fastmap;

import java.util.List;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class DistanceMatrix {
	
	private float[][] distanceInOriginalSpace;
	private float[][] distanceInReducedSpace;
	
	private int count;
	
	public DistanceMatrix(List<String> files, Similarity similarityMeasure) throws Exception {
		this.count = 0;
		//Calculating distances in original space
		for(int i = 0; i < files.size(); i++) {
			for(int j = i; j < files.size(); j++) {
				if(i == j) {
					distanceInOriginalSpace[i][j] = 0;
				} else {
					float distance = 1.0f/(1.0f + similarityMeasure.getScore(files.get(i), files.get(j)));
					distanceInOriginalSpace[i][j] = distance;
					distanceInOriginalSpace[j][i] = distance;
				}
			}
		}
		
	}
	
	public void update(float[][] reducedMatrix, Pivot pivot) {
		
	}
	
	public float getProjectedDistance(Pivot pivot, int object) {
		return 0;
	}
	
	public float[][] getCurrentMatrix() {
		return null;
	}

	public float[][] getDistanceMatrixInOriginalSpace() {
		return distanceInOriginalSpace;
	}

	public float[][] getDistanceMatrixInReducedSpace() {
		return distanceInReducedSpace;
	}
	
}
