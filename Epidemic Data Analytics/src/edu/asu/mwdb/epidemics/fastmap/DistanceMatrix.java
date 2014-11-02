package edu.asu.mwdb.epidemics.fastmap;

import java.util.List;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class DistanceMatrix {

	private float[][] distanceMatrixInOriginalSpace;
	private float[][] distanceMatrixInReducedSpace;

	private int current;

	public DistanceMatrix(List<String> files, Similarity similarityMeasure)
			throws Exception {
		this.current = 0;
		distanceMatrixInOriginalSpace = new float[files.size()][files.size()];
		distanceMatrixInReducedSpace = new float[files.size()][files.size()];
		// Calculating distances in original space
		for (int i = 0; i < files.size(); i++) {
			for (int j = i; j < files.size(); j++) {
				if (i == j) {
					distanceMatrixInOriginalSpace[i][j] = 0;
				} else {
					float distance = 1.0f / (1.0f + similarityMeasure.getScore(
							files.get(i), files.get(j)));
					distanceMatrixInOriginalSpace[i][j] = distance;
					distanceMatrixInOriginalSpace[j][i] = distance;
				}
			}
		}

	}

	public void update(float[][] reducedMatrix, Pivot pivot) {
		for (int i = 0; i < reducedMatrix.length; i++) {
			for (int j = 0; j < reducedMatrix.length; j++) {
				if (current == 0) {
					distanceMatrixInReducedSpace[i][j] = (float) Math
							.sqrt((Math.pow(distanceMatrixInOriginalSpace[i][j], 2) - Math
									.pow((reducedMatrix[i][current] - reducedMatrix[j][current]),
											2)));
				} else {
					distanceMatrixInReducedSpace[i][j] = (float) Math
							.sqrt((Math.pow(distanceMatrixInReducedSpace[i][j], 2) - Math
									.pow((reducedMatrix[i][current] - reducedMatrix[j][current]),
											2)));
				}
			}
		}
	}

	public float getProjectedDistance(Pivot pivot, int index) {
		if(current == 0) {
			return (distanceMatrixInOriginalSpace[pivot.getA()][index] * distanceMatrixInOriginalSpace[pivot.getA()][index] + 
					pivot.getDistance() * pivot.getDistance() - 
					distanceMatrixInOriginalSpace[pivot.getB()][index] * distanceMatrixInOriginalSpace[pivot.getB()][index])
					/(2.0f * pivot.getDistance());
		} else {
			return (distanceMatrixInReducedSpace[pivot.getA()][index] * distanceMatrixInReducedSpace[pivot.getA()][index] + 
					pivot.getDistance() * pivot.getDistance() - 
					distanceMatrixInReducedSpace[pivot.getB()][index] * distanceMatrixInReducedSpace[pivot.getB()][index])
					/(2.0f * pivot.getDistance());
		}
	}

	public float[][] getCurrentMatrix() {
		if (current == 0)
			return distanceMatrixInOriginalSpace;
		else
			return distanceMatrixInReducedSpace;
	}

	public float[][] getDistanceMatrixInOriginalSpace() {
		return distanceMatrixInOriginalSpace;
	}

	public float[][] getDistanceMatrixInReducedSpace() {
		return distanceMatrixInReducedSpace;
	}

}