package com.dnastack.dos.server.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataObject {
	
	private String id;
    private String name;
    private String size;
    private String created;
    private String updated;
    private String version;	
    private String mimeType;
    private List<Checksum> checksums;
    private List<URL> urls;
    private String description;
    private List<String> aliases;
    
    public DataObject() {
    	
	}
    
	public DataObject(String id, String name, String size, String created, String updated, String version,
			String mimeType, List<Checksum> checksums, List<URL> urls, String description, List<String> aliases) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
		this.created = created;
		this.updated = updated;
		this.version = version;
		this.mimeType = mimeType;
		this.checksums = checksums;
		this.urls = urls;
		this.description = description;
		this.aliases = aliases;
	}
    
	public DataObject(Ga4ghDataObject ga4gh) {
		super();
		this.id = ga4gh.getId();
		this.name = ga4gh.getName();
		this.size = ga4gh.getSize();
		this.created = ga4gh.getCreated();
		this.updated = ga4gh.getUpdated();
		this.version = ga4gh.getVersion();
		this.mimeType = ga4gh.getMimeType();
		this.checksums = ga4gh.getChecksums();
		List<URL> urlList = new ArrayList<>();
		ga4gh.getUrls().forEach(u -> urlList.add(new URL(u)));
		this.urls = urlList;
		//this.urls = ga4gh.getUrls();
		this.description = ga4gh.getDescription();
		this.aliases = ga4gh.getAliases();
	}
    
}
