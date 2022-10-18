package com.sitrack.alphabetSoup.repository;

import java.util.List;
import java.util.UUID;

import com.sitrack.alphabetSoup.model.WordDO;

public interface WordRepository {

	boolean createWords(List<WordDO> save);

	String findWordLength(int length, List<String> notInclude);

	String findWordPatternLength(int length, String match, List<String> notInclude);
}
