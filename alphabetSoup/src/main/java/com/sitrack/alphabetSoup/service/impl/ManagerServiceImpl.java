package com.sitrack.alphabetSoup.service.impl;

import com.sitrack.alphabetSoup.dto.CreateWordsDTO;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;
import com.sitrack.alphabetSoup.dto.Word;
import com.sitrack.alphabetSoup.model.WordDO;
import com.sitrack.alphabetSoup.repository.WordRepository;

import com.sitrack.alphabetSoup.service.ManagerService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("managerService")
@Transactional
public class ManagerServiceImpl implements ManagerService {
	private WordRepository wordRepository;

    @Autowired
    public ManagerServiceImpl(WordRepository wordRepository){
        this.wordRepository = wordRepository;
    }

	@Override
	public GenericMessageResponseDTO createWords(List<CreateWordsDTO> dto) {
		GenericMessageResponseDTO response = new GenericMessageResponseDTO();
		List<WordDO> save = new ArrayList<WordDO>();
		dto.forEach(e ->{
			save.add(new WordDO(e.getWord()));
		});
		try {
			response.setOk(this.wordRepository.createWords(save));
		} catch (Exception e) {
			response.setMessageError(e.getMessage());
		}
		return response;
	}

}
