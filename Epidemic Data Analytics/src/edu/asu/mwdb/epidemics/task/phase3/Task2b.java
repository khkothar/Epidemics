package edu.asu.mwdb.epidemics.task.phase3;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.lsh.GlobalBitVector;
import edu.asu.mwdb.epidemics.lsh.QueryGlobalBitVector;
import edu.asu.mwdb.epidemics.vectorApproximation.VAFile;

public class Task2b {

	public static void main(String[] args) throws IOException {
		
		boolean wrongArg = false;
		
		if(args.length != 4)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task2b <t top results> <Query Word File> <Simulation Word File> <bits per dimension>");
			System.exit(0);
		}
		
		int t = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[3]);
		GlobalBitVector globalBitVector = new GlobalBitVector(args[2]);
		Set<Window> uniqueWords = globalBitVector.getUniqueWindows();
		QueryGlobalBitVector queryGlobalBitVector = new QueryGlobalBitVector(uniqueWords, args[1]);
		
		int[] countVector = queryGlobalBitVector.getCountVectorForQueryFile();
		
		VAFile vaFile = new VAFile(b, globalBitVector.getCountVectorMapForInputFiles());
		List<String> results = vaFile.getSimilarFiles(countVector, t);
		
		System.out.println("Results : " + results);
		System.out.println("Bytes Read from Index :" + vaFile.getIndexSize());
		System.out.println("the number of compressed vectors needed to expand to answer the query : " + vaFile.getNumberOfCompressedVectorsRequired());
		
	}

}
