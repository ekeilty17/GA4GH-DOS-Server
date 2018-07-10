package com.dnastack.dos.server.model;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@Entity
public class Ga4ghDataBundleVersioning {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long versionId;
	
	@OneToOne
	private Ga4ghDataBundle ga4ghDataBundle;
	
	public Ga4ghDataBundleVersioning() {
		
	}
	
	public Ga4ghDataBundleVersioning(Ga4ghDataBundle ga4ghDataBundle) {
		super();
		this.ga4ghDataBundle = ga4ghDataBundle;
	}
	
	public Ga4ghDataBundleVersioning(Long versionId, Ga4ghDataBundle ga4ghDataBundle) {
		super();
		this.versionId = versionId;
		this.ga4ghDataBundle = ga4ghDataBundle;
	}
	
	/*
	@NotNull
	private String id;
	
	@Singular
	@ElementCollection
	@NotNull
	private List<String> data_object_ids;
    
	@NotNull
	private String created;
	@NotNull
	private String updated;
	
	@NotNull
	private String version;
	
	@Singular
	@ElementCollection
    @Embedded
    @NotNull
    @Valid
	private List<Checksum> checksums;
    
	private String description;
    
	@Singular
    @ElementCollection
    private List<String> aliases;
    
	@ElementCollection
    @MapKeyColumn(name="system_metadata_key")
    @Column(name="system_metadata_value")
	@NotNull
    private Map<String, String> system_metadata;
	
	@ElementCollection
    @MapKeyColumn(name="user_metadata_key")
    @Column(name="user_metadata_value")
	@NotNull
    private Map<String, String> user_metadata;
	
	public Ga4ghDataBundleVersioning() {
		
	}
	
	public Ga4ghDataBundleVersioning(Ga4ghDataBundle ga4ghDataBundle) {
		super();
		this.id = ga4ghDataBundle.getId();
		this.data_object_ids = ga4ghDataBundle.getData_object_ids();
		this.created = ga4ghDataBundle.getCreated(); 
		this. updated = ga4ghDataBundle.getUpdated();
		this.version = ga4ghDataBundle.getVersion();
		this.checksums = ga4ghDataBundle.getChecksums();
		this.description = ga4ghDataBundle.getDescription();
		this.aliases = ga4ghDataBundle.getAliases();
		this.system_metadata = ga4ghDataBundle.getSystem_metadata();
		this.user_metadata = ga4ghDataBundle.getUser_metadata();
	}
	*/
}
