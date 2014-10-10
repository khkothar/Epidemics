package edu.asu.mwdb.epidemics.write.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.write.buffer.Buffer;

public class Dictionary {
	
	private BufferedWriter bw = null; 
	
	/**
	 * Opens dictionary in an append mode
	 * @param fileName is name of the dictionary.
	 * @throws IOException
	 */
	public Dictionary(String fileName) throws IOException {
		this.bw = new BufferedWriter(new FileWriter(fileName));
	}
	
	public void write(Buffer buffer) throws IOException {
		if(buffer != null) {
			for(Word word : buffer.getWordList()) {
				write(word);
			}
		}
	}
	
	public void write(Word word) throws IOException {
		bw.append(word.toString() + "\n");
	}
	
	/**
	 * Closes the dictionary
	 * @throws IOException
	 */
	public void close() throws IOException {
		bw.close();
	}
	
}
