package com.sitrack.alphabetSoup.dto;

import java.util.List;
import java.util.UUID;

public class GenericMessageResponseDTO {

	private boolean isOk;
	private UUID id;
	private String messageError;
	private String message;
	private List<String> wordsList;
	private List<String> gameCard;

	public GenericMessageResponseDTO() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public List<String> getWordsList() {
		return wordsList;
	}

	public void setWordsList(List<String> wordsList) {
		this.wordsList = wordsList;
	}

	public List<String> getGameCard() {
		return this.gameCard;
	}

	public void setGameCard(List<String> gameCard) {
		this.gameCard = gameCard;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
