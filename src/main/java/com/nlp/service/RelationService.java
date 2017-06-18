package com.nlp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		TypeInfo typtInfo = relationMapper.getRelationTypeByDescription(type);
		if (typtInfo == null) {
			typtInfo = new TypeInfo(type);
			relationMapper.createRelationType(typtInfo);
			log.debug("Create relation type:" + typtInfo);
		}
		
		return typtInfo;
	}
	
	public synchronized Relation createRelation(Relation relation) {
		int id1 = relation.getSubject().getEntityId();
		int id2 = relation.getObject().getEntityId();
		Relation rela = relationMapper.getRelationByEntityId(id1, id2);
		if (rela == null) {
			relationMapper.createRelation(relation);
			rela = relation;
			log.debug("Create relation:" + rela);
		}
		return rela;
	}
	
	public synchronized RelationMention createRelationMention(int htmlId, Relation relation) {
		RelationMention mention = relationMapper.getRelationMention(relation.getRelaId(), htmlId);
		if (mention == null) {
			mention = new RelationMention(relation, new HTML(htmlId), 1);
			relationMapper.createRelationMention(mention);
		} else {
			mention.setCount(mention.getCount() + 1);
			relationMapper.updateRelationMention(mention);
		}
		log.debug("Create relation mention:" + mention);
		return mention;
	}
}
