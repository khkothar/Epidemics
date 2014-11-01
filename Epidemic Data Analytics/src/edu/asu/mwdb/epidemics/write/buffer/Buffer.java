package edu.asu.mwdb.epidemics.write.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.asu.mwdb.epidemics.domain.State;
import edu.asu.mwdb.epidemics.domain.Word;

public class Buffer {
	
	private List<Word> wordList;
	private final int SIZE = 51;
	
	public Buffer() {
		wordList = new ArrayList<Word>();
	}
	
	public boolean add(Word word) {
		wordList.add(word);
		if(wordList.size() == SIZE) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<Word> getWordList() {
		return wordList;
	}
	
	public Map<State, Word> getWordMap() {
		Map<State,Word> wordMap = new HashMap<State,Word>();
		for(Word word : wordList) {
			wordMap.put(word.getId().getState(), word);
		}
		return wordMap;
	}
	
}
