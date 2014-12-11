package edu.asu.mwdb.epidemics.fastmap;

import java.util.List;

import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;

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
					float score = similarityMeasure.getScore(files.get(i), files.get(j));
					float distance = 0;
					if(similarityMeasure instanceof EuclideanSimilarity || similarityMeasure instanceof DTWSimilarity) {
						if(score == 0) distance = Float.MAX_VALUE;
						else distance = 1.0f/score;
					} else {
						distance = 1.0f/(1 + score);
					}
					distanceMatrixInOriginalSpace[i][j] = distance;
					distanceMatrixInOriginalSpace[j][i] = distance;
					
					distanceMatrixInReducedSpace[i][j] = distance;
					distanceMatrixInReducedSpace[j][i] = distance;
				}
			}
		}

	}

	public void update(float[][] reducedMatrix, Pivot pivot) {
		for (int i = 0; i < reducedMatrix.length; i++) {
			for (int j = 0; j < reducedMatrix.length; j++) {
				
				distanceMatrixInReducedSpace[i][j] = (float) Math
						.sqrt(Math.abs((Math.pow(distanceMatrixInReducedSpace[i][j], 2) - Math
								.pow((reducedMatrix[i][current] - reducedMatrix[j][current]),
										2))));
			}
		}
		
		SimilarityMeasureUtils.printMatrix(distanceMatrixInReducedSpace);
		System.out.println("\n\n\n");
		
		current++;
	}

	public float getProjectedDistance(Pivot pivot, int index) {
		float qa, qb, ab;
		ab = pivot.getDistance();
		if(current == 0) {
			qa = distanceMatrixInOriginalSpace[pivot.getA()][index];
			qb = distanceMatrixInOriginalSpace[pivot.getB()][index];
		} else {
			qa = distanceMatrixInReducedSpace[pivot.getA()][index];
			qb = distanceMatrixInReducedSpace[pivot.getB()][index];
		}
		
		return (qa*qa + ab*ab - qb*qb)/(2.0f * ab);
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
