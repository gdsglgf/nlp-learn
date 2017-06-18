package com.nlp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlp.dto.DatatablesViewPage;
import com.nlp.dto.PageDTO;
import com.nlp.dto.WebLinkDTO;
import com.nlp.mapper.WebMapper;
import com.nlp.model.HTML;
import com.nlp.model.Host;
import com.nlp.model.WebLink;
import com.nlp.model.WebURL;
import com.nlp.util.URLUtils;

@Service
@Transactional
public class WebService {
	private static final Logger log = LogManager.getLogger(WebService.class);
	@Autowired
	private WebMapper webMapper;

	public synchronized Host createHost(String hostname) {
		Host host = null;
		try {
			host = webMapper.getHostByName(hostname);
			if (host == null) {
				host = new Host(hostname);
				webMapper.createHost(host);
			}
		} catch (Exception e) {
			log.error(e);
		}

		return host;
	}

	public synchronized WebURL createWebURL(String url) {
		WebURL webURL = null;
		try {
			String hostname = URLUtils.parseHost(url);
			Host host = createHost(hostname);

			webURL = webMapper.getWebURLByUrl(url);
			if (webURL == null) {
				webURL = new WebURL(url, host);
				webMapper.createWebURL(webURL);
			}
		} catch (Exception e) {
			log.error(e);
		}

		return webURL;
	}

	public synchronized HTML createHTML(WebURL url, String docno, String title) {
		HTML html = new HTML(docno, title, url);
		webMapper.createHTML(html);
		return html;
	}

	public synchronized WebLink createWebLink(int urlId, String href, String text) {
		WebLink webLink = null;
		try {
			WebURL urlOut = createWebURL(href);
			webLink = new WebLink(urlId, urlOut, text);
			webMapper.createWebLink(webLink);
		} catch (Exception e) {
			log.error(e);
		}

		return webLink;
	}
	
	public List<WebLinkDTO> getWebURLByTitle(PageDTO params) {
		List<WebLinkDTO> result = webMapper.getWebURLByTitle(params);
		for (WebLinkDTO dto : result) {
			int urlId = dto.getId();
			dto.setIn(webMapper.countInUrl(urlId));
			dto.setOut(webMapper.countOutUrl(urlId));
		}
		return result;
	}
	
	public DatatablesViewPage<WebLinkDTO> getWebURLByPage(PageDTO params) {
		DatatablesViewPage<WebLinkDTO> data = new DatatablesViewPage<WebLinkDTO>();
		data.setAaData(getWebURLByTitle(params));
		int total = webMapper.countWebURL(params);
		data.setRecordsTotal(total);
		data.setRecordsFiltered(total);
		return data;
	}
}
