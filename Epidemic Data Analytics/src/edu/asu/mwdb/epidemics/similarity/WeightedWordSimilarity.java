package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Id;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.neighborhood.LocationMatrix;

public class WeightedWordSimilarity implements Similarity {
	
	@Override
	public float getScore(String fileName1, String fileName2) throws Exception {

		boolean flag = false;
		Set<Window> uniqueWindows = new LinkedHashSet<Window>();
		Map<Window, List<Id>> wordOccuranceMapForFile1 = new LinkedHashMap<Window, List<Id>>();
		Map<Window, List<Id>> wordOccuranceMapForFile2 = new LinkedHashMap<Window, List<Id>>();
		
		File file1 = new File(fileName1);
		File file2 = new File(fileName2);

		BufferedReader br = new BufferedReader(new FileReader(new File(
				"simulation dictionary/epidemic_word_file.csv")));

		String line = "";

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(file1.getName())) {
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
			
			if (word.getId().getFileName().equals(file2.getName())) {
				flag = true;
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
		
		if( !flag ) {
			br = new BufferedReader(new FileReader(new File(
					"query dictionary/epidemic_word_file.csv")));
			while ((line = br.readLine()) != null) {
				Word word = new Word(line);
				if (word.getId().getFileName().equals(file2.getName())) {
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
		}

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

	protected float matrixMultiplications(int[] binaryVector1, int[] binaryVector2, float[][] A) {
		float intermediate[] = new float[binaryVector1.length];
		for(int i = 0; i < binaryVector1.length; i++) {
			int val = binaryVector1[i];
			intermediate[i] = 0;
			for(int j = 0; j < A.length; j++) {
				intermediate[i] += val * A[j][i];
			}
		}
		
		float score = 0;
		
		for(int i = 0; i < intermediate.length; i++) {
				score += intermediate[i] * binaryVector2[i];
		}
		
		return score;
	}
	
	protected void createBinaryVector(int[] binaryVector,
			Map<Window, List<Id>> wordOccuranceMapForFile,
			Set<Window> uniqueWindows) {
		int i = 0;

		for (Window w : uniqueWindows) {
			if (wordOccuranceMapForFile.containsKey(w)) {
				binaryVector[i++] = 1;
			} else {
				binaryVector[i++] = 0;
			}
		}
	}

	protected float[][] computeAMatrix(int[] binaryVector1, int[] binaryVector2,
			Map<Window, List<Id>> wordOccuranceMapForFile1,
			Map<Window, List<Id>> wordOccuranceMapForFile2,
			Set<Window> uniqueWindows) throws IOException {
		int n = binaryVector1.length;
		Iterator<Window> itr1 = wordOccuranceMapForFile1.keySet().iterator();
		float[][] A = new float[n][n];
		for (int i = 0; i < n; i++) {
			if(binaryVector1[i] == 0) continue;
			Window w1 = itr1.next();
			Iterator<Window> itr2 = wordOccuranceMapForFile2.keySet().iterator();
			for (int j = 0; j < n; j++) {
				if (binaryVector2[j] == 1) {
					Window w2 = itr2.next();
					A[i][j] = computeWeight(w1, w2,
							wordOccuranceMapForFile1.get(w1),
							wordOccuranceMapForFile2.get(w2));
				}

			}
		}
		return A;
	}

	private float computeWeight(Window w1, Window w2,
			List<Id> occurancesInFile1, List<Id> occurancesInFile2)
			throws IOException {
		float wordDistance = computeDistanceBetweenWords(w1, w2);
		float idWeight = computeStateTimeWeight(occurancesInFile1,
				occurancesInFile2);
		return wordDistance * idWeight;
	}

	private float computeStateTimeWeight(List<Id> occurancesInFile1,
			List<Id> occurancesInFile2) throws IOException {
		
		LocationMatrix locationMatrix = new LocationMatrix("LocationMatrix.csv");
		int heapSize = occurancesInFile1.size() > occurancesInFile2.size() ? occurancesInFile2
				.size() : occurancesInFile1.size();
		PriorityQueue<Float> heap = new PriorityQueue<Float>(heapSize, new MaxHeap());
		
		for (int i = 0; i < occurancesInFile1.size(); i++) {
			for (int j = 0; j < occurancesInFile2.size(); j++) {
				float timeFactor = 1.0f / (1.0f + Math
						.abs((Integer.parseInt(occurancesInFile1.get(i)
								.getIteration()) - Integer
								.parseInt(occurancesInFile2.get(j)
										.getIteration()))));
				int stateDistance = locationMatrix
						.getShortestDistance(occurancesInFile1.get(i)
								.getState(), occurancesInFile2.get(j)
								.getState());
				float stateFactor = 0;
				if(stateDistance != Integer.MAX_VALUE) {
					stateFactor = (float) (1.0f / (1.0f + Math.pow(stateDistance, 2)));
				}
				if(heap.size() == heapSize) {
					//remove lowest element
					heap.poll();
				}
				heap.offer(timeFactor * stateFactor);
			}
		}

		float sum = 0.0f;
		
		for(int i = 0; i < heapSize; i++) {
			sum += heap.poll();
		}
		
		return sum/(occurancesInFile1.size() < occurancesInFile2.size() ? occurancesInFile2
				.size() : occurancesInFile1.size());
	}

	private float computeDistanceBetweenWords(Window w1, Window w2) {
		int distance = 0;
		for (int i = 0; i < w1.getValues().length; i++) {
			distance += Math.pow((w1.getValues()[i] - w2.getValues()[i]), 2);
		}
		return 1.0f / (1.0f + distance);
	}
	
	private class MaxHeap implements Comparator<Float> {

		@Override
		public int compare(Float o1, Float o2) {
			return o1 >= o2 ? 1 : -1;
		}
		
	}

}
