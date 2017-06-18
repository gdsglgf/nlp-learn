package com.nlp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import com.nlp.dto.PageDTO;
import com.nlp.dto.WebLinkDTO;
import com.nlp.model.HTML;
import com.nlp.model.Host;
import com.nlp.model.WebLink;
import com.nlp.model.WebURL;

@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)
public interface WebMapper {
	public void createHost(Host host);
	public void createWebURL(WebURL url);
	public void createWebLink(WebLink link);
	public void createHTML(HTML html);
	
	/**
	 * 按照主机编号查找
	 * @param hostId 主机编号
	 * @return
	 */
	public Host getHostById(@Param("hostId") int hostId);
	
	public Host getHostByName(@Param("hostname") String hostname);
	/**
	 * 按照链接编号查找
	 * @param urlId 链接编号
	 * @return
	 */
	public WebURL getWebURLById(@Param("urlId") int urlId);
	public WebURL getWebURLByUrl(@Param("url") String url);
	/**
	 * 查找网页链接列表
	 * @param urlId 网页主链接编号
	 * @return
	 */
	public List<WebLink> getWebLinkListByUrlId(@Param("urlId") int urlId);
	
	/**
	 * 按照网页编号查找
	 * @param htmlId 网页编号
	 * @return
	 */
	public HTML getHTMLById(@Param("htmlId") int htmlId);
	
	public int countInUrl(@Param("urlId") int urlId);
	public int countOutUrl(@Param("urlId") int urlId);
	public int countWebURL(PageDTO params);
	public List<WebLinkDTO> getWebURLByTitle(PageDTO params);
}
