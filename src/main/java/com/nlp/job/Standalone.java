package com.nlp.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nlp.dto.EntityDTO;
import com.nlp.dto.LinkDTO;
import com.nlp.dto.WebDTO;
import com.nlp.model.Entity;
import com.nlp.model.EntityMention;
import com.nlp.model.FileModel;
import com.nlp.model.HTML;
import com.nlp.model.LengthLimit;
import com.nlp.model.Relation;
import com.nlp.model.TypeInfo;
import com.nlp.model.WebURL;
import com.nlp.service.EntityService;
import com.nlp.service.FileService;
import com.nlp.service.RelationService;
import com.nlp.service.WebService;
import com.nlp.util.FileUtils;
import com.nlp.util.HTMLParser;
import com.nlp.util.JSONUtils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

@Component
public class Standalone {
	private static final Logger log = LogManager.getLogger(Standalone.class);
	@Value("${src.disk.id}")
	private int diskId;
	@Value("${src.file.type}")
	private String fileType;
	@Value("${src.file.paths}")
	private String[] filePath;
	
	@Autowired
	private FileService fileService;
	@Autowired
	private WebService webService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private RelationService relationService;
	
	private StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");
	
	// load all pending files
	// for each file then split
	// parse html
	// save metadata
	// save links
	// extract entity and relation
	// save entity and relation
	public void exec() {
		log.info(String.format("diskId:%d, fileType:%s, paths:%s", diskId, fileType, Arrays.toString(filePath)));
		fileService.saveFileAtOnce(diskId, filePath, fileType);
		List<FileModel> files = fileService.getAllPendingFile();
		log.info(String.format("load %d files", files.size()));
		for (FileModel fm : files) {
			int cnt = 0;
			String line = null;
			StringBuilder sb = new StringBuilder();
			String path = fm.getAbsolutePath();
			log.info(String.format("File start:[%s]", path));
			try (BufferedReader reader = FileUtils.getBufferedReaderForCompressedFile(path)) {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					if (line.contains("</doc>")) {
						cnt++;
						parse(cnt, sb.toString());
						sb = new StringBuilder();
					}
				}
			} catch (IOException e) {
				log.error(e);
			}
			log.info(String.format("File end:[%s], total %d htmls", path, cnt));
		}
	}

	public void parse(int cnt, String htmlString) {
		WebDTO dto = HTMLParser.parse(htmlString);
		log.info(String.format("%d, url:%s", cnt, dto.getUrl()));
		if (dto.getHasText()) {
			if (dto.getUrl().length() < LengthLimit.URL_LENGTH) {
				WebURL webURL = webService.createWebURL(dto.getUrl());
				String docno = dto.getDocno();
				String title = dto.getTitle();
				if (title.length() < LengthLimit.TITLE_LENGTH) {
					HTML html = webService.createHTML(webURL, docno, title);
	
					int htmlId = html.getHtmlId();
					extract(htmlId, dto.getText());
	
					int urlId = webURL.getUrlId();
					saveLink(urlId, dto.getLinks());
				} else {
					log.error(String.format("title too long:url=[%s], title=[%s]", dto.getUrl(), title));
				}
			} else {
				log.error(String.format("URL too long:[%s]", dto.getUrl()));
			}
		}
	}

	private void saveLink(int urlId, List<LinkDTO> links) {
		int cnt = 0;
		for (LinkDTO link : links) {
			String href = link.getHref();
			String txt = link.getText();
			if (href.length() < LengthLimit.URL_LENGTH && txt.length() < LengthLimit.LINK_TEXT_LENGTH) {
				webService.createWebLink(urlId, href, txt);
				cnt++;
			} else {
				log.error(String.format("URL too long:[urlId=%d, %s]", urlId, link));
			}
		}
		log.info(String.format("Save link:[urlId=%d, total=%d]", urlId, cnt));
	}
	
	private synchronized List<List<EntityDTO>> annotate(String text) {
		if (text == null || text.trim().length() == 0) {
			return new ArrayList<List<EntityDTO>>();
		}
		Annotation annotation = new Annotation(text.trim());
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
	
	private void extract(int htmlId, String text) {
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
		Entity sub = entityService.createEntity(e1);
		Entity obj = entityService.createEntity(e2);
		String type = String.format("%s-%s", e1.getType(), e2.getType());
		TypeInfo typeInfo = relationService.createRelationType(type);
		Relation rela = relationService.createRelation(new Relation(sub, obj, typeInfo));
		relationService.createRelationMention(htmlId, rela);
	}
}
