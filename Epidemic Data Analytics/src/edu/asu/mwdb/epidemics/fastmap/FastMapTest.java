package edu.asu.mwdb.epidemics.fastmap;

import java.util.List;

import edu.asu.mwdb.epidemics.similarity.Similarity;

public class FastMapTest {
	public static void computeFastMap(List<String> files, Similarity similarityMeasure, int r) {
		float similarity[][] = new float[files.size()][files.size()];
		
		for (int i = 0; i < similarity.length; i++) {
			for (int j = i; i < similarity.length; j++) {
				try {
					similarityMeasure.getScore(files.get(i), files.get(j));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
