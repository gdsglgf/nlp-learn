package com.nlp.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nlp.dto.EntityDTO;
import com.nlp.model.Entity;
import com.nlp.model.EntityMention;
import com.nlp.model.HTML;
import com.nlp.model.LengthLimit;
import com.nlp.model.Relation;
import com.nlp.model.TypeInfo;
import com.nlp.service.EntityService;
import com.nlp.service.RelationService;
import com.nlp.util.JSONUtils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

@Component
public class EntityExtracter implements MessageListener {
	private static final Logger log = LogManager.getLogger(EntityExtracter.class);
	@Autowired
	private EntityService entityService;
	@Autowired
	private RelationService relationService;
	
	private StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");
	
	@Override
	public void onMessage(Message message) {
		if ( message instanceof MapMessage ) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				int fileId = mapMessage.getInt("fileId");
				int htmlId = mapMessage.getInt("htmlId");
				String text = mapMessage.getString("text");
				
				log.debug(String.format("fileId=%d, htmlId=%d, text=[%s]", fileId, htmlId, text));
				extract(htmlId, text);
			} catch (JMSException e) {
				log.debug(e);
			}
		}
	}

	public List<List<EntityDTO>> annotate(String text) {
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		
		List<List<EntityDTO>> result = new ArrayList<List<EntityDTO>>();
		for (CoreMap sentence : sentences) {
			List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
			List<EntityDTO> sens = new ArrayList<EntityDTO>();
			for (CoreLabel token : tokens) {
				String ner = token.getString(NamedEntityTagAnnotation.class);
				String word = token.getString(TextAnnotation.class);
				sens.add(new EntityDTO(ner, word));
			}
			result.add(sens);
		}
		
		return result;
	}
	
	private void countAndSaveEntity(Map<String, Map<String, Integer>> cacheCounter, List<EntityDTO> entities, String word, String ner) {
		if (word == null || ner == null || word.equals("")) {
			return;
		}
		
		entities.add(new EntityDTO(ner, word));
		
		Map<String, Integer> cnt = cacheCounter.get(word);
		if (cnt == null) {
			cnt = new HashMap<String, Integer>();
			cnt.put(ner, 1);
			cacheCounter.put(word, cnt);
		} else {
			Integer num = cnt.get(ner);
			if (num == null) {
				num = 1;
			} else {
				num = num + 1;
			}
			cnt.put(ner, num);
		}
	}
	
	public void extract(int htmlId, String text) {
		List<List<EntityDTO>> sentences = annotate(text);
		Map<String, Map<String, Integer>> cacheCounter = new HashMap<String, Map<String, Integer>>();
		for (List<EntityDTO> sentence : sentences) {
			String word = null, ner = null, lastword = "", lastner = null;
			List<EntityDTO> entities = new ArrayList<EntityDTO>();
			for (EntityDTO token : sentence) {
				word = token.getName();
				ner = token.getType();
				if (ner.equals("O")) {
					countAndSaveEntity(cacheCounter, entities, lastword, lastner);
					lastword = "";
					lastner = null;
				} else {
					if (ner.equals(lastner)) {
						lastword = lastword + word;
					} else {
						countAndSaveEntity(cacheCounter, entities, lastword, lastner);
						lastword = word;
						lastner = ner;
					}
				}
			}
			countAndSaveEntity(cacheCounter, entities, lastword, lastner);
			
			if (entities.size() == 2) {
				// 二元关系解析
				// 可用变量sentence, entities
				saveRelation(htmlId, entities.get(0), entities.get(1));
			}
		}
		
		// 保存命名实体cacheCounter
		saveEntity(htmlId, cacheCounter);
	}
	
	public void saveEntity(int htmlId, Map<String, Map<String, Integer>> cacheCounter) {
		HTML html = new HTML(htmlId);
		for (Entry<String, Map<String, Integer>> ent : cacheCounter.entrySet()) {
			String name = ent.getKey();
			if (name.length() < LengthLimit.ENTITY_NAME_LENGTH) {
				for (Entry<String, Integer> item : ent.getValue().entrySet()) {
					String type = item.getKey();
					int count = item.getValue();
					try {
						Entity entity = entityService.createEntity(name, type);
						entityService.createEntityMention(new EntityMention(entity, html, count));
					} catch(Exception e) {
						log.error(String.format("Error:%s, %s", e.getMessage(), new EntityDTO(type, name)));	// Duplicate entry *** for key 'uc_entity'
					}
				}
			} else {
				log.error(String.format("Entity name too long: htmlId=%d, %s", htmlId, JSONUtils.toJSONString(ent)));
			}
		}
	}
	
	public void saveRelation(int htmlId, EntityDTO e1, EntityDTO e2) {
		if (e1.getName().length() > LengthLimit.ENTITY_NAME_LENGTH || e2.getName().length() > LengthLimit.ENTITY_NAME_LENGTH) {
			return;
		}
		try {
			Entity sub = entityService.createEntity(e1);
			Entity obj = entityService.createEntity(e2);
			String type = String.format("%s-%s", e1.getType(), e2.getType());
			TypeInfo typeInfo = relationService.createRelationType(type);
			Relation rela = relationService.createRelation(new Relation(sub, obj, typeInfo));
			relationService.createRelationMention(htmlId, rela);
		} catch (Exception e) {
			log.error(e);
		}
	}
}
