package edu.asu.mwdb.epidemics.epidemic.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.asu.mwdb.epidemics.domain.Score;
import edu.asu.mwdb.epidemics.domain.SimilarityDomain;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.time_series_search.SimilarityMeasureUtils;
import edu.asu.mwdb.epidemics.time_series_search.TimeSeriesSearch;

/*import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import edu.asu.mwdb.epidemics.task.Task1;*/

public class LDASVDWithQuery {

	LinkedHashMap<Window,Integer> wordCountQuery;
	LinkedHashMap<Window,Integer> wordCountAll;
	private static List<String> fileNameList;

	//Computing the word count matrix for the query with respect to the epidemic word files.
	private void createWordCountMatrix() throws IOException{
		wordCountQuery = new LinkedHashMap<>();
		wordCountAll = new LinkedHashMap<>();
		//Create Map for QueryFile
		BufferedReader br = new BufferedReader(new FileReader(new File("query dictionary/epidemic_word_file.csv")));
		String line = "";
		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if(wordCountQuery.containsKey(word.getWindow())){
				int updatedCount = wordCountQuery.get(word.getWindow());
				wordCountQuery.put(word.getWindow(), ++updatedCount);
			}
			else {
				wordCountQuery.put(word.getWindow(), 1);
			}
		}
		br.close();

		//Create Map for All Epidemic Simulations Files
		br = new BufferedReader((new FileReader(new File("simulation dictionary/epidemic_word_file.csv"))));
		line = "";
		while((line = br.readLine()) != null){
			Word word = new Word(line);
			if(wordCountAll.containsKey(word.getWindow())){
				int updatedCount = wordCountAll.get(word.getWindow());
				wordCountAll.put(word.getWindow(), ++updatedCount);
			}
			else {
				wordCountAll.put(word.getWindow(), 1);
			}
		}
		br.close();

