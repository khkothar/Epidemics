package edu.asu.mwdb.epidemics.read;

import java.io.IOException;

import edu.asu.mwdb.epidemics.domain.Directory;
import edu.asu.mwdb.epidemics.domain.Resolution;
import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.write.buffer.Buffer;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;

public class EpidemicSimulationFileReaderThread implements Runnable {

	private Directory dir;
	private Resolution resolution;
	private BufferPool bufferPool;
	private int w;
	private int h;

	public EpidemicSimulationFileReaderThread(Directory dir, Resolution resolution, BufferPool bufferPool, int w, int h) {
		this.dir = dir;
		this.resolution = resolution;
		this.bufferPool = bufferPool;
		this.w = w;
		this.h = h;
	}

	@Override
	public void run() {
		String fileName = null;
		while ((fileName = dir.getNextFile()) != null) {
			EpidemicSimulationFileReader epidemicDataset;
			try {
				epidemicDataset = new EpidemicSimulationFileReader(dir.absolutePath(), fileName, w,
						h);
				float maxValue = epidemicDataset.getMaxValue();
				float minValue = epidemicDataset.getMinValue();
				float[] window = null;
				Buffer buffer = new Buffer();
				while ((window = epidemicDataset.getNextWindow()) != null) {
					normalize(window, maxValue, minValue);
					for (int i = 0; i < window.length; i++) {
						window[i] = resolution.getCenterValue(window[i]);
					}
					Window win = new Window(window);
					Word word = new Word(epidemicDataset.getId(), win);
					if(!buffer.add(word)) {
						bufferPool.add(buffer);
						buffer = new Buffer();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void normalize(float[] word, float maxValue, float minValue) {
		for (int i = 0; i < word.length; i++) {
			if(minValue == maxValue) {
				word[i] = 0.5f;
			} else {
				word[i] = (word[i] - minValue)/(maxValue - minValue);
			}
		}
	}

}
