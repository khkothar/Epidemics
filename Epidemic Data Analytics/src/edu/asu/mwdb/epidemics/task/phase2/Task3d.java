package edu.asu.mwdb.epidemics.task.phase2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.epidemic.analysis.SVD;

/*import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import edu.asu.mwdb.epidemics.task.Task1;*/

public class Task3d {

	LinkedHashMap<Window,Integer> wordCountQuery;
	LinkedHashMap<Window,Integer> wordCountAll;
	private static List<String> fileNameList;

	//Computing the word count matrix for the query with respect to the epidemic word files.
	private void createWordCountMatrix() throws IOException{
		wordCountQuery = new LinkedHashMap<>();
		wordCountAll = new LinkedHashMap<>();
		//Create Map for QueryFile
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"query dictionary/epidemic_word_file.csv")));
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

	private void execute3D(int r, String option, String directory) throws Exception{
		switch(option){
		case "3a":
			SVD s = new SVD();
			updateListFilesInFolder(new File(directory));
			s.createInputMatrixToFile("epidemic_word_file.csv");
			System.out.println("Matrix file created !!");
			s.svDecomposition(r,true);
			System.out.println("SVD matrices created!!");
			s.createLatentSemanticScoreFile("Data/SemanticScore.csv",fileNameList);
			System.out.println("Score file created !!!");
			break;
		case "3b":
			break;
		case "3c":
			break;
		default:
			break;
		}

	}
	public static void main(String[] args) {
		Task3d task3D = new Task3d();

		
		String input1 = "";
		int input2 = Integer.parseInt("5");
		int topK = Integer.parseInt("15");
		String input3 = "";
		
		/*String[] arg = {"InputCSVs","5","3","10"};
		try {
			Task1.main(arg);
		} catch (IOException | InterruptedException | MatlabConnectionException
				| MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		try {
			String directory = "InputCSVs";
			task3D.createWordCountMatrix();
			task3D.writeMatrix();
			task3D.execute3D(5, "3a", directory);			

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
