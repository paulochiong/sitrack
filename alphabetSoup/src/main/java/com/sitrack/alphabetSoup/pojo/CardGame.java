package com.sitrack.alphabetSoup.pojo;

import java.util.ArrayList;
import java.util.List;

import com.sitrack.alphabetSoup.config.Constant;
import com.sitrack.alphabetSoup.model.SaveGameDO;

public class CardGame {

	private String[][] newCardGame;
	private List<String> wordsList;

	private int lastPosColumn;
	private int lastPosRow;
	private String lastWord;
	private int lastWay;
	private int way;
	private List<Integer> posibleWay;
	private int maxWordLength;

	public CardGame(String[][] card, int width, int height) {
		this.newCardGame = card;
		this.lastPosColumn = width;
		this.lastPosRow = height;
	}

	public CardGame(SaveGameDO save) {
		this.newCardGame = new String[save.getHeight()][save.getWidth()];
		this.lastWord = null;
		this.lastWay = 0;
		this.posibleWay = new ArrayList<Integer>();
		if (save.isLeftToRight())
			posibleWay.add(1);
		if (save.isRightToLeft())
			posibleWay.add(2);
		if (save.isTopToBottom())
			posibleWay.add(3);
		if (save.isBottomToTop())
			posibleWay.add(4);
		if (save.isDiagonal())
			posibleWay.add(5);
		this.way = this.posibleWay.get(0);
		this.wordsList = new ArrayList<String>();
		cleanCardGame(save);
	}

	private void cleanCardGame(SaveGameDO save) {
		for (int i = 0; i < save.getHeight(); i++) {
			for (int j = 0; j < save.getWidth(); j++) {
				this.newCardGame[i][j] = null;
			}
		}
	}

	public String[][] getNewCardGame() {
		return newCardGame;
	}

	public void setNewCardGame(String[][] newCardGame) {
		this.newCardGame = newCardGame;
	}

	public List<String> getWordsList() {
		return wordsList;
	}

	public void setWordsList(List<String> wordsList) {
		this.wordsList = wordsList;
	}

	public String getLastWord() {
		return lastWord;
	}

	public void setLastWord(String lastWord) {
		this.lastWord = lastWord;
	}

	public int getLastWay() {
		return lastWay;
	}

	public void setLastWay(int lastWay) {
		this.lastWay = lastWay;
	}

	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	public List<Integer> getPosibleWay() {
		return posibleWay;
	}

	public void setPosibleWay(List<Integer> posibleWay) {
		this.posibleWay = posibleWay;
	}

	public int getMaxWordLength() {
		return maxWordLength;
	}

	public void setMaxWordLength(int maxWordLength) {
		this.maxWordLength = maxWordLength;
	}

	public int getLastPosColumn() {
		return lastPosColumn;
	}

	public void setLastPosColumn(int lastPosColumnn) {
		this.lastPosColumn = lastPosColumnn;
	}

	public int getLastPosRow() {
		return lastPosRow;
	}

	public void setLastPosRow(int lastPosRow) {
		this.lastPosRow = lastPosRow;
	}

}
