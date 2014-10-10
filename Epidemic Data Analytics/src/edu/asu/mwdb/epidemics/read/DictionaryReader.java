package edu.asu.mwdb.epidemics.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.asu.mwdb.epidemics.domain.Word;

public class DictionaryReader {
	
	private Word lowest = null;
	private Word highest = null;
	private long lowestIterationNumber = 0;
	private long highestIterationNumber = 0;
	
	public DictionaryReader(String fileName, String simulationFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		String line = br.readLine();
		double lowestStrength = Double.MAX_VALUE;
		double highestStrength = Double.MIN_VALUE;
		String previousTime = null;
		int count = 1;
		while((line = br.readLine()) != null) {
			Word word = new Word(line);
			if(word.getId().getFileName().equalsIgnoreCase(simulationFile)) {
				if(previousTime != null) {
					if(!previousTime.equals(word.getId().getIteration())) {
						count++;
					}
				}
				double strength = computeStrength(word.getWindow().getValues());
				previousTime = word.getId().getIteration();
				if(strength < lowestStrength) {
					lowest = word;
					lowestStrength = strength;
					lowestIterationNumber = count;
				}
				
				if(strength > highestStrength) {
					highest = word;
					highestStrength = strength;
					highestIterationNumber = count;
				}
			}
		}
		br.close();
	}

	private double computeStrength(float[] values) {
		double strength = 0;
		for(float value : values) {
			strength += value * value;
		}
		return Math.sqrt(strength);
	}
	
	public Word getWordWithLowestStrength() {
		return lowest;
	}
	
	public Word getWordWithHighestStrength() {
		return highest;
	}
	
	public long getLowestIterationNumber() {
		return lowestIterationNumber;
	}

	public long getHighestIterationNumber() {
		return highestIterationNumber;
	}
}
