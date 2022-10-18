package com.sitrack.alphabetSoup.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Word")
public class WordDO {

	@Id
	private String id;
	
	private String word;
	private int largo;

	public WordDO(String word) {
		this.word = word;
		this.largo = word.length();
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getLargo() {
		return largo;
	}

	public void setLargo(int largo) {
		this.largo = largo;
	}
}