		//Creating nX1 matrix
		for(Entry<Window, Integer> entry : wordCountAll.entrySet()){
			Window window = entry.getKey();
			if(wordCountQuery.containsKey(window)){
				wordCountAll.put(window, wordCountQuery.get(window));
			}
			else {
				wordCountAll.put(window, 0);
			}
		}
	}

	//Write the Map into a file.
	private void writeMatrix() throws IOException{
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter("Data/QUERY.csv"));
		StringBuffer line = new StringBuffer();

		for(Entry<Window, Integer> entry : wordCountAll.entrySet()){
			Window window = entry.getKey();
			line.append(wordCountAll.get(window));
			line.append(System.getProperty("line.separator"));
		}
		bufWriter.write(line.deleteCharAt(line.length() - 1).toString());
		bufWriter.newLine();
		line.setLength(0);
		bufWriter.close();
	}

	// Write matrix function for LDA query;	
	private void writeMatrixLDAQuery() throws IOException{
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter("Data/LDAInputMatrix.csv",true));
		StringBuffer line = new StringBuffer();

		for(Entry<Window, Integer> entry : wordCountAll.entrySet()){
			Window window = entry.getKey();
			line.append(wordCountAll.get(window) + ",");
		}
		bufWriter.write(line.deleteCharAt(line.length() - 1).toString());
		line.setLength(0);
		bufWriter.close();
	}

	//List the number of files in the folder.
	private void updateListFilesInFolder(File folder) {
		List<String> fileNames = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				updateListFilesInFolder(fileEntry);
			} else {
				fileNames.add(folder.getName() + "/" + fileEntry.getName());
			}
		}
		fileNameList = fileNames;   
	}

	/* 
	 * r - R Semantics
	 * topK - Top K similar files to Query
	 * option - 3a/3b/3c
	 * directory - Epidemic simulations file directory
	 * similarityMeasure - 1-8*/
	public void execute3D(String queryFile,int r, int topK, String option, String directory, int similarityMeasure) throws Exception{
		switch(option){
		case "3a":
			createWordCountMatrix();
			writeMatrix();
			SVD s = new SVD();
			updateListFilesInFolder(new File(directory));
			s.createInputMatrixToFile("simulation dictionary/epidemic_word_file.csv");
			System.out.println("Matrix file created !!");
			s.svDecomposition(r,true);
			System.out.println("SVD matrices created!!");
			s.createLatentSemanticScoreFile("Data/SimilarityResults.csv","Data/SemanticScore.csv",fileNameList, topK);
			System.out.println("Score file created !!!");
			break;
		case "3b":

			LDA l = new LDA();
			updateListFilesInFolder(new File(directory));
			l.createInputMatrixToFile("simulation dictionary/epidemic_word_file.csv");
			System.out.println("LDA Data Matrix file created !!");
			createWordCountMatrix();
			writeMatrixLDAQuery();
			System.out.println("Appending query Matrix with Data matrix file created !!");
			l.LDADecomposition(r,true);
			System.out.println("LDA with query matrices created!!");
			
			getKSimilarFiles("Data/LDAQuerySimilarity.csv",topK);
			//This is for query matrix.
//			createWordCountMatrix();
//			writeMatrixLDAQuery();
//			System.out.println("LDA with query Matrix file created !!");
//			l.LDADecomposition(5,true);
//			System.out.println("LDA with query matrices created!!");
			break;
		case "3c":
			SimSimSimilarityAnalysis task3c = new SimSimSimilarityAnalysis();
			TimeSeriesSearch timeSeriesSearch = new TimeSeriesSearch();
			updateListFilesInFolder(new File(directory));
			timeSeriesSearch.getKSimilarSimulations(queryFile, directory, fileNameList.size(), SimilarityMeasureUtils.getSimilarityMeasure(similarityMeasure));
			//Write List to File
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter("Data/QUERY.csv"));
			StringBuffer line = new StringBuffer();
			for(SimilarityDomain simObj : timeSeriesSearch.simDomainList){				
				line.append(simObj.getSimilarity());
				line.append(System.getProperty("line.separator"));
			}
			bufWriter.write(line.deleteCharAt(line.length() - 1).toString());
			bufWriter.newLine();
			line.setLength(0);
			bufWriter.close();
			task3c.executeLODUTask3C(directory, r, similarityMeasure, topK, true, "Data/SimilarityResults.csv");
			break;
		default:
			break;
		}

	}
	/**
	 * @param topK
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	private void getKSimilarFiles(String inFile, int topK) throws NumberFormatException, IOException {
		BufferedReader bufReader = new BufferedReader(new FileReader(new File(inFile)));
		List<Score> resultList = new ArrayList<>();
		String line = "";
		int rowIndex = 0;
		while ((line = bufReader.readLine()) != null) {
			Score s = new Score(fileNameList.get(rowIndex),Double.parseDouble(line));
			resultList.add(s);
			rowIndex++;
		}
		bufReader.close();		
		Collections.sort(resultList);
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter("Data/LDASimilarityResults.csv"));
		for(int i=0;i<topK;i++) {
			bufWriter.write(resultList.get(i).toString());
			bufWriter.newLine();
		}
		bufWriter.close();
//		System.out.println(topK + " similar files to Query file:");
//		for(int i=0;i<topK;i++){
//			System.out.println(resultList.get(i));
//		}
	}

//	public static void main(String[] args) {
//		Task3d task3D = new Task3d();		
//		String directory = "InputCSVs";
//		int rSemantics = Integer.parseInt("4");
//		int topK = Integer.parseInt("5");
//		String choice = "3c";
//		int similarityMeasure = Integer.parseInt("1");
//		
//		/*String[] arg = {"InputCSVs","5","3","10"};
//		try {
//			Task1.main(arg);
//		} catch (IOException | InterruptedException | MatlabConnectionException
//				| MatlabInvocationException e) {
//			e.printStackTrace();
//		}*/
//
//		try {			
//			task3D.execute3D(rSemantics,topK,choice, directory, similarityMeasure);			
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//	}

}
