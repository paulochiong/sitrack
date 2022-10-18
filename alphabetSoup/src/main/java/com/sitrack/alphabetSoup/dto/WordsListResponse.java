package com.sitrack.alphabetSoup.dto;

import java.util.ArrayList;
import java.util.List;

public class WordsListResponse {
	private List<String> wordsList;
	
	public WordsListResponse(List<String> list) {
		this.wordsList = list;
	}

	public List<String> getWordsList() {
		return wordsList;
	}

	public void setWordsList(List<String> wordsList) {
		this.wordsList = wordsList;
	}
	
}
