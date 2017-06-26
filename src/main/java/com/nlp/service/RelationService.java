package com.nlp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.dto.PageDTO;
import com.nlp.mapper.RelationMapper;
import com.nlp.model.HTML;
import com.nlp.model.Relation;
import com.nlp.model.RelationMention;
import com.nlp.model.TypeInfo;

@Service
@Transactional
public class RelationService {
	private static final Logger log = LogManager.getLogger(RelationService.class);
	@Autowired
	private RelationMapper relationMapper;
	
	public synchronized TypeInfo createRelationType(String type) {
		TypeInfo typtInfo = null;
		try {
			typtInfo = relationMapper.getRelationTypeByDescription(type);
			if (typtInfo == null) {
				typtInfo = new TypeInfo(type);
				relationMapper.createRelationType(typtInfo);
				log.debug("Create relation type:" + typtInfo);
			}
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
		}
		return typtInfo;
	}
	
	public synchronized Relation createRelation(Relation relation) {
		Relation rela = null;
		try {
			int id1 = relation.getSubject().getEntityId();
			int id2 = relation.getObject().getEntityId();
			rela = relationMapper.getRelationByEntityId(id1, id2);
			if (rela == null) {
				relationMapper.createRelation(relation);
				rela = relation;
				log.debug("Create relation:" + rela);
			}
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
		}	
		return rela;
	}
	
	public synchronized RelationMention createRelationMention(int htmlId, Relation relation) {
		RelationMention mention = null;
		try {
			mention = relationMapper.getRelationMention(relation.getRelaId(), htmlId);
			if (mention == null) {
				mention = new RelationMention(relation, new HTML(htmlId), 1);
				relationMapper.createRelationMention(mention);
			} else {
				mention.setCount(mention.getCount() + 1);
				relationMapper.updateRelationMention(mention);
			}
			log.debug("Create relation mention:" + mention);
		} catch (Exception e) {
			log.error(String.format("%s", e.getMessage()));
		}
			
		return mention;
	}
	
	public DatatablesViewPage<Relation> getRelationList(PageDTO page) {
		DatatablesViewPage<Relation> result = new DatatablesViewPage<Relation>();
		String type = page.getParams().get("type");
		TypeInfo relaType = relationMapper.getRelationTypeByDescription(type);
		Integer typeId = null;
		if (relaType != null) {
			typeId = relaType.getTypeId();
			page.getParams().put("typeId", typeId.toString());
		}
		if (type.equals("ALL-ALL") || typeId != null) {
			List<Relation> pageData = relationMapper.getRelationList(page);
			result.setAaData(pageData);
			int total = 0;
			if (pageData.size() > 0) {
				total = relationMapper.countRelation(page);
			}
			result.setRecordsTotal(total);
			result.setRecordsFiltered(total);
			log.debug(String.format("Query: %s, total: %d", page, total));
		}
		return result;
	}
}
