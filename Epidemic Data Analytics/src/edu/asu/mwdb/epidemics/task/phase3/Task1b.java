package edu.asu.mwdb.epidemics.task.phase3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.asu.mwdb.epidemics.lsh.LSH;

public class Task1b {

public static void main(String[] args) {
	boolean wrongArg = false;
	if(args.length != 5)
		wrongArg = true;
	if(wrongArg){
		System.err.println("Please enter correct command line arguments:");
		System.err.println("Usage: Task3a <noOfLayers> <noOfHashesh> <Epidemic word file> <Qurey word File> <t>");
		System.exit(0);
	}
		int noOfLayers = Integer.parseInt(args[0]);
		int noOfHashes = Integer.parseInt(args[1]);
		String inputEpidemicWordFileName = args[2];
		String queryEpidemicWordFileName = args[3];
		int t = Integer.parseInt(args[4]);
		
		LSH lshObj = new LSH(noOfLayers, noOfHashes, inputEpidemicWordFileName);
		System.out.println("Index structure size(in bytes): " + lshObj.getIndexStructureSize());
		
		List<String> results = new ArrayList<>();
		
		Set<String> list = lshObj.getTSimilarFiles(queryEpidemicWordFileName, t, results);
		if(list == null || list.size() == 0) {
			System.out.println("Unable to find any serach results after completing iterations for threshold!!");
			System.out.println("Number of Unique Vectors considered: 0");
		} else {
			if (list.size() < t) {
				System.out.println("Unable to find " + t + " number of results. Printing the " + list.size() + " results.");
				for(String str : list) {
					System.out.print(str + " ");
				}
				System.out.println("Number of Unique Vectors considered: " + list.size());
				System.out.println("Number of Overall Vectors considered: " + results.size());
				System.out.println("Number of Overall Vectors considered: " + results.size()*2);
			} else {
				System.out.println("Printing top " + t + " results.");
				for(String str : list) {
					System.out.print(str + " ");
				}
				System.out.println("Number of Unique Vectors considered: " + list.size());
				System.out.println("Number of Overall Vectors considered: " + results.size());
				System.out.println("Number of Overall Vectors considered: " + results.size()*2);
			}
		}
	}

}
