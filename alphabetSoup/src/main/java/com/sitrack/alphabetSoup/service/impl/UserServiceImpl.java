package com.sitrack.alphabetSoup.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sitrack.alphabetSoup.config.Constant;
import com.sitrack.alphabetSoup.dto.CoordinateDTO;
import com.sitrack.alphabetSoup.dto.CreateGameDTO;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;
import com.sitrack.alphabetSoup.model.SaveGameDO;
import com.sitrack.alphabetSoup.pojo.CardGame;
import com.sitrack.alphabetSoup.repository.SaveGameRepository;
import com.sitrack.alphabetSoup.repository.WordRepository;
import com.sitrack.alphabetSoup.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private static final Log log = LogFactory.getLog(UserServiceImpl.class);

	private WordRepository wordRepository;
	private SaveGameRepository saveGameRepository;

	@Autowired
	public UserServiceImpl(WordRepository wordRepository, SaveGameRepository saveGameRepository) {
		this.wordRepository = wordRepository;
		this.saveGameRepository = saveGameRepository;
	}

	@Override
	public GenericMessageResponseDTO createGame(CreateGameDTO dto) {
		GenericMessageResponseDTO response = new GenericMessageResponseDTO();
		if (dto.getW() < 15 || dto.getH() < 15) {
			response.setMessageError("El ancho o alto de no puede ser menor a 15");
			return response;
		}
		if (dto.getW() > 80 || dto.getH() > 80) {
			response.setMessageError("El ancho o alto de no puede ser mayor a 80");
			return response;
		}
		SaveGameDO save = new SaveGameDO(dto);
		if (!(save.isBottomToTop() || save.isDiagonal() || save.isLeftToRight() || save.isRightToLeft()
				|| save.isTopToBottom())) {
			response.setMessageError(
					"Mal configurado, debe tener al menos una opción de orientación para las palabras");
			return response;
		}
		save = newGame(save);
		try {
			response.setId(this.saveGameRepository.createGame(save));
		} catch (Exception e) {
			response.setMessageError(e.getMessage());
		}
		return response;
	}

	@Override
	public GenericMessageResponseDTO getWordsInSaveGame(UUID id) {
		GenericMessageResponseDTO response = new GenericMessageResponseDTO();
		try {
			response.setWordsList(this.saveGameRepository.getWordsInSaveGame(id));
		} catch (Exception e) {
			response.setMessageError(e.getMessage());
		}
		return response;
	}

	@Override
	public GenericMessageResponseDTO getGameCard(UUID id) {
		GenericMessageResponseDTO response = new GenericMessageResponseDTO();
		try {
			response.setGameCard(this.saveGameRepository.getGameCard(id));
		} catch (Exception e) {
			response.setMessageError(e.getMessage());
		}
		return response;
	}

	private SaveGameDO newGame(SaveGameDO save) {
		CardGame card = new CardGame(save);
		card = findNextWord(card, save);
		save.setWordsList(card.getWordsList());
		save.setGameCard(buildGameCard(card, save));
		return save;
	}

	private List<String> buildGameCard(CardGame card, SaveGameDO save) {
		List<String> gameCard = new ArrayList<String>();
		for (int i = 0; i < save.getHeight(); i++) {
			String row = "|";
			for (int j = 0; j < save.getWidth(); j++) {
				if (card.getNewCardGame()[i][j] != null)
					row = row + card.getNewCardGame()[i][j] + "|";
				else
					row = row + (char) (Constant.R.nextInt(26) + 'a') + "|";
			}
			gameCard.add(row);
		}
		return gameCard;
	}

	private CardGame findNextWord(CardGame card, SaveGameDO save) {
		card = findNextWordByWay(card, save);
		if (card.getWordsList().size() < save.getNumberOfWords()) {
			if (card.getPosibleWay().get(card.getPosibleWay().size() - 1) == card.getWay()) {
				card.setWay(card.getPosibleWay().get(0));
			} else {
				card.setWay(card.getPosibleWay().get(card.getPosibleWay().indexOf(card.getWay()) + 1));
			}
			return findNextWord(card, save);
		} else
			return card;
	}

	private CardGame findNextWordByWay(CardGame card, SaveGameDO save) {
		if (card.getLastWay() == card.getWay() || card.getLastWay() == Constant.INIWAY) {
			card = findNewPosition(card, save);
			card.setLastWord(this.wordRepository.findWordLength(findNewWordLength(card, save), card.getWordsList()));
		} else {
			card = findNewWordWithRegex(card, save);
		}
		card.setLastWay(card.getWay());
		if (card.getLastWord() == null) {
			return findNextWordByWay(card, save);
		}
		for (int k = 0; k < card.getLastWord().length(); k++) {
			switch (card.getWay()) {
			case Constant.LTRWAY:
				card.getNewCardGame()[card.getLastPosRow()][card.getLastPosColumn()+ (k - 1)] = card.getLastWord()
						.charAt(k) + "";
				break;
			case Constant.RTLWAY:
				card.getNewCardGame()[card.getLastPosRow()][card.getLastPosColumn()- (k + 1)] = card.getLastWord()
						.charAt(k) + "";
				break;
			case Constant.TTBWAY:
				card.getNewCardGame()[card.getLastPosRow() + (k - 1)][card.getLastPosColumn()] = card.getLastWord()
						.charAt(k) + "";
				break;
			case Constant.BTTWAY:
				card.getNewCardGame()[card.getLastPosRow()- (k + 1)][card.getLastPosColumn()] = card.getLastWord()
						.charAt(k) + "";
				break;
			default:
				card.getNewCardGame()[card.getLastPosRow() + (k - 1)][card.getLastPosColumn() + (k - 1)] = card
						.getLastWord().charAt(k) + "";
				break;
			}
		}
		card.getWordsList().add(card.getLastWord());
		return card;
	}

	private CardGame findNewWordWithRegex(CardGame card, SaveGameDO save) {
		int rowPointerPos = card.getLastPosRow(), columnPointerPos = card.getLastPosColumn();
		String newWord = null;
		for (int i = 0; i < card.getLastWord().length(); i++) {
			String match = "";
			int length = 0, regexAnyCounter = 0;
			switch (card.getWay()) {
			case (Constant.RTLWAY):
				while (0 < columnPointerPos) {
					length = length + 1;
					if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null) {
						if (regexAnyCounter > 0)
							match = match + "{" + regexAnyCounter + "}";
						regexAnyCounter = 0;
						match = match + card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] + ".";
					} else {
						if ("".equals(match))
							match = ".";
						regexAnyCounter = regexAnyCounter + 1;
					}
					columnPointerPos = columnPointerPos - 1;
					if (!(0 < columnPointerPos) && match.endsWith("."))
						match = match + "{0," + regexAnyCounter + "}";
				}
				break;
			case (Constant.BTTWAY):
				while (0 < rowPointerPos) {
					length = length + 1;
					if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null) {
						if (regexAnyCounter > 0)
							match = match + "{" + regexAnyCounter + "}";
						regexAnyCounter = 0;
						match = match + card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] + ".";
					} else {
						if ("".equals(match))
							match = ".";
						regexAnyCounter = regexAnyCounter + 1;
					}
					rowPointerPos = rowPointerPos - 1;
					if (!(0 < rowPointerPos) && match.endsWith("."))
						match = match + "{0," + regexAnyCounter + "}";
				}
				break;
			default:
				while ((save.getWidth() > columnPointerPos) && (save.getHeight() > rowPointerPos)) {
					length = length + 1;
					if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null) {
						if (regexAnyCounter > 0)
							match = match + "{" + regexAnyCounter + "}";
						regexAnyCounter = 0;
						match = match + card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] + ".";
					} else {
						if ("".equals(match))
							match = ".";
						regexAnyCounter = regexAnyCounter + 1;
					}
					if (card.getWay() == Constant.LTRWAY || card.getWay() == Constant.DWAY)
						columnPointerPos = columnPointerPos + 1;
					if (card.getWay() == Constant.TTBWAY || card.getWay() == Constant.DWAY)
						rowPointerPos = rowPointerPos + 1;
					if (!((save.getWidth() > columnPointerPos) && (save.getHeight() > rowPointerPos))
							&& match.endsWith("."))
						match = match + "{0," + regexAnyCounter + "}";
				}
				break;
			}
			newWord = this.wordRepository.findWordPatternLength(length, match, card.getWordsList());
			if (newWord != null) {
				switch (card.getWay()) {
				case Constant.LTRWAY:
					card.setLastPosColumn(card.getLastPosColumn() + i);
					break;
				case Constant.RTLWAY:
					card.setLastPosColumn(card.getLastPosColumn() - i);
					break;
				case Constant.TTBWAY:
					card.setLastPosRow(card.getLastPosRow() + i);
					break;
				case Constant.BTTWAY:
					card.setLastPosRow(card.getLastPosRow() - i);
					break;
				default:
					card.setLastPosRow(card.getLastPosRow() + i);
					card.setLastPosColumn(card.getLastPosColumn() + i);
					break;
				}
				i = card.getLastWord().length();
			} else {
				switch (card.getWay()) {
				case Constant.LTRWAY:
					columnPointerPos = card.getLastPosColumn() + i;
					break;
				case Constant.RTLWAY:
					columnPointerPos = card.getLastPosColumn() - i;
					break;
				case Constant.TTBWAY:
					rowPointerPos = card.getLastPosRow() + i;
					break;
				case Constant.BTTWAY:
					rowPointerPos = card.getLastPosRow() - i;
					break;
				default:
					columnPointerPos = card.getLastPosColumn() + i;
					rowPointerPos = card.getLastPosRow() + i;
					break;
				}
			}
		}
		card.setLastWord(newWord);
		return card;
	}

	private int findNewWordLength(CardGame card, SaveGameDO save) {
		int length = 0, rowPointerPos = card.getLastPosRow(), columnPointerPos = card.getLastPosColumn();
		boolean nonStop = true;
		switch (card.getWay()) {
		case (Constant.RTLWAY):
			while ((0 < columnPointerPos) && nonStop) {
				if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null)
					nonStop = false;
				else
					length = length + 1;
				columnPointerPos = columnPointerPos - 1;
			}
			break;
		case (Constant.BTTWAY):
			while ((0 < rowPointerPos) && nonStop) {
				if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null)
					nonStop = false;
				else
					length = length + 1;
				rowPointerPos = rowPointerPos - 1;
			}
			break;
		default:
			while ((save.getWidth() > columnPointerPos) && (save.getHeight() > rowPointerPos) && nonStop) {
				if (card.getNewCardGame()[rowPointerPos - 1][columnPointerPos - 1] != null)
					nonStop = false;
				else
					length = length + 1;
				if (card.getWay() == Constant.LTRWAY || card.getWay() == Constant.DWAY)
					columnPointerPos = columnPointerPos + 1;
				if (card.getWay() == Constant.TTBWAY || card.getWay() == Constant.DWAY)
					rowPointerPos = rowPointerPos + 1;
			}
			break;
		}
		if (length >= Constant.WORDMINLENGTH)
			return length;
		else {
			card = findNewPosition(card, save);
			return findNewWordLength(card, save);
		}
	}

	private CardGame findNewPosition(CardGame card, SaveGameDO save) {
		boolean findAgain = false;
		card.setLastPosRow(Constant.R.nextInt(save.getHeight()) + 1);
		card.setLastPosColumn(Constant.R.nextInt(save.getWidth()) + 1);
		log.debug("w: " + card.getLastPosColumn() + " | h: " + card.getLastPosRow());
		try {
			for (int i = 0; i < Constant.WORDMINLENGTH; i++) {
				switch (card.getLastWay()) {
				case Constant.LTRWAY:
					if (card.getNewCardGame()[card.getLastPosRow()][card.getLastPosColumn() + i] != null)
						findAgain = true;
					break;
				case Constant.RTLWAY:
					if (card.getNewCardGame()[card.getLastPosRow()][card.getLastPosColumn() - i] != null)
						findAgain = true;
					break;
				case Constant.TTBWAY:
					if (card.getNewCardGame()[card.getLastPosRow() + i][card.getLastPosColumn()] != null)
						findAgain = true;
					break;
				case Constant.BTTWAY:
					if (card.getNewCardGame()[card.getLastPosRow() - i][card.getLastPosColumn()] != null)
						findAgain = true;
					break;
				default:
					if (card.getNewCardGame()[card.getLastPosRow() + i][card.getLastPosColumn() + i] != null)
						findAgain = true;
					break;
				}
			}
		} catch (Exception e) {
			log.info("Random Point fall out of range");
			return findNewPosition(card, save);
		}
		if (findAgain)
			return findNewPosition(card, save);
		else
			return card;
	}

	@Override
	public GenericMessageResponseDTO informAWord(UUID uuid, CoordinateDTO dto) {
		GenericMessageResponseDTO ret = getGameCard(uuid);
		ret.setWordsList(getWordsInSaveGame(uuid).getWordsList());
		List<String[]> card = getCardSet(ret.getGameCard());
		String word = extractTheWord(card, dto);
		log.debug("palabra encontrada:_" + word);
		if (ret.getWordsList().contains(word)) {
			this.saveGameRepository.uploadCardGame(uuid, changeTheCardWithFoundedWord(card, dto));
			ret.setMessage("La palabra " + word + " ha sido encontrada!");
			return ret;
		}
		ret.setMessage("La palabra que ingreso es " + word + " la cual no esta en la lista para ser encontrada");
		return ret;
	}

	private List<String> changeTheCardWithFoundedWord(List<String[]> card, CoordinateDTO dto) {
		if (dto.getSr().intValue() == dto.getEr().intValue()) {
			if (dto.getSc().intValue() < dto.getEc().intValue())
				for (int i = 0; i < (dto.getEc().intValue() - dto.getSc().intValue())+1; i++) {
					card.get(dto.getSr().intValue() - 1)[dto.getSc().intValue()
							+ i] = card.get(dto.getSr().intValue() - 1)[dto.getSc().intValue() + i].toUpperCase();
				}
			else
				for (int i = 0; i < (dto.getSc().intValue() - dto.getEc().intValue())+1; i++) {
					card.get(dto.getSr().intValue() - 1)[dto.getEc().intValue()
							- i] = card.get(dto.getSr().intValue() - 1)[dto.getEc().intValue() - i].toUpperCase();
				}
			return makeGameCardFromCardUpdated(card);
		}
		if (dto.getSc().intValue() == dto.getEc().intValue()) {
			if (dto.getSr().intValue() < dto.getEr().intValue())
				for (int i = 0; i < (dto.getEr().intValue() - dto.getSr().intValue())+1; i++) {
					card.get(dto.getSr().intValue() + (i - 1))[dto.getSc()
							.intValue()] = card.get(dto.getSr().intValue() + (i - 1))[dto.getSc().intValue()]
									.toUpperCase();
				}
			else
				for (int i = 0; i < (dto.getSr().intValue() - dto.getEr().intValue())+1; i++) {
					card.get(dto.getSr().intValue() - (i - 1))[dto.getEc()
							.intValue()] = card.get(dto.getSr().intValue() - (i - 1))[dto.getEc().intValue()]
									.toUpperCase();
				}
			return makeGameCardFromCardUpdated(card);
		}
		for (int i = 0; i < (dto.getSr().intValue() - dto.getEr().intValue()); i++) {
			card.get(dto.getSr().intValue() + (i - 1))[dto.getEc().intValue()
					+ i] = card.get(dto.getSr().intValue() + (i - 1))[dto.getEc().intValue() + i].toUpperCase();
		}
		return makeGameCardFromCardUpdated(card);
	}

	private List<String> makeGameCardFromCardUpdated(List<String[]> card) {
		List<String> ret = new ArrayList<String>();
		for(int i=0; i < card.size(); i++) {
			String word="|";
			for(int j=1; j < card.get(i).length; j++) {
				word = word + card.get(i)[j]+"|";
			}
			ret.add(word);
		}
		return ret;
	}

	private String extractTheWord(List<String[]> card, CoordinateDTO dto) {
		String ret = "";
		if (dto.getSr().intValue() == dto.getEr().intValue()) {
			if (dto.getSc().intValue() < dto.getEc().intValue())
				for (int i = 0; i < (dto.getEc().intValue() - dto.getSc().intValue())+1; i++) {
					ret = ret + card.get(dto.getSr().intValue() - 1)[dto.getSc().intValue() + i];
				}
			else
				for (int i = 0; i < (dto.getSc().intValue() - dto.getEc().intValue())+1; i++) {
					ret = ret + card.get(dto.getSr().intValue() - 1)[dto.getEc().intValue() - i];
				}
		}
		if (dto.getSc().intValue() == dto.getEc().intValue()) {
			if (dto.getSr().intValue() < dto.getEr().intValue())
				for (int i = 0; i < (dto.getEr().intValue() - dto.getSr().intValue())+1; i++) {
					ret = ret + card.get(dto.getSr().intValue() + (i - 1))[dto.getSc().intValue()];
				}
			else
				for (int i = 0; i < (dto.getSr().intValue() - dto.getEr().intValue())+1; i++) {
					ret = ret + card.get(dto.getSr().intValue() - (i - 1))[dto.getEc().intValue()];
				}
		}
		if ("".equals(ret))
			for (int i = 0; i < (dto.getSr().intValue() - dto.getEr().intValue())+1; i++) {
				ret = ret + card.get(dto.getSr().intValue() + (i - 1))[dto.getEc().intValue() + i];
			}
		return ret;
	}

	private List<String[]> getCardSet(List<String> gameCard) {
		List<String[]> ret = new ArrayList<String[]>();
		for (int i = 0; i < gameCard.size(); i++) {
			ret.add(gameCard.get(i).split("\\|"));
		}
		return ret;
	}

}
