package com.nlp.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {
	public static String parseHost(String url) {
		String host = null;
		try {
			URL urlObj = new URL(url);
			host = urlObj.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return host;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://medialize.github.io/URI.js/about-uris.html");
		System.out.println(url.getProtocol());
		System.out.println(url.getHost());
		System.out.println(url.getPath());
		System.out.println(url.getAuthority());
		System.out.println(url.getPort());
	}
}
