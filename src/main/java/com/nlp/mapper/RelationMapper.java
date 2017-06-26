package com.nlp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import com.nlp.dto.PageDTO;
import com.nlp.model.Relation;
import com.nlp.model.RelationMention;
import com.nlp.model.TypeInfo;

@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface RelationMapper {
	public void createRelationType(TypeInfo type);
	public void createRelation(Relation relation);
	public void createRelationMention(RelationMention mention);
	public void updateRelationMention(RelationMention mention);
	
	public TypeInfo getRelationTypeById(@Param("typeId") int typeId);
	public TypeInfo getRelationTypeByDescription(@Param("description") String description);
	public Relation getRelationById(@Param("relaId") int relaId);
	public Relation getRelationByEntityId(@Param("id1") int id1, @Param("id2") int id2);
	public RelationMention getRelationMentionById(@Param("mentionId") int mentionId);
	public RelationMention getRelationMention(@Param("relaId") int relaId, @Param("htmlId") int htmlId);
	
	public int countRelation(PageDTO page);
	public List<Relation> getRelationList(PageDTO page);
}
