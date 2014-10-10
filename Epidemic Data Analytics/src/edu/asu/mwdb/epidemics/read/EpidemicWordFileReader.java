package read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import write.buffer.Buffer;
import write.buffer.BufferPool;
import domain.Word;

public class EpidemicWordFileReader implements Runnable {
	
	private BufferPool bufferPool;
	private String fileName;
	public EpidemicWordFileReader(String fileName, BufferPool bufferPool) throws IOException {
		this.bufferPool = bufferPool;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		
		try {
			File f = new File(fileName);
			if(!f.exists()) {
					throw new FileNotFoundException("Epidemic word file does not exists");
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			Buffer buffer = new Buffer();
			while((line = br.readLine()) != null) {
				Word word = new Word(line);
				if(!buffer.add(word)) {
					bufferPool.add(buffer);
					buffer = new Buffer();
				}
			}
			bufferPool.setFinished(true);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
