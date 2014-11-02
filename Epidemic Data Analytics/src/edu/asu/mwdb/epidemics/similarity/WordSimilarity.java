package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;

public class WordSimilarity implements Similarity {

	private Set<Window> s1, s2;
	
	@Override
	public float getScore(String f1, String f2) throws IOException {

		s1 = new HashSet<Window>();
		s2 = new HashSet<Window>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"simulation dictionary/epidemic_word_file_diff.csv")));
		String line = "";
		boolean flag = false; 
		File file1 = new File(f1);
		File file2 = new File(f2);

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(file1.getName())) {
				s1.add(word.getWindow());
			}
			
			if (word.getId().getFileName().equals(file2.getName())) {
				s2.add(word.getWindow());
				flag = true;
			}
		}
		br.close();
		
		if(!flag) {
			br = new BufferedReader(new FileReader(new File(
					"query dictionary/epidemic_word_file_diff.csv")));
			while ((line = br.readLine()) != null) {
				Word word = new Word(line);
				if (word.getId().getFileName().equals(file2.getName())) {
					s2.add(word.getWindow());
				}
			}
			br.close();
		}
		
		Set<Window> cloneS1 = new HashSet<Window>(s1);
		cloneS1.retainAll(s2);
		
		return cloneS1.size();
	}

}
