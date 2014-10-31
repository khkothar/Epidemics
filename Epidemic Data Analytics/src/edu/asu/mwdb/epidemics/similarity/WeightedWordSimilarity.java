package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Id;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;

public class WeightedWordSimilarity implements Similarity {

	
	
	@Override
	public float getScore(String fileName1, String fileName2) throws Exception {
		
		Set<Window> uniqueWindows = new LinkedHashSet<Window>();
		Map<Window, List<Id>> wordOccuranceMapForFile1 = new LinkedHashMap<Window, List<Id>>();
		Map<Window, List<Id>> wordOccuranceMapForFile2 = new LinkedHashMap<Window, List<Id>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"epidemic_word_file.csv")));
		
		String line = "";

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(fileName1)) {
				uniqueWindows.add(word.getWindow());
				if(wordOccuranceMapForFile1.containsKey(word.getWindow())) {
					wordOccuranceMapForFile1.get(word.getWindow()).add(word.getId()); 
				} else {
					List<Id> occuranceList = new ArrayList<Id>();
					occuranceList.add(word.getId());
					wordOccuranceMapForFile1.put(word.getWindow(), occuranceList);
				}
			}
			
			if (word.getId().getFileName().equals(fileName2)) {
				uniqueWindows.add(word.getWindow());
				if(wordOccuranceMapForFile2.containsKey(word.getWindow())) {
					wordOccuranceMapForFile2.get(word.getWindow()).add(word.getId()); 
				} else {
					List<Id> occuranceList = new ArrayList<Id>();
					occuranceList.add(word.getId());
					wordOccuranceMapForFile2.put(word.getWindow(), occuranceList);
				}
			}
		}
		br.close();
		
		int[] binaryVector1 = new int[uniqueWindows.size()];
		createBinaryVector(binaryVector1, wordOccuranceMapForFile1, uniqueWindows);
		int[] binaryVector2 = new int[uniqueWindows.size()];
		createBinaryVector(binaryVector2, wordOccuranceMapForFile2, uniqueWindows);
		
		float[][] A = computeAMatrix(binaryVector1, binaryVector2, wordOccuranceMapForFile1, wordOccuranceMapForFile2, uniqueWindows);
		return 0;
	}

	private void createBinaryVector(int[] binaryVector, Map<Window, List<Id>> wordOccuranceMapForFile, Set<Window> uniqueWindows) {
		int i = 0;
		
		for(Window w : uniqueWindows) {
			if(wordOccuranceMapForFile.containsKey(w)) {
				binaryVector[i++] = 1;
			} else {
				binaryVector[i++] = 0;
			}
		}
	}

	private float[][] computeAMatrix(int[] binaryVector1, int[] binaryVector2, Map<Window, List<Id>> wordOccuranceMapForFile1,
			Map<Window, List<Id>> wordOccuranceMapForFile2,
			Set<Window> uniqueWindows) {
		int n = binaryVector1.length;
		Iterator<Window> itr1= uniqueWindows.iterator();
		float[][] A = new float[n][n];
		for(int i = 0; i < n; i++) {
			Window w1 = itr1.next();
			Iterator<Window> itr2 = uniqueWindows.iterator();
			for(int j = 0; j < n; j++) {
				if(binaryVector1[i] == 1 && binaryVector2[j] == 1) {
					Window w2 = itr2.next();
					A[i][j] = computeWeight(w1, w2, wordOccuranceMapForFile1.get(w1), wordOccuranceMapForFile2.get(w2)); 
				}
				
			}
		}
		return A;
	}

	private float computeWeight(Window w1, Window w2, List<Id> occurancesInFile1,
			List<Id> occurancesInFile2) {
		
		return 0;
	}

}
