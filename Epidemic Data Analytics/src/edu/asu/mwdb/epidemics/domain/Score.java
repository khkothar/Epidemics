/**
 * 
 */
package edu.asu.mwdb.epidemics.domain;

/**
 * @author Mohan-Thorat
 *
 */
public class Score implements Comparable<Score>{

	private String fileName;
	private double score;
	
	public Score() {}
	
	public Score(String fileName, double score) {
		this.fileName = fileName;
		this.score = score;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public String toString() {
		return this.fileName + "," + this.score;
	}
	
	public int compareTo(Score s) {
		return this.getScore() == s.getScore() ? 0 : (this.getScore() < s.getScore() ? 1 : -1);
	}
}
