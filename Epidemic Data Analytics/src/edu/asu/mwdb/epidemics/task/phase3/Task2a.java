package edu.asu.mwdb.epidemics.task.phase3;

import edu.asu.mwdb.epidemics.lsh.GlobalBitVector;
import edu.asu.mwdb.epidemics.vectorApproximation.VAFile;

public class Task2a {
	
	public static void main(String[] args) {
		
		boolean wrongArg = false;
		
		if(args.length != 2)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task2a <bits per dimension> <Simulation Word File>");
			System.exit(0);
		}
		
		int bitsPerDimension = Integer.parseInt(args[0]);
		GlobalBitVector globalBitVector = new GlobalBitVector(args[1]);
		VAFile vaFile = new VAFile(bitsPerDimension, globalBitVector.getCountVectorMapForInputFiles());
		System.out.println("Index Size : " + vaFile.getIndexSize() + " bytes");
	}
}
