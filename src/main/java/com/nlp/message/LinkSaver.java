package com.nlp.message;

import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nlp.dto.LinkDTO;
import com.nlp.model.LengthLimit;
import com.nlp.service.WebService;

@Component
public class LinkSaver implements MessageListener {
	private static final Logger log = LogManager.getLogger(LinkSaver.class);
	@Autowired
	private WebService webService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				Map<String, Object> data = (Map<String, Object>) objectMessage.getObject();
				log.debug(data);
				int urlId = (int) data.get("urlId");
				List<LinkDTO> links = (List<LinkDTO>) data.get("links");
				for (LinkDTO link : links) {
					String href = link.getHref();
					String txt = link.getText();
					if (href.length() < LengthLimit.URL_LENGTH && txt.length() < LengthLimit.LINK_TEXT_LENGTH) {
						webService.createWebLink(urlId, href, txt);
					} else {
						log.debug(String.format("URL too long:[%s]", href));
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
