package edu.asu.mwdb.epidemics.dt;

import java.awt.Label;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DecisionTree {
	
	private Map<String, int[]> data;
	private Map<String, Label> trainingSet;
	private Set<Label> usedLabels;
	private TreeNode root;
	
	public DecisionTree(Map<String, int[]> data, Map<String, Label> trainingSet) {
		
		this.data = data;
		this.trainingSet = trainingSet;
		usedLabels = new HashSet<Label>();
		root = createTreeHelper(data);
		
	}

	private TreeNode createTreeHelper(Map<String, int[]> remainingData) {
		
		if(remainingData.size() > data.size() * 0.1) {
			
			int featureIndex = getFeatureWithHighestInformationGain(remainingData);
			TreeNode treeNode = new TreeNode();
			treeNode.featureIndex = featureIndex;
			Map<String, int[]> leftEntries = getEntries(remainingData, featureIndex, 0);
			treeNode.left = createTreeHelper(leftEntries);
			Map<String, int[]> rightEntries = getEntries(remainingData, featureIndex, 1);
			treeNode.right = createTreeHelper(rightEntries);
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

	private int getFeatureWithHighestInformationGain(Map<String, int[]> remainingData) {
		
		return 0;
	}
	
	public Map<String, Label> identifyLabel(Map<String, int[]> queries) {
		
		TreeNode tempRoot = root;
		while(tempRoot != null) {
			
		}
		
		return null;
	}
	
	
}
