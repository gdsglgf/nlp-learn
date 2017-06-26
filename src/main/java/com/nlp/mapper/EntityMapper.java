package com.nlp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import com.nlp.dto.PageDTO;
import com.nlp.model.Entity;
import com.nlp.model.EntityMention;
import com.nlp.model.TypeInfo;

@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface EntityMapper {
	public void createEntityType(TypeInfo type);
	public void createEntity(Entity entity);
	public void createEntityMention(EntityMention mention);
	
	public TypeInfo getEntityTypeById(@Param("typeId") int typeId);
	public TypeInfo getEntityTypeByDescription(@Param("description") String description);
	public Entity getEntityById(@Param("entityId") int entityId);
	public Entity getEntityByName(@Param("name") String name);
	public Entity getEntity(@Param("name") String name, @Param("description") String description);
	public EntityMention getEntityMentionById(@Param("mentionId") int mentionId);
	
	public List<TypeInfo> getAllEntityType();
	public int countEntity(PageDTO page);
	public List<Entity> getEntityList(PageDTO page);
	
	public int countEntityMention(@Param("entityId") Integer entityId);
	public List<EntityMention> getEntityMentionList(PageDTO page);
}
