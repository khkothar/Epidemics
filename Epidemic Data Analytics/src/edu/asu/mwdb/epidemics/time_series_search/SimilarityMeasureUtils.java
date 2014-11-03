package edu.asu.mwdb.epidemics.time_series_search;

import java.text.DecimalFormat;

import edu.asu.mwdb.epidemics.similarity.AverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.DifferenceWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;
import edu.asu.mwdb.epidemics.similarity.WeightedAverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedDiffWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WordSimilarity;

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

	/**
	 * 
	 * @param queryFile
	 * @param simFile
	 * @param measure
	 * @return
	 * @throws Exception
	 * returns the Similarity measure as float
	 */
	public static Similarity getSimilarity(SimilarityMeasure measure) throws Exception {
		switch(measure.getType()){
		case 1:
			return new EuclideanSimilarity();
		case 2:
			return new DTWSimilarity();
		case 3:
			return new WordSimilarity();
		case 4:
			return new AverageWordSimilarity();
		case 5:
			return new DifferenceWordSimilarity();
		case 6:
			return new WeightedWordSimilarity();
		case 7:
			return new WeightedAverageWordSimilarity();
		case 8:
			return new WeightedDiffWordSimilarity();
		default:
			return null;
		}
	}
	
	public static void printMatrix(float[][] matrix){
		/*
		 * temporary result print
		 */
		DecimalFormat df = new DecimalFormat("#.#########");
		for(int i = 0; i < matrix.length; i++){
			System.out.println();
			for(int j = 0; j < matrix[0].length; j++){
				System.out.print(" "+df.format(matrix[i][j]));
			}
		}
	}
}
