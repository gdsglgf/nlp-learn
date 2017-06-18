package com.nlp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.dto.EntityDTO;
import com.nlp.mapper.EntityMapper;
import com.nlp.model.Entity;
import com.nlp.model.EntityMention;
import com.nlp.model.TypeInfo;

@Service
@Transactional
public class EntityService {
	private static final Logger log = LogManager.getLogger(EntityService.class);
	@Autowired
	private EntityMapper entityMapper;
	
	public synchronized TypeInfo createEntityType(String type) {
		TypeInfo typtInfo = entityMapper.getEntityTypeByDescription(type);
		if (typtInfo == null) {
			typtInfo = new TypeInfo(type);
			entityMapper.createEntityType(typtInfo);
			log.debug("Create entity type:" + typtInfo);
		}
		return typtInfo;
	}
	
	public synchronized Entity createEntity(String name, String type) {
		Entity entity = entityMapper.getEntity(name, type);
		if (entity == null) {
			TypeInfo typtInfo = createEntityType(type);
			entity = new Entity(typtInfo, name);
			entityMapper.createEntity(entity);
			log.debug("Create entity:" + entity);
		}
		return entity;
	}
	
	public synchronized Entity createEntity(EntityDTO dto) {
		if (dto == null) {
			return null;
		} else {
			return createEntity(dto.getName(), dto.getType());
		}
	}
	
	public void createEntityMention(EntityMention mention) {
		entityMapper.createEntityMention(mention);
		log.debug("Create entity mention:" + mention);
	}
}
