package edu.asu.mwdb.epidemics.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.asu.mwdb.epidemics.domain.Id;
import edu.asu.mwdb.epidemics.domain.State;

public class EpidemicSimulationFileReader {
	
	private int windowLength;
	private int shiftLength;
	private String fileName;
	private String absolutePath;
	private float maxValue = Integer.MIN_VALUE;
	private float minValue = Integer.MAX_VALUE;
	private Queue<float[]> windowQueue;
	private Queue<Id> idQueue;
	private LinkedList<String> iterationQueue;
	private BufferedReader br = null;
	private Id id = null;
	LinkedList<float[]> rows;
	List<String> states;
	
	public EpidemicSimulationFileReader(String absolutePath, String fileName, int w, int h) throws IOException {
		this.fileName = fileName;
		this.absolutePath = absolutePath;
		this.windowLength = w;
		this.shiftLength = h;
		windowQueue = new LinkedList<float[]>();
		idQueue = new LinkedList<Id>();
		iterationQueue = new LinkedList<String>();
		rows = new LinkedList<float[]>();
		states = new ArrayList<String>();
		setMinMax();
	}
	
	private void setMinMax() throws IOException {
		String line;
		BufferedReader br = new BufferedReader(new FileReader(new File(absolutePath + "\\" + fileName)));
		//skipping first line
		br.readLine();
		while((line = br.readLine()) != null) {
			String[] columns = line.split(",");
			for(int i = 2; i < columns.length; i++) {
				float value = Float.parseFloat(columns[i]);
				if(maxValue < value) {
					maxValue = value;
				}
				if(minValue > value) {
					minValue = value;
				}
			}
		}
		
		br.close();
	}

	public float getMaxValue() throws IOException {
		return maxValue;
	}
	
	public float getMinValue() throws IOException {
		return minValue;
	}
	
	public float[] getNextWindow() throws IOException {
		if(windowQueue.size() == 0) {
			if(!addWindowsToQueue()) {
				return null;
			}
		}
		id = idQueue.poll();
		return windowQueue.poll();
	}
	
	private boolean addWindowsToQueue() throws IOException {
		if(br == null) {
			br = new BufferedReader(new FileReader(new File(absolutePath + "\\" + fileName)));
			String line = br.readLine();
			if(line == null)
				return false;
			String columns[] = line.split(",");
			Collections.addAll(states, Arrays.copyOfRange(columns, 2, columns.length));
		}
		
		/**
		 * reading float values for each row
		 */
		for(int i = rows.size(); i < windowLength; i++) {
			String line = br.readLine();
			if(line == null) {
				return false;
			} else {
				String[] columns = line.split(",");
				float[] metrics = new float[columns.length - 2];
				iterationQueue.add(columns[0]);
				for(int j = 2; j < columns.length; j++) {
					metrics[j - 2] = Float.parseFloat(columns[j]);
				}
				rows.add(metrics);
			}
		}
		
		/**
		 * Adding values to windowQueue
		 */
		for(int i = 0; i < rows.get(0).length; i++) {
			float[] window = new float[windowLength];
			for(int j = 0; j < windowLength; j++) {
				window[j] = (rows.get(j))[i];
			}
			windowQueue.add(window);
			idQueue.add(new Id(fileName, State.valueOf(states.get(i).replace("-", "_")), iterationQueue.get(0)));
		}
		
		/**
		 * Shifting window by shiftLength
		 */
		for(int i = 0; i < shiftLength; i++) {
			if(rows.size() > 0 && iterationQueue.size() > 0) {
				rows.removeFirst();
				iterationQueue.removeFirst();
			} else {
				br.readLine();
			}
		}
		
		return true;
	}

	public Id getId() {
		return id;
	}
	
	@Override
	protected void finalize() throws Throwable {
		br.close();
	}
}
