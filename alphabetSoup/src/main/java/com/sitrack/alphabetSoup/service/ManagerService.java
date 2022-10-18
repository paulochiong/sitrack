package com.sitrack.alphabetSoup.service;

import java.util.List;

import com.sitrack.alphabetSoup.dto.CreateWordsDTO;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;

public interface ManagerService {

	GenericMessageResponseDTO createWords(List<CreateWordsDTO> dto);
	
}