package edu.asu.mwdb.epidemics.task;

import java.io.IOException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import edu.asu.mwdb.epidemics.domain.Directory;
import edu.asu.mwdb.epidemics.domain.Resolution;
import edu.asu.mwdb.epidemics.read.EpidemicSimulationFileReaderThread;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;
import edu.asu.mwdb.epidemics.write.csv.Dictionary;
import edu.asu.mwdb.epidemics.write.csv.DictionaryWriter;

public class Task1 {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws MatlabInvocationException
	 * @throws MatlabConnectionException
	 */

	public static void main(String[] args) throws IOException,
			InterruptedException, MatlabConnectionException,
			MatlabInvocationException {
		if (args.length >= 4) {
			String directory = args[0];
			int windowLength = Integer.parseInt(args[1]);
			int shiftLength = Integer.parseInt(args[2]);
			int levels = Integer.parseInt(args[3]);

			int numberOfCores = Runtime.getRuntime().availableProcessors();
			Resolution resolution = new Resolution(levels);
			BufferPool bufferPool = new BufferPool();
			Directory dir = new Directory(directory);
			Dictionary dictionary = new Dictionary("epidemic_word_file.csv");

			Thread threads[] = new Thread[numberOfCores - 1];

			for (int i = 0; i < numberOfCores - 1; i++) {
				threads[i] = new Thread(new EpidemicSimulationFileReaderThread(
						dir, resolution, bufferPool, windowLength, shiftLength));
				threads[i].start();
			}

			Thread dictionaryWriter = new Thread(new DictionaryWriter(
					bufferPool, dictionary));
			dictionaryWriter.start();

			for (int i = 0; i < numberOfCores - 1; i++) {
				threads[i].join();
			}

			bufferPool.setFinished(true);

		}
	}

}
