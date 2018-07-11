package com.dnastack.dos.server.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DataBundle {

	private String id;
	private List<String> data_object_ids;
	private String created;
	private String updated;
	private String version;
	private List<Checksum> checksums;
	private String description;
	private List<String> aliases;
	private Map<String, String> system_metadata;
	private Map<String, String> user_metadata;

	// Custom Constructors

	public DataBundle() {

	}

	public DataBundle(String id, List<String> data_object_ids, String created, String updated, String version,
			List<Checksum> checksums, String description, List<String> aliases, Map<String, String> system_metadata,
			Map<String, String> user_metadata) {
		super();
		this.id = id;
		this.data_object_ids = data_object_ids;
		this.created = created;
		this.updated = updated;
		this.version = version;
		this.checksums = checksums;
		this.description = description;
		this.aliases = aliases;
		this.system_metadata = system_metadata;
		this.user_metadata = user_metadata;
	}

	public DataBundle(Ga4ghDataBundle ga4gh) {
		super();
		this.id = ga4gh.getId();
		this.data_object_ids = ga4gh.getData_object_ids();
		this.created = ga4gh.getCreated();
		this.updated = ga4gh.getUpdated();
		this.version = ga4gh.getVersion();
		this.checksums = ga4gh.getChecksums();
		this.description = ga4gh.getDescription();
		this.aliases = ga4gh.getAliases();
		this.system_metadata = ga4gh.getSystem_metadata();
		this.user_metadata = ga4gh.getUser_metadata();
	}

}
