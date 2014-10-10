package edu.asu.mwdb.epidemics.neighborhood;

import java.util.List;
import java.util.Map;

import edu.asu.mwdb.epidemics.domain.State;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.write.buffer.Buffer;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;

public class ComputeAverageDiffThread implements Runnable {

	private BufferPool readPool;
	private BufferPool writeAvgPool;
	private BufferPool writeDiffPool;
	private float alpha;
	private LocationMatrix locationMatrix;
	
	public ComputeAverageDiffThread(BufferPool readPool, BufferPool writeAvgPool, BufferPool writeDiffPool, LocationMatrix matrix, float alpha) {
		this.readPool = readPool;
		this.writeAvgPool = writeAvgPool;
		this.writeDiffPool = writeDiffPool;
		this.locationMatrix = matrix;
		this.alpha = alpha;
	}
	
	@Override
	public void run() {
		while(readPool.hasNext() || !readPool.isFinished()) {
			
			Buffer buffer = readPool.getNextBuffer();
			if(buffer != null) {
				Buffer bufferAvg = new Buffer();
				Buffer bufferDiff = new Buffer();
				Map<State, Word> wordMap = buffer.getWordMap();
				for(Word word : buffer.getWordList()) {
					State state = word.getId().getState();
					float[] window = word.getWindow().getValues();
					List<State> neighbors = locationMatrix.getNeighbors(state);
					float[] sum = new float[window.length];
					for(State neighbor : neighbors) {
						add(sum,wordMap.get(neighbor).getWindow().getValues());
					}
					float[] average = average(sum, neighbors.size());
					float[] intermediate = compute1MinusAlpha(average.clone());
					float[] avg_values = computeAverageWord(intermediate, window);
					float[] diff_values = computeDiffWord(window, average.clone());
					Window avg_window = new Window(avg_values);
					Window diff_window = new Window(diff_values);
					Word avg_word = new Word(word.getId(), avg_window);
					Word diff_word = new Word(word.getId(), diff_window);
					bufferAvg.add(avg_word);
					bufferDiff.add(diff_word);
					
				}
				
				writeAvgPool.add(bufferAvg);
				writeDiffPool.add(bufferDiff);
			}
			
		}
		
		writeAvgPool.setFinished(true);
		writeDiffPool.setFinished(true);
	}

	private float[] computeDiffWord(float[] window, float[] average) {
		float[] win_diff = new float[average.length];
		for(int i = 0; i < average.length; i++) {
			if(window[i] == 0) {
				win_diff[i] = 0.000001f;
			} else {
				win_diff[i] = (window[i] - average[i])/window[i];
			}
		}
		return win_diff;
	}

	private float[] computeAverageWord(float[] intermediate, float[] window) {
		float[] win_avg = new float[intermediate.length];
		for(int i = 0; i < intermediate.length; i++) {
			win_avg[i] = alpha * window[i] + intermediate[i];
		}
		return win_avg;
	}

	private float[] compute1MinusAlpha(float[] sum) {
		float[] temp = new float[sum.length];
		for(int i = 0; i < sum.length; i++) {
			temp[i] = (1 - alpha) * sum[i];
		}
		return temp;
	}

	private float[] average(float[] sum, int size) {
		float[] average = new float[sum.length];
		if(size > 0) {
			for(int i = 0; i < sum.length; i++) {
				average[i] = sum[i]/size;
			}
		}
		return average;
	}

	private void add(float[] sum, float[] values) {
		for(int i = 0; i < sum.length; i++) {
			sum[i] += values[i];
		}
	}

}
