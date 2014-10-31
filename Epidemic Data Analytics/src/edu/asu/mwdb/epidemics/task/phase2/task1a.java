package edu.asu.mwdb.epidemics.task.phase2;

import java.text.DecimalFormat;

import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;

public class task1a {
	public static void main(String args[]) throws Exception{
		Similarity sim = new DTWSimilarity();
		String fileName1 = "F:\\Study\\Acads\\ASU\\Fall_2014\\MWDB\\Project\\Phase_2\\sampleData\\SampleData_P2\\SampleData_P2\\Set_of_Simulation_Files\\1.csv";
		String fileName2 = "F:\\Study\\Acads\\ASU\\Fall_2014\\MWDB\\Project\\Phase_2\\sampleData\\SampleData_P2\\SampleData_P2\\Set_of_Simulation_Files\\5.csv";
		float similarity  = sim.getScore(fileName1, fileName2);
		DecimalFormat df = new DecimalFormat("#.#########");
		System.out.println("Euclidean Similarity is : "+ df.format(similarity));
	}
}
