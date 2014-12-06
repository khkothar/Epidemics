package edu.asu.mwdb.epidemics.lsh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Window;

public class LSH {
	
	private List<Layer> layerList = null;
	private static GlobalBitVector gVector = null; 
	
	public LSH() {}
	
	public LSH(int noOfLayers, int hashesPerLayer , String wordFileName) {
		
		//Creating  global bit vector.
		gVector = new GlobalBitVector(wordFileName);
		createLayers(noOfLayers, hashesPerLayer);
	}
	
	public List<Layer> getLayerList() {
		return layerList;
	}

	public void setLayerList(List<Layer> layerList) {
		this.layerList = layerList;
	}

	private final void createLayers(int noOfLayers, int hashesPerLayer){
		//Create global bit vector map.
		Map<String, int[]> fileBitVectorMap = gVector.getBitVectorMapForInputFiles();
		Set<Window> uniqueWindowSet = gVector.getUniqueWindows();
		
		List<Layer> layerList = new ArrayList<>();
		
		int i;
		for(i=0; i<noOfLayers ; i++) {
			List<Integer> randomNumList = generateKRandomIndexes(uniqueWindowSet.size(), hashesPerLayer);
			Layer layer = new Layer(randomNumList,fileBitVectorMap);
			layerList.add(layer);
		}
		
		this.setLayerList(layerList);
	}
	
	//Method for generating k random number within the given range.
	private List<Integer> generateKRandomIndexes(int range, int hashesPerLayer) {
		Random rng = new Random();
		Set<Integer> generated = new LinkedHashSet<Integer>();
		List<Integer> randomNumberList = new ArrayList<>();
		
		while (generated.size() < hashesPerLayer) {
			Integer next = rng.nextInt(range - 1) + 1;
			generated.add(next);
		}
		
		for(int randomNumber : generated) {
			randomNumberList.add(randomNumber);
		}
		return randomNumberList;
	}
	
	//Get similar files.
	public Set<String> getTSimilarFiles(String queryFileName, int t) {
		
		Set<String> similarFileResultSet = new HashSet<>();
		int i, accessedIndex = 0;
		int threshold = 5;
		Set<Window> uniqueWindowSet = gVector.getUniqueWindows();
		List<Layer> layerList = this.getLayerList();
		
		QueryGlobalBitVector queryBitVectorObj = new QueryGlobalBitVector(uniqueWindowSet, queryFileName);
		int[] queryBitVector = queryBitVectorObj.getBitVectorForQueryFile();
		
		for(i=0 ; i<threshold ; i++) {
			for(Layer layer : layerList) {
				accessedIndex += layer.getSimilarFilesNames(queryBitVector, t, similarFileResultSet, i, accessedIndex);
				if(similarFileResultSet.size() >= t) {
					break;
				}
			}
			if(similarFileResultSet.size() >= t) {
				break;
			}
		}
		System.out.println("Accessed indices : " + accessedIndex);
		
		return similarFileResultSet;
	}
	
	public long getIndexStructureSize() {
		long indexStructureSize = 0;
		List<Layer> layers = this.getLayerList();
		
		for(Layer layer : layers) {
			indexStructureSize += layer.getSize();
		}
		
		return indexStructureSize;
	}
	
	public static void main(String[] args) {
		LSH lshObj = new LSH(10, 10, "epidemic_word_file.csv");
		System.out.println("Index structure size(in bytes): " + lshObj.getIndexStructureSize());
		int t = 5;
		
		Set<String> list = lshObj.getTSimilarFiles("queryFile_10.csv", t);
		if(list == null || list.size() == 0) {
			System.out.println("Unable to find any serach results after completing iterations for threshold!!");
		} else {
			if (list.size() < t) {
				System.out.println("Unable to find " + t + " number of results. Printing the found results.");
			} else {
				System.out.println("Printing top " + t + " results.");
			}
			for(String str : list) {
				System.out.print(str + " ");
			}
		}
	}
}
