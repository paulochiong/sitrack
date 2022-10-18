package com.sitrack.alphabetSoup.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sitrack.alphabetSoup.config.Constant;
import com.sitrack.alphabetSoup.dto.CreateGameDTO;

@Document("SaveGame")
public class SaveGameDO {

	@Id
	private String id;
	
	private UUID uuid;
	private int width;
	private int height;
	private boolean leftToRight;
	private boolean rightToLeft;
	private boolean topToBottom;
	private boolean bottomToTop;
	private boolean diagonal;
	private String gameSet;
	private List<String> wordsList;
	private List<String> gameCard;
	private int numberOfWords;
	
	public SaveGameDO() {
		this.uuid = UUID.randomUUID();
	}

	public SaveGameDO(String id, UUID uuid, int width, int height, boolean leftToRight, boolean rightToLeft,
			boolean topToBottom, boolean bottomToTop, boolean diagonal, String gameSet, int numberOfWords) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.width = width;
		this.height = height;
		this.leftToRight = leftToRight;
		this.rightToLeft = rightToLeft;
		this.topToBottom = topToBottom;
		this.bottomToTop = bottomToTop;
		this.diagonal = diagonal;
		this.gameSet = gameSet;
		this.numberOfWords = numberOfWords;
	}

	public SaveGameDO(CreateGameDTO dto) {
		this.uuid = UUID.randomUUID();
		if (dto.getW() != null && dto.getW().intValue() > 0)
			this.width = dto.getW().intValue();
		else
			this.width = Constant.WIDTH;
		if (dto.getH() != null && dto.getH().intValue() > 0)
			this.height = dto.getH().intValue();
		else
			this.height = Constant.HEIGHT;
		if (dto.getLtr() != null)
			this.leftToRight = (dto.getLtr().intValue() > 0);
		else
			this.leftToRight = Constant.LEFT2RIGHT;
		if (dto.getRtl() != null)
			this.rightToLeft = (dto.getRtl().intValue() > 0);
		else
			this.rightToLeft = Constant.RIGHT2LEFT;
		if (dto.getTtb() != null)
			this.topToBottom = (dto.getTtb().intValue() > 0);
		else
			this.topToBottom = Constant.TOP2BOTTOM;
		if (dto.getBtt() != null)
			this.bottomToTop = (dto.getBtt().intValue() > 0);
		else
			this.bottomToTop = Constant.BOTTOM2TOP;
		if (dto.getD() != null)
			this.diagonal = (dto.getD().intValue() > 0);
		else
			this.diagonal = Constant.DIAGONAL;
		if (dto.getNow() != null && dto.getNow().intValue() > 0)
			this.numberOfWords = dto.getNow().intValue();
		else
			this.numberOfWords = Constant.WORDSFORCARD;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isLeftToRight() {
		return leftToRight;
	}

	public void setLeftToRight(boolean leftToRight) {
		this.leftToRight = leftToRight;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public boolean isTopToBottom() {
		return topToBottom;
	}

	public void setTopToBottom(boolean topToBottom) {
		this.topToBottom = topToBottom;
	}

	public boolean isBottomToTop() {
		return bottomToTop;
	}

	public void setBottomToTop(boolean bottomToTop) {
		this.bottomToTop = bottomToTop;
	}

	public boolean isDiagonal() {
		return diagonal;
	}

	public void setDiagonal(boolean diagonal) {
		this.diagonal = diagonal;
	}

	public String getGameSet() {
		return gameSet;
	}

	public void setGameSet(String gameSet) {
		this.gameSet = gameSet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public void setNumberOfWords(int numberOfWords) {
		this.numberOfWords = numberOfWords;
	}
	
}
