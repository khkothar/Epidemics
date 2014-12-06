package edu.asu.mwdb.epidemics.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import edu.asu.mwdb.epidemics.domain.Directory;
import edu.asu.mwdb.epidemics.domain.Resolution;
import edu.asu.mwdb.epidemics.read.EpidemicSimulationFileReaderThread;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;
import edu.asu.mwdb.epidemics.write.csv.Dictionary;
import edu.asu.mwdb.epidemics.write.csv.DictionaryWriter;

public class Task1Query {
	public static void createQueryWordFile(String[] args) throws IOException,
			InterruptedException, MatlabConnectionException,
			MatlabInvocationException {
		if (args.length >= 4) {
			int windowLength = Integer.parseInt(args[0]);
			int shiftLength = Integer.parseInt(args[1]);
			int levels = Integer.parseInt(args[2]);
			String directory = args[3];

			int numberOfCores = 2;
			Resolution resolution = new Resolution(levels);
			new File("QueryWordFileDirectory").mkdir();
			
			String[] files = new File(directory).list();
			for(String file : files) {
				BufferPool bufferPool = new BufferPool();
				File queryDirectory = new File("temp");
				queryDirectory.mkdir();
				FileUtils.copyFileToDirectory(new File(directory + "\\" + file), queryDirectory);
				Directory dir = new Directory(queryDirectory.getName() + "\\" + file);
				Dictionary dictionary = new Dictionary("QueryWordFileDirectory\\" + file);
	
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
				dictionaryWriter.join();
				//Thread.sleep(5 * 1000);
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, MatlabConnectionException, MatlabInvocationException {
		createQueryWordFile(args);
	}
}
