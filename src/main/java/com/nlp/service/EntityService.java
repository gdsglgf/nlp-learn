package com.nlp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.dto.EntityDTO;
import com.nlp.dto.PageDTO;
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
		TypeInfo typtInfo = null;
		try {
			typtInfo = entityMapper.getEntityTypeByDescription(type);
			if (typtInfo == null) {
				typtInfo = new TypeInfo(type);
				entityMapper.createEntityType(typtInfo);
				log.debug("Create entity type:" + typtInfo);
			}
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
		}
		
		return typtInfo;
	}
	
	public synchronized Entity createEntity(String name, String type) {
		Entity entity = null;
		try {
			entity = entityMapper.getEntity(name, type);
			if (entity == null) {
				TypeInfo typtInfo = createEntityType(type);
				entity = new Entity(typtInfo, name);
				entityMapper.createEntity(entity);
				log.debug("Create entity:" + entity);
			}
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
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
	
	public synchronized void createEntityMention(EntityMention mention) {
		try {
			entityMapper.createEntityMention(mention);
			log.debug("Create entity mention:" + mention);
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
		}	
	}
	
	public DatatablesViewPage<Entity> getEntityList(PageDTO page) {
		DatatablesViewPage<Entity> result = new DatatablesViewPage<Entity>();
		List<Entity> pageData = entityMapper.getEntityList(page);
		result.setAaData(pageData);
		int total = entityMapper.countEntity(page);
		result.setRecordsTotal(total);
		result.setRecordsFiltered(total);
		log.debug(String.format("Query: %s, total: %d", page, total));
		return result;
	}
	
	public List<TypeInfo> getAllEntityType() {
		return entityMapper.getAllEntityType();
	}
	
	public DatatablesViewPage<EntityMention> getEntityMentionList(PageDTO page) {
		DatatablesViewPage<EntityMention> result = new DatatablesViewPage<EntityMention>();
		List<EntityMention> pageData = entityMapper.getEntityMentionList(page);
		result.setAaData(pageData);
		Integer entityId = null;
		try {
			entityId = new Integer(page.getParams().get("entityId"));
		} catch (Exception e) {
			
		}
		int total = entityMapper.countEntityMention(entityId);
		result.setRecordsTotal(total);
		result.setRecordsFiltered(total);
		log.debug(String.format("Query: %s, total: %d", page, total));
		return result;
	}
	
	public Entity getEntityById(int entityId) {
		return entityMapper.getEntityById(entityId);
	}
}
