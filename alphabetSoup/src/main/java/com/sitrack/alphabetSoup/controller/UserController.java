package com.sitrack.alphabetSoup.controller;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sitrack.alphabetSoup.dto.CoordinateDTO;
import com.sitrack.alphabetSoup.dto.CreateGameDTO;
import com.sitrack.alphabetSoup.dto.CreateGameResponse;
import com.sitrack.alphabetSoup.dto.ErrorMessageResponse;
import com.sitrack.alphabetSoup.dto.GenericMessageResponseDTO;
import com.sitrack.alphabetSoup.dto.WordsListResponse;
import com.sitrack.alphabetSoup.service.UserService;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/AlphabetSoup")
public class UserController {

	private static final Log log = LogFactory.getLog(UserController.class);

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/")
	public ResponseEntity createGame(@RequestBody CreateGameDTO dto) {
		log.info("start to create a game");
		GenericMessageResponseDTO response = userService.createGame(dto);
		if (response.getMessageError() == null)
			return ResponseEntity.ok(new CreateGameResponse(response.getId()));
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(response.getMessageError()));
	}

	@GetMapping("/list/{id}")
	@ResponseBody
	public ResponseEntity getWordsInSaveGame(@PathVariable("id") String id) {
		log.info("searching words list of " + id + " save");
		GenericMessageResponseDTO response = userService.getWordsInSaveGame(UUID.fromString(id));
		if (response.getMessageError() == null)
			return ResponseEntity.ok(new WordsListResponse(response.getWordsList()));
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(response.getMessageError()));
	}

	@GetMapping("/view/{id}")
	@ResponseBody
	public ResponseEntity getGameCard(@PathVariable("id") String id) {
		log.info("searching Game Card of " + id + " save");
		GenericMessageResponseDTO response = userService.getGameCard(UUID.fromString(id));
		if (response.getMessageError() == null)
			return ResponseEntity.ok(new WordsListResponse(response.getGameCard()));
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(response.getMessageError()));
	}

	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity informAWord(@PathVariable("id") String id, @RequestBody CoordinateDTO dto) {
		log.info("searching Game Card of " + id + " save");
		GenericMessageResponseDTO response = userService.informAWord(UUID.fromString(id), dto);
		if (response.getMessageError() == null)
			return ResponseEntity.ok(response.getMessage());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(response.getMessageError()));
	}
}
