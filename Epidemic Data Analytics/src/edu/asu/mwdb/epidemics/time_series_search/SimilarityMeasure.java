package edu.asu.mwdb.epidemics.time_series_search;

public enum SimilarityMeasure {
	NOMEASURE(0),
	EUCLIDEAN(1),
	DTW(2),
	WORD(3),
	WORD_AVG(4),
	WORD_DIFF(5),
	WIEGHTED_WORD(6),
	WEIGHTED_WORD_AVG(7),
	WEIGTHED_WORD_DIFF(8);
	
	private int type;
	private SimilarityMeasure(int type) {
		this.setType(type);
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
};
