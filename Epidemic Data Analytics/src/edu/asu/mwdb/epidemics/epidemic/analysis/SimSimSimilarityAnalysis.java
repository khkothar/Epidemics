package edu.asu.mwdb.epidemics.epidemic.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.mwdb.epidemics.similarity.AverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.DifferenceWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;
import edu.asu.mwdb.epidemics.similarity.WeightedAverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedDiffWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WordSimilarity;

/**
 * @author Mohan-Thorat
 *
 */
public class SimSimSimilarityAnalysis {

	private static List<String> fileNameList;
	
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
	
	//Create a Similarity Similarity File matrix.
	public float[][] createSimSimMatrix(String directoryPath, int simMeasure) throws Exception {
		updateListFilesInFolder(new File(directoryPath));
		/*for(String str : fileNameList) {
			System.out.println(" " + str);
		}*/
		int numOfFile = fileNameList.size();
		float[][] simSimMatrix = new float[numOfFile][numOfFile];
		Similarity similarity = null;
		
		switch(simMeasure) {
		case 1: 
			similarity = new EuclideanSimilarity();
			break;
		case 2:
			similarity = new DTWSimilarity();
			break;
		case 3: 
			similarity = new WordSimilarity();
			break;
		case 4:
			similarity = new AverageWordSimilarity();
			break;
		case 5: 
			similarity = new DifferenceWordSimilarity();
			break;
		case 6:
			similarity = new WeightedWordSimilarity();
			break;
		case 7: 
			similarity = new WeightedAverageWordSimilarity();
			break;
		case 8:
			similarity = new WeightedDiffWordSimilarity();
			break;
		}
		
		for(int i =0 ;i < numOfFile ; i++) {
			for(int j = i; j < numOfFile ; j++) {
				simSimMatrix[i][j] = similarity.getScore(fileNameList.get(i), fileNameList.get(j));
				simSimMatrix[j][i] = simSimMatrix[i][j];
			}
		}		
		
		return simSimMatrix;	
	}
	
	//Write Similarity Similartiy matrix in the file for input to SVD.
	private void createInputMatrixToFileForSVD(float[][] inputMatrix) throws IOException {
		
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
	
	public void executeLODUTask3C(String directoryPath,int r, int simMeasure, int topK, boolean isQuery, String inputFile) throws Exception {
		float[][] simSimMatrix = createSimSimMatrix(directoryPath, simMeasure);
		createInputMatrixToFileForSVD(simSimMatrix);
		System.out.println("Matrix file created 3C!!! ");
		SVD svd = new SVD();
		svd.svDecomposition(r,isQuery);
		System.out.println("SVD matrices created!!");
		svd.createLatentSemanticScoreFile(inputFile,"Data/SemanticScore3C.csv",fileNameList, topK);
		System.out.println("Score file created !!!");
	}

//	public static void main(String[] args) {
//		Task3C instance = new Task3C();
//		//InputCSV directory.
//		String input1 = "inputCSVs";
//		//r value.
//		int input2 = Integer.parseInt("4");
//		//Similarity Measure
//		int input3 = Integer.parseInt("2");
//		String defaultFile = "Data/U.csv";
//		try {
//			instance.executeLODUTask3C(input1,input2,input3, -1, false, defaultFile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}

}
