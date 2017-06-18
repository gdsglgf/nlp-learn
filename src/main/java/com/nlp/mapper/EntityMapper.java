package com.nlp.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

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
	public Entity getEntity(@Param("name") String name, @Param("description") String description);
	public EntityMention getEntityMentionById(@Param("mentionId") int mentionId);
}
