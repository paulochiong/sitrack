package com.sitrack.alphabetSoup.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.sitrack.alphabetSoup.model.SaveGameDO;
import com.sitrack.alphabetSoup.repository.SaveGameRepository;

@Repository
public class SaveGameRepositoryImpl implements SaveGameRepository {

	private final MongoOperations mongoOperations;

	@Autowired
	public SaveGameRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public UUID createGame(SaveGameDO save) {
		save = this.mongoOperations.insert(save, "SaveGame");
		return save.getUuid();
	}
	
	@Override
	public List<String> getWordsInSaveGame(UUID id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uuid").is(id));
		SaveGameDO ret = this.mongoOperations.find(query, SaveGameDO.class).get(0);
		return ret.getWordsList();
	}

	@Override
	public List<String> getGameCard(UUID id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uuid").is(id));
		SaveGameDO ret = this.mongoOperations.find(query, SaveGameDO.class).get(0);
		return ret.getGameCard();
	}

	@Override
	public void uploadCardGame(UUID uuid, List<String>  changeTheCardWithFoundedWord) {
		Query query = new Query().addCriteria(Criteria.where("uuid").is(uuid));
		Update updateDef = new Update().set("gameCard", changeTheCardWithFoundedWord);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
		this.mongoOperations.findAndModify(query, updateDef, SaveGameDO.class);
	}

}
