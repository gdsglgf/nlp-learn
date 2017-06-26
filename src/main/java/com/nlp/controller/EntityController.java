package com.nlp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.dto.PageDTO;
import com.nlp.model.Entity;
import com.nlp.model.EntityMention;
import com.nlp.model.Relation;
import com.nlp.service.EntityService;
import com.nlp.service.RelationService;

@Controller
@RequestMapping(value = "/entity")
public class EntityController {
	private static final Logger log = LogManager.getLogger(EntityController.class);
	@Autowired
	private EntityService entityService;
	@Autowired
	private RelationService relationService;
	
	@RequestMapping(value = "/entity-list", method = RequestMethod.GET)
	public ModelAndView entityListView() {
		ModelAndView view = new ModelAndView("entity/entity-list");
		view.addObject("types", entityService.getAllEntityType());
		return view;
	}
	
	@RequestMapping(value = "/queryEntity.action", method = RequestMethod.POST)
	public @ResponseBody DatatablesViewPage<Entity> queryEntityAction(
			HttpServletRequest request, PageDTO page) {
		log.debug(page);
		return entityService.getEntityList(page);
	}
	
	@RequestMapping(value = "/mention-list", method = RequestMethod.GET)
	public ModelAndView mentionListView(
			@RequestParam(value = "entityId", required = true) Integer entityId) {
		ModelAndView view = new ModelAndView("entity/mention-list");
		Entity entity = entityService.getEntityById(entityId);
		view.addObject("entityId", entityId);
		view.addObject("type", entity.getType().getDescription());
		view.addObject("name", entity.getName());
		return view;
	}
	
	@RequestMapping(value = "/queryEntityMention.action", method = RequestMethod.POST)
	public @ResponseBody DatatablesViewPage<EntityMention> queryEntityMentionAction(
			HttpServletRequest request, PageDTO page) {
		log.debug(page);
		return entityService.getEntityMentionList(page);
	}
	
	@RequestMapping(value = "/relation-list", method = RequestMethod.GET)
	public ModelAndView relationListView() {
		ModelAndView view = new ModelAndView("entity/relation-list");
		view.addObject("types", entityService.getAllEntityType());
		return view;
	}
	
	@RequestMapping(value = "/queryRelation.action", method = RequestMethod.POST)
	public @ResponseBody DatatablesViewPage<Relation> queryRelationAction(
			HttpServletRequest request, PageDTO page) {
		log.debug(page);
		
		String name = page.getParams().get("name");
		if (name != null && name.trim().length() > 0) {
			Entity entity = entityService.getEntityByName(name);
			if (entity != null) {
				page.getParams().put("entityId", Integer.toString(entity.getEntityId()));
			}
		}
		
		return relationService.getRelationList(page);
	}
}
