package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Id;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;

public class WeightedAverageWordSimilarity extends WeightedWordSimilarity implements Similarity {

	@Override
	public float getScore(String fileName1, String fileName2) throws Exception {

		Set<Window> uniqueWindows = new LinkedHashSet<Window>();
		Map<Window, List<Id>> wordOccuranceMapForFile1 = new LinkedHashMap<Window, List<Id>>();
		Map<Window, List<Id>> wordOccuranceMapForFile2 = new LinkedHashMap<Window, List<Id>>();

		BufferedReader br = new BufferedReader(new FileReader(new File(
				"simulation dictionary/epidemic_word_file_avg.csv")));

		String line = "";

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(fileName1)) {
				uniqueWindows.add(word.getWindow());
				if (wordOccuranceMapForFile1.containsKey(word.getWindow())) {
					wordOccuranceMapForFile1.get(word.getWindow()).add(
							word.getId());
				} else {
					List<Id> occuranceList = new ArrayList<Id>();
					occuranceList.add(word.getId());
					wordOccuranceMapForFile1.put(word.getWindow(),
							occuranceList);
				}
			}
		}
		br.close();
		
		br = new BufferedReader(new FileReader(new File(
				"query dictionary/epidemic_word_file_avg.csv")));
		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(fileName2)) {
				uniqueWindows.add(word.getWindow());
				if (wordOccuranceMapForFile2.containsKey(word.getWindow())) {
					wordOccuranceMapForFile2.get(word.getWindow()).add(
							word.getId());
				} else {
					List<Id> occuranceList = new ArrayList<Id>();
					occuranceList.add(word.getId());
					wordOccuranceMapForFile2.put(word.getWindow(),
							occuranceList);
				}
			}
		}
		br.close();

		int[] binaryVector1 = new int[uniqueWindows.size()];
		createBinaryVector(binaryVector1, wordOccuranceMapForFile1,
				uniqueWindows);
		int[] binaryVector2 = new int[uniqueWindows.size()];
		createBinaryVector(binaryVector2, wordOccuranceMapForFile2,
				uniqueWindows);

		float[][] A = computeAMatrix(binaryVector1, binaryVector2,
				wordOccuranceMapForFile1, wordOccuranceMapForFile2,
				uniqueWindows);
		
		return matrixMultiplications(binaryVector1, binaryVector2, A)/uniqueWindows.size();
	}

}
