package task;

import java.io.IOException;

import neighborhood.ComputeAverageDiffThread;
import neighborhood.LocationMatrix;
import read.EpidemicWordFileReader;
import write.buffer.BufferPool;
import write.csv.Dictionary;
import write.csv.DictionaryWriter;

public class Task2 {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		BufferPool readBufferPool = new BufferPool();
		BufferPool writeAvgBufferPool = new BufferPool();
		BufferPool writeDiffBufferPool = new BufferPool();
		LocationMatrix locationMatrix = new LocationMatrix(args[0]);
		float alpha = Float.parseFloat(args[2]);
		Thread parseEpidemicWordFileThread = new Thread(new EpidemicWordFileReader(args[1], readBufferPool));
		parseEpidemicWordFileThread.start();
		Thread computeAvgDiffThread = new Thread(new ComputeAverageDiffThread(readBufferPool, writeAvgBufferPool, writeDiffBufferPool, locationMatrix, alpha)); 
		computeAvgDiffThread.start();
		Dictionary dictionaryAvg = new Dictionary("epidemic_word_file_avg.csv");
		Dictionary dictionaryDiff = new Dictionary("epidemic_word_file_diff.csv");
		Thread dictionaryWriter1 = new Thread(new DictionaryWriter(writeAvgBufferPool, dictionaryAvg));
		dictionaryWriter1.start();
		Thread dictionaryWriter2 = new Thread(new DictionaryWriter(writeDiffBufferPool, dictionaryDiff));
		dictionaryWriter2.start();
		dictionaryWriter1.join();
		dictionaryWriter2.join();
	}
}
