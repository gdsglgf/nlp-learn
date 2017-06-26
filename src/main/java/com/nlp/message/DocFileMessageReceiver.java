package com.nlp.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.nlp.dto.LinkDTO;
import com.nlp.dto.WebDTO;
import com.nlp.model.HTML;
import com.nlp.model.LengthLimit;
import com.nlp.model.WebURL;
import com.nlp.service.WebService;
import com.nlp.util.HTMLParser;

@Component
public class DocFileMessageReceiver implements MessageListener {
	private static final Logger log = LogManager.getLogger(DocFileMessageReceiver.class);
	@Resource(name = "linkSaverJmsTemplate")
	private JmsTemplate linkSaverJmsTemplate;
	@Resource(name = "entityExtracterJmsTemplate")
	private JmsTemplate entityExtracterJmsTemplate;

	@Autowired
	private WebService webService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				String htmlString = mapMessage.getString("doc");

				WebDTO dto = HTMLParser.parse(htmlString);
				log.debug(dto);
				if (dto.getHasText()) {
					if (dto.getUrl().length() < LengthLimit.URL_LENGTH) {
						WebURL webURL = webService.createWebURL(dto.getUrl());
						String docno = dto.getDocno();
						String title = dto.getTitle();
						HTML html = webService.createHTML(webURL, docno, title);
						if (html != null) {
							int htmlId = html.getHtmlId();
							int fileId = mapMessage.getInt("fileId");
							sendToEntityMessageReceiver(fileId, htmlId, dto.getText());
	
							int urlId = webURL.getUrlId();
							sendToLinkSaver(urlId, dto.getLinks());
						}
					} else {
						log.debug(String.format("URL too long:[%s]", dto.getUrl()));
					}
				}
			} catch (JMSException e) {
				log.debug(e);
			}
		}
	}

	private void sendToLinkSaver(int urlId, List<LinkDTO> links) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("urlId", urlId);
		data.put("links", links);
		linkSaverJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage((Serializable) data);
				return message;
			}
		});
	}

	private void sendToEntityMessageReceiver(int fileId, int htmlId, String docText) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("fileId", fileId);
		message.put("htmlId", htmlId);
		message.put("text", docText);
		entityExtracterJmsTemplate.convertAndSend(message);
	}
}
