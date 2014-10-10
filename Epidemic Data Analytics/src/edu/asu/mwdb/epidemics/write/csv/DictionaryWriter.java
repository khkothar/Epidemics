package write.csv;

import java.io.IOException;

import write.buffer.Buffer;
import write.buffer.BufferPool;


public class DictionaryWriter implements Runnable {
	
	private BufferPool bufferPool;
	private Dictionary dictionary;
	
	public DictionaryWriter(BufferPool pool, Dictionary dictionary) {
		this.bufferPool = pool;
		this.dictionary = dictionary;
	}
	@Override
	public void run() {
		while(bufferPool.hasNext() || !bufferPool.isFinished()) {
			Buffer buffer = bufferPool.getNextBuffer();
			try {
				dictionary.write(buffer);
			} catch (IOException e) {
				System.err.println("DictionaryWriter : Error while writting to dictionary.");
				e.printStackTrace();
			}
		}
		try {
			dictionary.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
