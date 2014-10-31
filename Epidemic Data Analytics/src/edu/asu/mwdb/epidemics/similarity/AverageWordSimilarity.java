package edu.asu.mwdb.epidemics.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.asu.mwdb.epidemics.domain.Window;
import edu.asu.mwdb.epidemics.domain.Word;

public class AverageWordSimilarity implements Similarity {

	private Set<Window> s1, s2;

	public AverageWordSimilarity(File f1, File f2) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"epidemic_word_file_avg.csv")));
		s1 = new HashSet<Window>();
		s2 = new HashSet<Window>();
		String line = "";

		while ((line = br.readLine()) != null) {
			Word word = new Word(line);
			if (word.getId().getFileName().equals(f1.getName())) {
				s1.add(word.getWindow());
			} else if (word.getId().getFileName().equals(f2.getName())) {
				s2.add(word.getWindow());
			}
		}

		br.close();
	}

	@Override
	public float getScore(String f1, String f2) {
		Set<Window> cloneS1 = new HashSet<Window>(s1);
		cloneS1.retainAll(s2);
		return cloneS1.size();
	}

}
