package com.dnastack.dos.server.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@ToString(exclude = { "checksums" })
@EqualsAndHashCode(exclude = { "checksums" })
public class Ga4ghDataBundle {

	@Id
	@NotNull
	private String versionId;

	private boolean highest;

	@NotNull
	private String id;

	@Singular
	@ElementCollection
	@NotNull
	private List<String> data_object_ids = new ArrayList<String>();

	@NotNull
	private String created;
	@NotNull
	private String updated;

	@NotNull
	private String version;

	@Singular
	@ElementCollection
	@Embedded
	//@NotNull
	@Valid
	private List<Checksum> checksums = new ArrayList<Checksum>();

	private String description;

	@Singular
	@ElementCollection
	private List<String> aliases = new ArrayList<String>();

	// WISH Should be able to handle objects, but since java is strictly typed,
	//		serializing them might be the best we can do
	@ElementCollection
	@MapKeyColumn(name = "system_metadata_key")
	@Column(name = "system_metadata_value")
	@NotNull
	private Map<String, String> system_metadata = new HashMap<String, String>();

	@ElementCollection
	@MapKeyColumn(name = "user_metadata_key")
	@Column(name = "user_metadata_value")
	@NotNull
	private Map<String, String> user_metadata = new HashMap<String, String>();

	// Custom Constructors

	// Missing: All Fields
	public Ga4ghDataBundle() {

	}

	// Missing: versionId, highest
	public Ga4ghDataBundle(String id, List<String> data_object_ids, String created, String updated, String version,
			List<Checksum> checksums, String description, List<String> aliases, Map<String, String> system_metadata,
			Map<String, String> user_metadata) {
		super();
		this.versionId = id + 'v' + version;
		this.highest = true;
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

	// Missing: versionId
	public Ga4ghDataBundle(boolean highest, String id, List<String> data_object_ids, String created, String updated,
			String version, List<Checksum> checksums, String description, List<String> aliases,
			Map<String, String> system_metadata, Map<String, String> user_metadata) {
		super();
		this.versionId = id + 'v' + version;
		this.highest = highest;
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

	public Ga4ghDataBundle(String versionId, boolean highest, String id, List<String> data_object_ids, String created,
			String updated, String version, List<Checksum> checksums, String description, List<String> aliases,
			Map<String, String> system_metadata, Map<String, String> user_metadata) {
		super();
		this.versionId = versionId;
		this.highest = highest;
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

	public Ga4ghDataBundle(DataBundle dataBundle) {
		super();
		this.versionId = dataBundle.getId() + 'v' + dataBundle.getVersion();
		this.highest = true;
		this.id = dataBundle.getId();
		this.data_object_ids = new ArrayList<String>(dataBundle.getData_object_ids());
		this.created = dataBundle.getCreated();
		this.updated = dataBundle.getUpdated();
		this.version = dataBundle.getVersion();
		this.checksums = new ArrayList<Checksum>(dataBundle.getChecksums());
		this.description = dataBundle.getDescription();
		this.aliases = new ArrayList<String>(dataBundle.getAliases());
		this.system_metadata = new HashMap<String,String>(dataBundle.getSystem_metadata());
		this.user_metadata = new HashMap<String,String>(dataBundle.getUser_metadata());
	}

}
