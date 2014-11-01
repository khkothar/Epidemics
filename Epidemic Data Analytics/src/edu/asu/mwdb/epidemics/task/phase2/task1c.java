package edu.asu.mwdb.epidemics.task.phase2;

import java.text.DecimalFormat;

import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.Similarity;

public class task1c {
	public static void main(String args[]) throws Exception{
		Similarity sim = new DTWSimilarity();
		String file1 = args[0];//"F:\\Study\\Acads\\ASU\\Fall_2014\\MWDB\\Project\\Phase_2\\sampleData\\SampleData_P2\\SampleData_P2\\Set_of_Simulation_Files\\1.csv";
		String file2 = args[1];//"F:\\Study\\Acads\\ASU\\Fall_2014\\MWDB\\Project\\Phase_2\\sampleData\\SampleData_P2\\SampleData_P2\\Set_of_Simulation_Files\\5.csv";
		float similarity  = sim.getScore(file1, file2);
		DecimalFormat df = new DecimalFormat("#.#########");
		System.out.println("Euclidean Similarity is : "+ df.format(similarity));
	}
}
