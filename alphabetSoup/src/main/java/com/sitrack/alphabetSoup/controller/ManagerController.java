package com.sitrack.alphabetSoup.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sitrack.alphabetSoup.dto.CreateGameDTO;
import com.sitrack.alphabetSoup.dto.CreateGameResponse;
import com.sitrack.alphabetSoup.dto.CreateWordsDTO;
import com.sitrack.alphabetSoup.dto.ErrorMessageResponse;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;
import com.sitrack.alphabetSoup.service.ManagerService;
import com.sitrack.alphabetSoup.service.UserService;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/AlphabetSoup/Manager")
public class ManagerController {

	private static final Log log = LogFactory.getLog(UserController.class);

	private final ManagerService managerService;

	@Autowired
	public ManagerController(ManagerService managerService) {
		this.managerService = managerService;
	}
	
	@PostMapping("/addWords")
	public ResponseEntity createWords(@RequestBody List<CreateWordsDTO> dto) {
		log.info("start to create words");
		GenericMessageResponseDTO response = managerService.createWords(dto);
		if(response.isOk())
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(response.getMessageError()));
	}
}