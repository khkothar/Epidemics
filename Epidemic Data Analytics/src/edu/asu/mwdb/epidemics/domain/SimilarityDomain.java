package edu.asu.mwdb.epidemics.domain;

public class SimilarityDomain {
	private String queryFile;
	private String simFile;
	private float similarity;
	/**
	 * @return the queryFile
	 */
	public String getQueryFile() {
		return queryFile;
	}
	/**
	 * @param queryFile the queryFile to set
	 */
	public void setQueryFile(String queryFile) {
		this.queryFile = queryFile;
	}
	/**
	 * @return the simFile
	 */
	public String getSimFile() {
		return simFile;
	}
	/**
	 * @param simFile the simFile to set
	 */
	public void setSimFile(String simFile) {
		this.simFile = simFile;
	}
	/**
	 * @return the similarity
	 */
	public float getSimilarity() {
		return similarity;
	}
	/**
	 * @param similarity the similarity to set
	 */
	public void setSimilarity(float similarity) {
		this.similarity = similarity;
	}
}
