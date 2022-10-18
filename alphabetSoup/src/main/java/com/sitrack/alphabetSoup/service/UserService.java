package com.sitrack.alphabetSoup.service;

import java.util.UUID;

import com.sitrack.alphabetSoup.dto.CoordinateDTO;
import com.sitrack.alphabetSoup.dto.CreateGameDTO;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;

public interface UserService {

	GenericMessageResponseDTO createGame(CreateGameDTO dto);

	GenericMessageResponseDTO getWordsInSaveGame(UUID id);

	GenericMessageResponseDTO getGameCard(UUID fromString);

	GenericMessageResponseDTO informAWord(UUID uuid, CoordinateDTO dto);
	
}
