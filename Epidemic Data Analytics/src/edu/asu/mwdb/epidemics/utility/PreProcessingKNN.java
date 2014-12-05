package edu.asu.mwdb.epidemics.utility;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.asu.mwdb.epidemics.task.phase2.Task1;

public class PreProcessingKNN {
	LinkedHashMap<String,Float> distMeasure;
	LinkedHashMap<String, Integer> labelCount;
	@SuppressWarnings("rawtypes")
	public LinkedHashMap<String,Float> similarityMeasure(LinkedHashMap<String,String> labelMap, 
			String knownLabeledFiles, int similarityMeasure) throws Exception{
		//Find the best similarity Measure.
		Task1 task1 = new Task1();
		distMeasure = new LinkedHashMap<String,Float>();
		labelCount = new LinkedHashMap<String,Integer>();
		
		Iterator itEntry = labelMap.entrySet().iterator();
		while(itEntry.hasNext()){
			Map.Entry entry = (Map.Entry)itEntry.next();
			String label = (String) entry.getValue();
			String fileName = (String) entry.getKey();
			Float avgDist = (float) 0;
			int count = 0;
			
			Iterator itAnotherEntry = labelMap.entrySet().iterator();
			while(itAnotherEntry.hasNext()){
				Map.Entry anotherEntry = (Map.Entry)itAnotherEntry.next();
				String tempLabel = (String) anotherEntry.getValue();
				String tempKey = (String) anotherEntry.getKey();
				if(label.equals(tempLabel) && !(tempKey.equals(fileName))){
					System.out.print("Label : " + tempLabel);
					avgDist+= task1.driverProgramforTask1(knownLabeledFiles + "\\" + tempKey,knownLabeledFiles + "\\" + fileName, 
							similarityMeasure);
					count++;
				}
			}
			if(distMeasure.containsKey(label)){
				float dist = distMeasure.get(label);
				dist += avgDist;
				distMeasure.put(label, dist);
			}
			else {
				distMeasure.put(label, avgDist);
			}			
			if(labelCount.containsKey(label)){
				int dist = labelCount.get(label);
				dist += count;
				labelCount.put(label, dist);
			}
			else {
				labelCount.put(label, count);
			}
			itEntry.remove();
		}

		for(Map.Entry<String, Float> entry:distMeasure.entrySet()){
			String label = entry.getKey();
			Float avg = entry.getValue();
			distMeasure.put(label, (avg/labelCount.get(label)));
			System.out.println("Label : " + label + "Distances : " + distMeasure.get(label));
		}
		return distMeasure;
	}
}
