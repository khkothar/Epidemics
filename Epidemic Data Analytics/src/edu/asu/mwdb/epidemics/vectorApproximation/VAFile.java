package edu.asu.mwdb.epidemics.vectorApproximation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class VAFile {
	
	private int bitsPerDimension;
	private Map<Integer, BitSet> rangeMap;
	private int min;
	private int max; 
	private Map<String, int[]> originalVectors;
	private Map<String, BitSet> vaIndex;
	private int bitsetSize;
	private int numberOfCompressedVectorsRequired;
	
	
	public VAFile(int b, Map<String, int[]> originalVectors) {		
		this.bitsPerDimension = b;
		this.originalVectors = originalVectors;
		rangeMap = new HashMap<Integer, BitSet>();
		vaIndex = new HashMap<String, BitSet>();
		setMinMax(originalVectors);
		createRangeMap();
		createIndex();
	}


	private void createIndex() {
		if( originalVectors != null ) {
			for(Map.Entry<String, int[]> entry : originalVectors.entrySet()) {
				vaIndex.put(entry.getKey(), getCompressedKey(entry.getValue()));
			}
		}
	}

	private BitSet getPartitionBits(int field) {

		for (Map.Entry<Integer, BitSet> entry : rangeMap.entrySet()) {

			if (entry.getKey() >= field)
				return entry.getValue();
		}

		return null;
	}

	private BitSet getCompressedKey(int[] value) {

		BitSet vectorKey = new BitSet();
		int fieldCnt = 0;
		bitsetSize = value.length * bitsPerDimension;
		for (int field : value) {

			BitSet partitionBits = getPartitionBits(field);
	
			for (int i = 0; i < bitsPerDimension; i++) {
				if (partitionBits.get(i)) {
					vectorKey.set(bitsPerDimension * fieldCnt + i);
				}
			}

			fieldCnt++;
		}
		
		return vectorKey;
	}


	private void createRangeMap() {
		int range = max - min + 1;
		int factor = (short) Math.pow(2, bitsPerDimension);
		short partitionLen = (short) (range / factor);

		for (short i = 0; i < factor - 1; i++) {
			BitSet bitSet = getBitSet(i);
			rangeMap.put(min + partitionLen
					* (i + 1), bitSet);
		}
		rangeMap.put(max, getBitSet(factor - 1));
	}

	private BitSet getBitSet(int j) {
		BitSet bitSet = new BitSet(bitsPerDimension);

		for (int i = 0; i < bitsPerDimension; i++) {
			if ((j & (1 << i)) != 0) {
				bitSet.set(i);
			} else
				bitSet.set(i, false);
		}
		return bitSet;
	}

	private void setMinMax(Map<String, int[]> originalVectors) {
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
		if( originalVectors != null ) {
			for(Map.Entry<String, int[]> entry : originalVectors.entrySet()) {
				for(int value : entry.getValue()) {

					if(value < min) {
						min = value;
					}
					
					if(value > max) {
						max = value;
					}
				}
			}
		}
	}
	
	public int getIndexSize() {
		return (int) Math.ceil((vaIndex.size() * bitsetSize)/8.0);
	}

	public List<String> getSimilarFiles(int[] query, int k) {
		
		List<String> possibleCandidates = getResultsWithFalsePositives(query, k);
		numberOfCompressedVectorsRequired = possibleCandidates.size();
		PriorityQueue<FileNameWrapper> priorityQueue = new PriorityQueue<FileNameWrapper>(k, new FileNameComparator());
		
		for(String fileName : possibleCandidates) {
			priorityQueue.add(new FileNameWrapper(fileName, getActualDistace(query, fileName)));
			if(priorityQueue.size() > k) {
				priorityQueue.poll();
			}
		}
		
		List<String> finalResults = new ArrayList<String>();
		
		for(int i = 0; i < k; i++) {
			finalResults.add(priorityQueue.poll().getFileName());
		}
		
		return finalResults;
	}
	
	public int getNumberOfCompressedVectorsRequired() {
		return numberOfCompressedVectorsRequired;
	}


	private double getActualDistace(int[] queryVector, String fileName) {
		int[] originalVector = originalVectors.get(fileName);
		int squareSum = 0;
		for(int i = 0; i < queryVector.length; i++) {
			squareSum += Math.pow(queryVector[i] - originalVector[i], 2);
		}
		return Math.sqrt(squareSum);
	}


	private List<String> getResultsWithFalsePositives(int[] query, int k) {
		// TODO Auto-generated method stub
		TreeMap<Double,List<String>> distanceMap=new TreeMap<Double,List<String>>();
		int currentSize=0;
		BitSet targetKey = getCompressedKey(query);
		for(Entry<String, BitSet> entry : vaIndex.entrySet()) {
			double distance=getUpperBound(query, targetKey, entry.getValue());

			if(currentSize<k||k==0){
				addEntry(distanceMap, entry, distance);
				currentSize++;
			} else if (distance == distanceMap.lastKey()) {
				distanceMap.get(distance).add(entry.getKey());
				currentSize++;
			} else if (distance < distanceMap.lastKey()) {
				int lastValueSize=distanceMap.lastEntry().getValue().size();
				if((currentSize-lastValueSize)>=k-1){
					distanceMap.remove(distanceMap.lastKey());
					addEntry(distanceMap, entry, distance);
					currentSize-=lastValueSize;
				}else{
					addEntry(distanceMap, entry, distance);
				}
				currentSize++;
			}
		}
		
		List<String> possibleCandidates = new ArrayList<String>();
		
		for(Map.Entry<Double, List<String>> entry : distanceMap.entrySet()) {
			possibleCandidates.addAll(entry.getValue());
		}
		
		return possibleCandidates;
	}


	private void addEntry(TreeMap<Double, List<String>> distanceMap,
			Entry<String, BitSet> entry, double distance) {
		List<String> value;
		if(distanceMap.containsKey(distance)) {
			value = distanceMap.get(distance);
			value.add(entry.getKey());
		} else {
			value = new ArrayList<String>();
			value.add(entry.getKey());
			distanceMap.put(distance, value);
		}
	}


	public double getUpperBound(int[] target, BitSet targetCompressedVector, 
			BitSet currentCompressedVector){

		float upperBound=0;

		for(int i = 0; i < target.length; i++){
			
			BitSet targetBitSet=targetCompressedVector.get(i*bitsPerDimension, (i+1)*bitsPerDimension);
			int targetRegionIndex= getIntValue(targetBitSet)+1;
			
			BitSet currentBitSet=currentCompressedVector.get(i*bitsPerDimension, (i+1)*bitsPerDimension);
			short currentRegionIndex=(short) (getIntValue(currentBitSet)+1);
			
			if(currentRegionIndex<targetRegionIndex){
				upperBound+=Math.pow((short)target[i] - getBoundaryValue(currentRegionIndex-1),2);
			}else if(currentRegionIndex>targetRegionIndex){
				//System.out.println("================="+i);
				upperBound += Math.pow(getBoundaryValue(currentRegionIndex+1) - target[i],2);
			}
		}
		return (float) Math.sqrt(upperBound);
	}
	
	private int getBoundaryValue(int index) {
		if(index==1)
			return min;
		else {
				int i=1;
				for(Map.Entry<Integer,BitSet> entry:rangeMap.entrySet()){
					if(i==index-1)
						return entry.getKey();
					i++;
				}
		}
		return 0;
	}

	
	private int getIntValue(BitSet fieldBitSet) {
		int sum=0;
		for(short i=0;i<bitsPerDimension;i++){
			if(fieldBitSet.get(i)){
				sum+=Math.pow(2, i);
			}
		}
		return sum;
	}
	
	private class FileNameWrapper {
		private String fileName;
		private double distance;
		public FileNameWrapper(String filename, double distance) {
			this.fileName = filename;
			this.distance = distance;
		}
		
		public String getFileName() {
			return fileName;
		}
		
		public double getDistance() {
			return distance;
		}
		
	}
	
	private class FileNameComparator implements Comparator<FileNameWrapper> {

		@Override
		public int compare(FileNameWrapper o1, FileNameWrapper o2) {
			if(o1.getDistance() < o2.getDistance()) {
				return 1;
			} else {
				return 0;
			}
		}
		
	}
}
