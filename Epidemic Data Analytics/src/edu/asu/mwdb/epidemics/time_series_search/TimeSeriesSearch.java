package edu.asu.mwdb.epidemics.time_series_search;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;








import edu.asu.mwdb.epidemics.domain.SimilarityDomain;

public class TimeSeriesSearch {
	public List<SimilarityDomain> simDomainList;
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
				//float sim = getSimilarity(f1.getName(), listOfFiles[i].getName(), measure);
				float sim = SimilarityMeasureUtils.getSimilarity(measure).getScore(listOfFiles[i].getAbsolutePath(),f1.getAbsolutePath());
				System.out.println("Similarity between queryFile and " + listOfFiles[i].getName()+" is : "+ df.format(sim));
				SimilarityDomain simObj = new SimilarityDomain();
				simObj.setQueryFile(queryFile);
				simObj.setSimFile(listOfFiles[i].getName());
				simObj.setSimilarity(sim);
				priorityQue.add(simObj);
				if(priorityQue.size() > k)
					priorityQue.poll();
			}
			simDomainList = new LinkedList<>();
			for(SimilarityDomain sObj : priorityQue){
				simDomainList.add(sObj);
			}
			SimilarityDomain sim = priorityQue.poll();
			while(sim != null){
				System.out.println("file : "+sim.getSimFile() + " similarity : "+ df.format(sim.getSimilarity()));
				kSimilarSims.add(sim.getSimFile());
				sim = priorityQue.poll();
			}
			return kSimilarSims;
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
