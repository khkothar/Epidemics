package edu.asu.mwdb.epidemics.similarity;

public interface Similarity {
	float getScore(String fileName1, String fileName2) throws Exception;
}
