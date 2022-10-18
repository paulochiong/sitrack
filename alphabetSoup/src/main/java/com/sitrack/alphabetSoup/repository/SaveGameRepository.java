package com.sitrack.alphabetSoup.repository;

import java.util.List;
import java.util.UUID;

import com.sitrack.alphabetSoup.model.SaveGameDO;

public interface SaveGameRepository {

	UUID createGame(SaveGameDO save);

	List<String> getWordsInSaveGame(UUID id);

	List<String> getGameCard(UUID id);

	void uploadCardGame(UUID uuid, List<String> changeTheCardWithFoundedWord);

}
