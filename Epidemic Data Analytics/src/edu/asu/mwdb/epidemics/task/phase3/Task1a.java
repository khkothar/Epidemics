package edu.asu.mwdb.epidemics.task.phase3;

import java.util.Set;

import edu.asu.mwdb.epidemics.lsh.LSH;

public class Task1a {

	public static void main(String[] args) {
		boolean wrongArg = false;
		if(args.length != 3)
			wrongArg = true;
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: Task1a <noOfLayers> <noOfHashesh> <Epidemic word file>");
			System.exit(0);
		}
		int noOfLayers = Integer.parseInt(args[0]);
		int noOfHashes = Integer.parseInt(args[1]);
		String inputEpidemicWordFileName = args[2];
		
		LSH lshObj = new LSH(noOfLayers, noOfHashes, inputEpidemicWordFileName);
		System.out.println("Index structure size(in bytes): " + lshObj.getIndexStructureSize());
		
	}
}
