package edu.asu.mwdb.epidemics.fastmap;

import java.util.ArrayList;
import java.util.List;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class FastMap {
	
	/**
	 * @return the pivotList
	 */
	public List<Pivot> getPivotList() {
		return pivotList;
	}

	/**
	 * @param pivotList the pivotList to set
	 */
	public void setPivotList(List<Pivot> pivotList) {
		this.pivotList = pivotList;
	}

	/**
	 * @return the distanceMatrix
	 */
	public DistanceMatrix getDistanceMatrix() {
		return distanceMatrix;
	}

	/**
	 * @param distanceMatrix the distanceMatrix to set
	 */
	public void setDistanceMatrix(DistanceMatrix distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	/**
	 * @return the reducedMatrix
	 */
	public ReducedMatrix getReducedMatrix() {
		return reducedMatrix;
	}

	/**
	 * @param reducedMatrix the reducedMatrix to set
	 */
	public void setReducedMatrix(ReducedMatrix reducedMatrix) {
		this.reducedMatrix = reducedMatrix;
	}

	private List<Pivot> pivotList;
	private DistanceMatrix distanceMatrix;
	private ReducedMatrix reducedMatrix;
	
	public FastMap(List<String> files, Similarity similarityMeasure, int k) throws Exception {
		pivotList = new ArrayList<Pivot>();
		distanceMatrix = new DistanceMatrix(files, similarityMeasure);
		reducedMatrix = new ReducedMatrix(files.size(), k, distanceMatrix);
		for(int i = 0; i < k; i++ ) {
			Pivot pivot = new Pivot(distanceMatrix.getCurrentMatrix());
			pivotList.add(pivot);
			reducedMatrix.addColumn(i, pivot);
			distanceMatrix.update(reducedMatrix.getMatrix(), pivot);
		}
	}
}
