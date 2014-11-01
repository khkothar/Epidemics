package edu.asu.mwdb.epidemics.time_series_search;

public class SimilarityMeasureUtils {
	public static SimilarityMeasure getSimilarityMeasure(int measureValue){
		switch(measureValue){
			case 1:
				return SimilarityMeasure.EUCLIDEAN;
			case 2:
				return SimilarityMeasure.DTW;
			case 3:
				return SimilarityMeasure.WORD;
			case 4:
				return SimilarityMeasure.WORD_AVG;
			case 5:
				return SimilarityMeasure.WORD_DIFF;
			case 6:
				return SimilarityMeasure.WIEGHTED_WORD;
			case 7:
				return SimilarityMeasure.WEIGHTED_WORD_AVG;
			case 8:
				return SimilarityMeasure.WEIGTHED_WORD_DIFF;
		}
		return SimilarityMeasure.NOMEASURE;
	}
}
