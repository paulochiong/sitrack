package com.sitrack.alphabetSoup.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.sitrack.alphabetSoup.config.Constant;
import com.sitrack.alphabetSoup.dto.Word;
import com.sitrack.alphabetSoup.model.SaveGameDO;
import com.sitrack.alphabetSoup.model.WordDO;
import com.sitrack.alphabetSoup.repository.WordRepository;

@Repository
public class WordRepositoryImpl implements WordRepository {

	private final MongoOperations mongoOperations;

	@Autowired
	public WordRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public boolean createWords(List<WordDO> save) {
		this.mongoOperations.insertAll(save);
		return true;
	}

	@Override
	public String findWordLength(int length, List<String> notInclude) {
		Query query = new Query();
		query.addCriteria(Criteria.where("largo").lt(length));
		if (notInclude.size() > 0)
			query.addCriteria(Criteria.where("word").nin(notInclude));
		List<WordDO> set = this.mongoOperations.find(query, WordDO.class);
		if (set.size() < 1)
			return null;
		return set.get(Constant.R.nextInt(set.size())).getWord();
	}

	@Override
	public String findWordPatternLength(int length, String match, List<String> notInclude) {
		Query query = new Query();
		query.addCriteria(Criteria.where("word").regex(match));
		query.addCriteria(Criteria.where("largo").lt(length));
		List<WordDO> set = this.mongoOperations.find(query, WordDO.class);
		if (set.size() < 1)
			return null;
		List<String> ret = new ArrayList<String>();
		set.forEach((s) -> {
			if(!notInclude.contains(s.getWord()))
				ret.add(s.getWord());
		});
		return ret.get(Constant.R.nextInt(ret.size()));
	}
}
