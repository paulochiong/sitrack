package com.sitrack.alphabetSoup.dto;

import java.util.UUID;

public class CreateGameResponse {
	private UUID id;

	public CreateGameResponse(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
