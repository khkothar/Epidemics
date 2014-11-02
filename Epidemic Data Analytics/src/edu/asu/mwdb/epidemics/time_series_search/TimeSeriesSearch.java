package edu.asu.mwdb.epidemics.time_series_search;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


//import edu.asu.mwdb.epidemics.domain.SimilarityDomain;
import edu.asu.mwdb.epidemics.similarity.AverageWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.DTWSimilarity;
import edu.asu.mwdb.epidemics.similarity.DifferenceWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.EuclideanSimilarity;
import edu.asu.mwdb.epidemics.similarity.WeightedWordSimilarity;
import edu.asu.mwdb.epidemics.similarity.WordSimilarity;

public class TimeSeriesSearch {
	/**
	 * 
	 * @param queryFile
	 * @param simulationDir
	 * @param k
	 * @param measure
	 * @return
	 * @throws Exception
	 * Returns the list of k similar simulations
	 */
		public List<String> getKSimilarSimulations(String queryFile, String simulationDir, int k, SimilarityMeasure measure) throws Exception{
			ValueComparator comparator = new ValueComparator();
			PriorityQueue<SimilarityDomain> priorityQue = new PriorityQueue<SimilarityDomain>(k, comparator);
			DecimalFormat df = new DecimalFormat("#.#########");
			File folder = new File(simulationDir);
			File[] listOfFiles = folder.listFiles();
			List<String> kSimilarSims = new ArrayList<String>();
			for(int i = 0; i < listOfFiles.length; i++){
				File f1 = new File(queryFile);
				//File f2 = new File(listOfFiles[i].getAbsolutePath());
				float sim = getSimilarity(f1.getName(), listOfFiles[i].getName(), measure);
				System.out.println("Similarity between queryFile and " + listOfFiles[i].getName()+" is : "+ df.format(sim));
				SimilarityDomain simObj = new SimilarityDomain();
				simObj.setQueryFile(queryFile);
				simObj.setSimFile(listOfFiles[i].getName());
				simObj.setSimilarity(sim);
				priorityQue.add(simObj);
				if(priorityQue.size() > k)
					priorityQue.poll();
			}
			SimilarityDomain sim = priorityQue.poll();
			while(sim != null){
				System.out.println("file : "+sim.getSimFile() + " similarity : "+ df.format(sim.getSimilarity()));
				kSimilarSims.add(sim.getSimFile());
				sim = priorityQue.poll();
			}
			return kSimilarSims;
		}
/**
 * 
 * @param queryFile
 * @param simFile
 * @param measure
 * @return
 * @throws Exception
 * returns the Similarity measure as float
 */
		private float getSimilarity(String queryFile, String simFile,
				SimilarityMeasure measure) throws Exception {
			switch(measure.getType()){
			case 1:
				return new EuclideanSimilarity().getScore(queryFile, simFile);
			case 2:
				return new DTWSimilarity().getScore(queryFile, simFile);
			case 3:
				return new WordSimilarity().getScore(queryFile, simFile);
			case 4:
				return new AverageWordSimilarity().getScore(queryFile, simFile);
			case 5:
				return new DifferenceWordSimilarity().getScore(queryFile, simFile);
			case 6:
				return new WeightedWordSimilarity().getScore(queryFile, simFile);
			default:
				return 0;
			}
		}
		
		private class SimilarityDomain {
			private String queryFile;
			private String simFile;
			private float similarity;
			/**
			 * @return the queryFile
			 */
			public String getQueryFile() {
				return queryFile;
			}
			/**
			 * @param queryFile the queryFile to set
			 */
			public void setQueryFile(String queryFile) {
				this.queryFile = queryFile;
			}
			/**
			 * @return the simFile
			 */
			public String getSimFile() {
				return simFile;
			}
			/**
			 * @param simFile the simFile to set
			 */
			public void setSimFile(String simFile) {
				this.simFile = simFile;
			}
			/**
			 * @return the similarity
			 */
			public float getSimilarity() {
				return similarity;
			}
			/**
			 * @param similarity the similarity to set
			 */
			public void setSimilarity(float similarity) {
				this.similarity = similarity;
			}
			
		}
		
		private class ValueComparator implements Comparator<SimilarityDomain> {
		    /**
		     * compare overrides the compare function of Comparator class
		     */
		    public int compare(SimilarityDomain file1, SimilarityDomain file2) {
		        if (file1.getSimilarity() <= file2.getSimilarity()) {
		            return -1;
		        } else {
		            return 1;
		        }
		    }
		}
}
