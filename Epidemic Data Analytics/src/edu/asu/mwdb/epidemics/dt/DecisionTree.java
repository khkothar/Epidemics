package edu.asu.mwdb.epidemics.dt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTree {
	
	private Map<String, int[]> data;
	private Map<String, Label> trainingSet;
	private TreeNode root;
	
	public DecisionTree(Map<String, int[]> data, Map<String, Label> trainingSet) {
		
		this.data = data;
		this.trainingSet = trainingSet;
		root = createDecisionTreeHelper(data, "");
		
	}

	private TreeNode createDecisionTreeHelper(Map<String, int[]> remainingData, String usedFeatures) {
		
		if(remainingData.size() > data.size() * 0.1) {
			
			int featureIndex = getFeatureWithHighestInformationGain(remainingData, usedFeatures);
			TreeNode treeNode = new TreeNode();
			treeNode.featureIndex = featureIndex;
			Map<String, int[]> leftEntries = getEntries(remainingData, featureIndex, 0);
			treeNode.left = createDecisionTreeHelper(leftEntries, usedFeatures + featureIndex + ",");
			Map<String, int[]> rightEntries = getEntries(remainingData, featureIndex, 1);
			treeNode.right = createDecisionTreeHelper(rightEntries, usedFeatures + featureIndex + ",");
			return treeNode;
			
		} else {
			return null;
		}
		
	}

	private Map<String, int[]> getEntries(Map<String, int[]> remainingData, int featureIndex, int value) {
		
		Map<String, int[]> entries = new HashMap<String, int[]>();
		for(Map.Entry<String, int[]> entry : remainingData.entrySet()) {
			if(entry.getValue()[featureIndex] == value) {
				entries.put(entry.getKey(), entry.getValue());
			}
		}
		return entries;
		
	}

	private int getFeatureWithHighestInformationGain(Map<String, int[]> remainingData, String usedFeatures) {
		
		Set<Integer> usedFeatureSet = parseUsedFeatures(usedFeatures);
		
		float entropyOfLabels = calculateEntropy(remainingData.keySet());
		int vectorSize = 0;
		for(Map.Entry<String, int[]> entry : remainingData.entrySet()) {
			vectorSize = entry.getValue().length;
			break;
		}
		
		float informationGain = Float.MIN_VALUE;
		int featureIndex = 0;
		
		for(int i = 0; i < vectorSize; i++) {
			
			if(usedFeatureSet.contains(i)) {
				continue;
			}
			
			Map<String, int[]> zeroEntries = getEntries(remainingData, i, 0);
			float zeroEntropy = calculateEntropy(zeroEntries.keySet());
			Map<String, int[]> oneEntries = getEntries(remainingData, i, 1);
			float oneEntropy = calculateEntropy(zeroEntries.keySet());
			
			float featureEntropy = ((float)zeroEntries.size() / remainingData.size()) * zeroEntropy + 
					((float)oneEntries.size() / remainingData.size()) * oneEntropy;
			
			if(entropyOfLabels - featureEntropy > informationGain) {
				informationGain = entropyOfLabels - featureEntropy;
				featureIndex = i;
			}
						
		}
		
		
		return featureIndex;
	}
	
	private Set<Integer> parseUsedFeatures(String usedFeatures) {

		Set<Integer> featureSet = new HashSet<Integer>();
		
		if(!usedFeatures.equals("")) {
			String[] featureArray = usedFeatures.split(",");
			for(String feature : featureArray) {
				if(feature != null) {
					featureSet.add(Integer.parseInt(feature));
				}
			}
		}
		
		return featureSet;
	}

	private float calculateEntropy(Set<String> files) {
		Map<Label, List<String>> labelMap = new HashMap<Label, List<String>>();
		
		for(String file : files) {
			if(labelMap.containsKey(trainingSet.get(file))) {
				labelMap.get(trainingSet.get(file)).add(file);
			} else {
				List<String> fileList = new ArrayList<String>();
				fileList.add(file);
				labelMap.put(trainingSet.get(file), fileList);
			}
		}
		
		float entropy = 0;
		
		for(Map.Entry<Label, List<String>> entry : labelMap.entrySet()) {
			float weight = (float) (1.0f * entry.getValue().size())/files.size();
			entropy += weight * (Math.log10(weight) / Math.log10(2));
		}
		
		return entropy * -1;
	}

	public Map<String, Label> identifyLabel(Map<String, int[]> queries) {
		
		Map<String, Label> results = new HashMap<String, Label>();
		
		for(Map.Entry<String, int[]> query : queries.entrySet()) {
			
			TreeNode tempRoot = root;
			TreeNode parent = root;
			int[] queryVector = query.getValue(); 
			while(tempRoot != null) {
				if(queryVector[tempRoot.featureIndex] == 0) {
					parent = tempRoot;
					tempRoot = tempRoot.left;
				} else {
					parent = tempRoot;
					tempRoot = tempRoot.left;
				}
			}
			
			Label label = getMaximumRepeatingLabel(parent.files);
			results.put(query.getKey(), label);
			
		}
		
		return results;
		
	}

	private Label getMaximumRepeatingLabel(List<String> files) {

		Label majorCandidate = trainingSet.get(files.get(0));
				int counter = 0;
		
		if(files.size() == 0) {
			return (Label) trainingSet.values().toArray()[0];
		}
		
	    for (int i = 1; i < files.size(); ++i) {
			if (majorCandidate.getName() == trainingSet.get(files.get(i)).getName()) {
	            ++counter;
	        } else if (counter == 0) {
	            majorCandidate = trainingSet.get(files.get(i));
	            ++counter;
	        } else {
	            --counter;
	        }
	    }
	    return majorCandidate;
		
	}
	
	
}
