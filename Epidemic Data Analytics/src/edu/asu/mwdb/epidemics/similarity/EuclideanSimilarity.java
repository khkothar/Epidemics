package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.FileReader;

public class EuclideanSimilarity implements Similarity{

	/* (non-Javadoc)
	 * @see edu.asu.mwdb.epidemics.similarity.Similarity#getScore()
	 */
	
	public float getScore(String fileName1, String fileName2) throws Exception {
		float[] similarityArray = new float[51];
		float partialSimilarity = 0;
		String line1, line2;
		int i;
		String[] inputArray1, inputArray2;
		BufferedReader bufReader1 = new BufferedReader(new FileReader(fileName1));
		BufferedReader bufReader2 = new BufferedReader(new FileReader(fileName2));
		
		bufReader1.readLine();
		bufReader2.readLine();
		while((line1 = bufReader1.readLine()) != null && (line2 = bufReader2.readLine()) != null) {
			inputArray1 = line1.split(",");
			inputArray2 = line2.split(",");
			for(i=2; i<inputArray1.length && i<inputArray2.length ; i++) {
				similarityArray[i-2] += calculateDifferenceOfSqaures(inputArray1[i], inputArray2[i]);
			}
		}
		for (int j = 0; j < similarityArray.length; j++) {
			partialSimilarity += Math.sqrt(similarityArray[j]);
		}
		bufReader1.close();
		bufReader2.close();
		
		return 1/(1+partialSimilarity/51);
	}
	
	public double calculateDifferenceOfSqaures(String value1, String value2) {
		double in1 = Double.parseDouble(value1);
		double in2 = Double.parseDouble(value2);
		
		return Math.abs(in1*in1 - in2*in2);
	}

}
