/**
 * 
 */
package edu.asu.mwdb.epidemics.task;

import java.io.IOException;

import edu.asu.mwdb.epidemics.read.EpidemicWordFileReader;
import edu.asu.mwdb.epidemics.write.buffer.BufferPool;


public class Task4 {

	public static void main(String[] args) throws IOException {
		/**
		 * Creating BufferPools for two files
		 */
		BufferPool readBufferPool1 = new BufferPool();
		BufferPool readBufferPool2 = new BufferPool();
		
		Thread parseEpidemicWordFileThread1 = new Thread(new EpidemicWordFileReader(args[0], readBufferPool1));
		Thread parseEpidemicWordFileThread2 = new Thread(new EpidemicWordFileReader(args[1], readBufferPool2)); 
		
		parseEpidemicWordFileThread1.start();
		parseEpidemicWordFileThread2.start();
		
	}

}
