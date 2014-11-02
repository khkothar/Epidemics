/**
 * 
 */
package edu.asu.mwdb.epidemics.epidemic.analysis;

import edu.asu.mwdb.epidemics.domain.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
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
			fileNameAccessList.add(fileName.trim());
		}
	}
	
	//Creates input matrix of as epidemic simulation files as Object and words as features 
	// This input matrix is passed to SVD matlab function for Eigen decomposition.
	public int[][] getInputMatrix(String fileName) throws Exception {
		Map<Window,List<Id>> wordFileMap = getEpidemicWordMap(fileName);
		int[][] inputMatrix = new int[fileNameSet.size()][wordFileMap.size()];
		int columnIndex = 0;
		int rowIndex;
		
		//Updating the file access list used for filling up the 2d input array. 
		updateFileNameAccessList();
		
		for(Entry<Window, List<Id>> entry : wordFileMap.entrySet()) {
			Window window = entry.getKey();
			List<Id> idList = entry.getValue();
			windowAccessList.add(window);
			
			for(Id id : idList) {
				//Updating the count matrix column wise using the every word and index of fileName in fileNameAccessList.
				rowIndex = fileNameAccessList.indexOf(id.getFileName());
				if(rowIndex == -1) {
					throw new Exception("fileNameAccessList does not contain fileName !!");
				}
				inputMatrix[rowIndex][columnIndex] += 1;
			}
			columnIndex++;
		}
		
		return inputMatrix;
	}
	
	//Creating matrix file for providing input to MATLAB SVD function.
	public void createInputMatrixToFile(String inFileName) throws Exception {
		int[][] inputMatrix = getInputMatrix(inFileName);
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter("Data/X.csv"));
		StringBuffer line = new StringBuffer();
		
		for (int i = 0; i < inputMatrix.length; i++) {
			for (int j = 0; j < inputMatrix[i].length; j++) {
				line.append(inputMatrix[i][j]);
				line.append(",");
			}
			bufWriter.write(line.deleteCharAt(line.length() - 1).toString());
			bufWriter.newLine();
			line.setLength(0);
		}
		
		bufWriter.close();
	}
	
	public void svDecomposition(int r, boolean isQuery) throws MatlabInvocationException, MatlabConnectionException{
		//Create a proxy, which we will use to control MATLAB
		 MatlabProxyFactory factory = new MatlabProxyFactory();
		 MatlabProxy proxy = factory.getProxy();
		 proxy.eval("[U,S,V] = svDecomposition(" + r + "," + isQuery + ")");
		 proxy.disconnect();
	}
	
	// Creating list of Score objects in decreasing order of score.
	private List<List<Score>> getLatentSemanticSimulationScores(String inFile, List<String> fileNameList) throws Exception {
		
		BufferedReader bufReader = new BufferedReader(new FileReader(new File(inFile)));
		List<List<Score>> resultList = new ArrayList<>();
		List<Score> tempList;
		String line = bufReader.readLine();
		int rowIndex = 0;
		String[] arrayLine = line.split(","); 
		
		for(int i =0; i < arrayLine.length ; i++) {
			List<Score> list = new ArrayList<>();
			Score s = new Score(fileNameList.get(rowIndex),Double.parseDouble(arrayLine[i]));
			list.add(s);
			resultList.add(list);
		}
		rowIndex++;
		while ((line = bufReader.readLine()) != null) {
			String[] tempArray = line.split(",");
			for (int i = 0; i < tempArray.length; i++) {
				tempList = resultList.remove(i);
				Score s = new Score(fileNameList.get(rowIndex),Double.parseDouble(tempArray[i]));
				tempList.add(s);
				resultList.add(i, tempList);
			}
			rowIndex++;
		}
		bufReader.close();
		
		for(List<Score> list : resultList) {
			Collections.sort(list);
		}
		
		return resultList;
	}
	
	//Writing to Latent Semantic Score file.
	public void createLatentSemanticScoreFile(String inFile, String outFile, List<String> fileNameList, int noOfLines) throws Exception {
		
		List<List<Score>> outList = getLatentSemanticSimulationScores(inFile, fileNameList);
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter(outFile));
		int semanticIndex = 1;
		int line = 0;
		for (List<Score> list : outList) {			
			bufWriter.write("Latent Semantic : " +  semanticIndex++);
			bufWriter.newLine();
			for (Score s : list) {
				bufWriter.write(s.toString());
				bufWriter.newLine();
				++line;
				if(noOfLines!=-1 && (line>=noOfLines)) {
					break;
				}
			}
			bufWriter.newLine();			
		}
		bufWriter.close();
		
	}
	
	public static void main(String[] args) {
		SVD s = new SVD();
		
		try {
			s.createInputMatrixToFile("epidemic_word_file.csv");
			System.out.println("Matrix file created !!");
			s.svDecomposition(5,false);
			System.out.println("SVD matrices created!!");
			s.createLatentSemanticScoreFile("Data/U.csv","Data/SVDSemanticScore.csv",fileNameAccessList, -1);
			System.out.println("Score file created !!!");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}