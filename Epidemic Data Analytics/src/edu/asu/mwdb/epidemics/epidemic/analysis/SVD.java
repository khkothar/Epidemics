/**
 * 
 */
package edu.asu.mwdb.epidemics.epidemic.analysis;

import edu.asu.mwdb.epidemics.domain.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Word;

public class SVD {

	private static Set<String> fileNameSet = new HashSet<>();
	private static List<Window> windowAccessList = new ArrayList<>();
	private static List<String> fileNameAccessList = new ArrayList<>();
	
	//Function for generating the Map from given epidemic word file.
	private Map<Window, List<Id>> getEpidemicWordMap(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		String line = "";
		Map<Window,List<Id>> wordFileMap = new HashMap<>();
		List<Id> tempList;

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			fileNameSet.add(word.getId().getFileName());
			if(wordFileMap.containsKey(word.getWindow())) {
				tempList = wordFileMap.get(word.getWindow());
				tempList.add(word.getId());
				wordFileMap.put(word.getWindow(), tempList);
			} else {
				List<Id> list = new ArrayList<>();
				list.add(word.getId());
				wordFileMap.put(word.getWindow(), list);
			}
		}
		br.close();
		return wordFileMap;
	}
	
	//Updating the fileAccessList required for index lookup of fileNames.
	private void updateFileNameAccessList() throws Exception {
		if(fileNameSet.size() == 0) {
			throw new Exception("File name set is Empty!!!");
		}
		for(String fileName : fileNameSet) {
			fileNameAccessList.add(fileName);
		}
	}
	
	public int[][] getInputMatrix(String fileName) throws Exception {
		Map<Window,List<Id>> wordFileMap = getEpidemicWordMap(fileName);
		int[][] inputMatrix = new int[fileNameSet.size()][wordFileMap.size()];
		
		//Updating the file access list used for filling up the 2d input array. 
		updateFileNameAccessList();
		
		for(Entry<Window, List<Id>> entry : wordFileMap.entrySet()) {
			Window window = entry.getKey();
			List<Id> idList = entry.getValue();
			windowAccessList.add(window);
			int columnIndex = 0;
			int rowIndex;
			
			for(Id id : idList) {
				//Updating the count matrix column wise using the every word and index of fileName in fileNameAccessList.
				rowIndex = fileNameAccessList.indexOf(id.getFileName());
				if(rowIndex == -1) {
					throw new Exception("fileNameAccessList does not contain fileName !!");
				}
				inputMatrix[rowIndex][columnIndex] += 1;
			}
		}
		return inputMatrix;
	}
	
}
