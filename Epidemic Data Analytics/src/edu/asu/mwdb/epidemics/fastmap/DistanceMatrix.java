package edu.asu.mwdb.epidemics.fastmap;

import java.util.List;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class DistanceMatrix {

	private float[][] distanceInOriginalSpace;
	private float[][] distanceInReducedSpace;

	private int current;

	public DistanceMatrix(List<String> files, Similarity similarityMeasure)
			throws Exception {
		this.current = 0;
		// Calculating distances in original space
		for (int i = 0; i < files.size(); i++) {
			for (int j = i; j < files.size(); j++) {
				if (i == j) {
					distanceInOriginalSpace[i][j] = 0;
				} else {
					float distance = 1.0f / (1.0f + similarityMeasure.getScore(
							files.get(i), files.get(j)));
					distanceInOriginalSpace[i][j] = distance;
					distanceInOriginalSpace[j][i] = distance;
				}
			}
		}

	}

	public void update(float[][] reducedMatrix, Pivot pivot) {
		for (int i = 0; i < reducedMatrix.length; i++) {
			for (int j = 0; j < reducedMatrix.length; j++) {
				if (current == 0) {
					distanceInReducedSpace[i][j] = (float) Math
							.sqrt((Math.pow(distanceInOriginalSpace[i][j], 2) - Math
									.pow((reducedMatrix[i][current] - reducedMatrix[j][current]),
											2)));
				} else {
					distanceInReducedSpace[i][j] = (float) Math
							.sqrt((Math.pow(distanceInReducedSpace[i][j], 2) - Math
									.pow((reducedMatrix[i][current] - reducedMatrix[j][current]),
											2)));
				}
			}
		}
	}

	public float getProjectedDistance(Pivot pivot, int index) {
		return (distanceInReducedSpace[pivot.getA()][index] * distanceInReducedSpace[pivot.getA()][index] + 
				pivot.getDistance() * pivot.getDistance() - 
				distanceInReducedSpace[pivot.getB()][index] * distanceInReducedSpace[pivot.getB()][index])
				/(2.0f * pivot.getDistance());
	}

	public float[][] getCurrentMatrix() {
		if (current == 0)
			return distanceInOriginalSpace;
		else
			return distanceInReducedSpace;
	}

	public float[][] getDistanceMatrixInOriginalSpace() {
		return distanceInOriginalSpace;
	}

	public float[][] getDistanceMatrixInReducedSpace() {
		return distanceInReducedSpace;
	}

}
