package com.dnastack.dos.server.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class URL {

	private String url;
	private Map<String, String> system_metadata;
	private Map<String, String> user_metadata;

	// Custom Constructors

	public URL() {

	}

	public URL(String url, Map<String, String> system_metadata, Map<String, String> user_metadata) {
		super();
		this.url = url;
		this.system_metadata = system_metadata;
		this.user_metadata = user_metadata;
	}

	public URL(Ga4ghURL ga4ghURL) {
		super();
		this.url = ga4ghURL.getUrl();
		this.system_metadata = ga4ghURL.getSystem_metadata();
		this.user_metadata = ga4ghURL.getUser_metadata();
	}

}
