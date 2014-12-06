package edu.asu.mwdb.epidemics.query_utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class Normalize {
	private double min;
	private double max;
	private String stateName;
	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}
	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}
	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}
	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Normalize() {
	}
	public Normalize(double min2, double max2) {
		this.min = min2;
		this.max = max2;
	}

	public void normalizeFile(String file, String inpDir, String normDir) throws IOException {
		BufferedReader buffRdr = null;
		String line = "";
		String normalizedLine = "";
		String split = ",";
		String[] entry;
		String[] entryOut;
		double max = 0.0, min = Double.MAX_VALUE, localMin, localMax;
		String readPath = inpDir + file;
		String writePath = normDir + file;
		buffRdr = new BufferedReader(new FileReader(readPath));
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(writePath));
		if((line = buffRdr.readLine()) != null){
			buffWriter.write(line);
			buffWriter.write(Constants.SEPARATOR_NEWLINE);
		}
		else
		{
			System.out.println("File Empty!!!"+ readPath);
			buffRdr.close();
			buffWriter.close();
			return;
		}
		while ((line = buffRdr.readLine()) != null) {
			if(line != null){
				entry = line.split(split);
				localMax = getMaxOfLine(entry);
				if(localMax > max)
					max = localMax;

				localMin = getMinOfLine(entry);
				if(localMin < min)
					min = localMin;
			}
				line = buffRdr.readLine();
		}
		buffRdr.close();
		
		Normalize normalize = new Normalize(min, max);
		buffRdr = new BufferedReader(new FileReader(readPath));
		//avoid the header 
		line = buffRdr.readLine();
		while((line = buffRdr.readLine()) != null){
			entry = line.split(split);
			entryOut = getNormalizedLine(entry, normalize);
			normalizedLine = getCSVLine(entryOut);
			normalizedLine = StringOperations.trimLastCharacter(normalizedLine);
			buffWriter.write(normalizedLine);
			buffWriter.write(Constants.SEPARATOR_NEWLINE);
		}
		buffWriter.close();
	}
	private String getCSVLine(String[] entryOut) {
		String result = "";
		for(int i = 0; i < entryOut.length ; i++){
			result = result + entryOut[i] + Constants.SEPARATOR_COMMA;
		}
		return result;
	}
	private String[] getNormalizedLine(String[] entry, Normalize normalize) {

		String entryOut[] = new String[entry.length];
		entryOut[0] = entry[0];
		entryOut[1] = entry[1];
		DecimalFormat df = new DecimalFormat("#.#########");
		for(int i = 2 ; i< entry.length ; i++){
			Double normalizedVal;
			double valueToNormalize = Double.parseDouble(entry[i]);
			normalizedVal = getNormalizedValue(normalize, valueToNormalize);
			entryOut[i] = df.format(normalizedVal);
		}
		
		return entryOut;
	}
	private double getNormalizedValue(Normalize normalize, double valueToNormalize) {
		double normValue;
		if((normalize.getMax() == normalize.getMin()))
			return 0.5;
		normValue = (valueToNormalize - normalize.getMin())/ (normalize.getMax() - normalize.getMin());
		return normValue;
	}
	private double getMinOfLine(String[] entry) {
		double lMin = Double.MAX_VALUE;
		for(int i = 0; i < entry.length-2 ; i++){
			if(Double.parseDouble(entry[i+2]) < lMin)
				lMin = Double.parseDouble(entry[i+2]);
		}
		return lMin;
	}
	private double getMaxOfLine(String[] entry) {
		double lMax = 0.0;
		for(int i = 0; i < entry.length-2 ; i++){
			if(Double.parseDouble(entry[i+2]) > lMax)
				lMax = Double.parseDouble(entry[i+2]);
		}
		return lMax;
	}
	public void normalizeAllFiles(List<String> allFiles, String dir, String dirNorm) throws IOException {
		for(String file : allFiles){
			//Normalize all files
		//String file = allFiles.get(0);
				normalizeFile(file, dir, dirNorm);
			}
	}

}
