package com.dnastack.dos.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
//@Builder
@Entity
@ToString(exclude = {"checksums", "urls"})
@EqualsAndHashCode(exclude = {"checksums", "urls"})
//(name = "ga4gh_data_object")
public class Ga4ghDataObject {
    
	@Id
	@NotNull
	private String versionId;
	
	private boolean mostRecent;
	
	@NotNull
	//@Column(name = "ga4gh_data_object_id")
	private String id;
    private String name;
    @NotNull
    private String size;
    
    @NotNull
    private String created;
    @NotNull
    private String updated;
    
    @NotNull
    private String version;	
    private String mimeType;
    
    @Singular
    @ElementCollection
    @Embedded
    //@NotNull 	// FIXME Validation error when @NotNull is present
    //@Valid
    private List<Checksum> checksums;
    
    // TODO look into if FetchType should be changed to LAZY
    // @OneToMany(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    // TODO look into "join table for mapping collections if you want Cascade collection"
    // FIXME urls must have a unique id, which means can't have duplicate urls
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@Valid
    private List<Ga4ghURL> urls;
    
    private String description;
    
    @Singular
    @ElementCollection
    private List<String> aliases;

    
    // Custom Constructors
    
    public Ga4ghDataObject() {
		
	}
    
    public Ga4ghDataObject(String id, String name, String size, String created,
			String updated, String version, String mimeType, List<Checksum> checksums, List<Ga4ghURL> urls,
			String description, List<String> aliases) {
		super();
		this.versionId = id + 'v' + version;
		this.mostRecent = true;
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
    
	public Ga4ghDataObject(String versionId, boolean mostRecent, String id, String name, String size, String created,
			String updated, String version, String mimeType, List<Checksum> checksums, List<Ga4ghURL> urls,
			String description, List<String> aliases) {
		super();
		this.versionId = versionId;
		this.mostRecent = mostRecent;
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
    
	public Ga4ghDataObject(DataObject dataObject) {
		super();
		this.versionId = dataObject.getId() + 'v' + dataObject.getVersion();
		this.mostRecent = true;
		this.id = dataObject.getId();
		this.name = dataObject.getName();
		this.size = dataObject.getSize();
		this.created = dataObject.getCreated();
		this.updated = dataObject.getUpdated();
		this.version = dataObject.getVersion();
		this.mimeType = dataObject.getMimeType();
		this.checksums = dataObject.getChecksums();
		List<Ga4ghURL> urlList = new ArrayList<>();
		dataObject.getUrls().forEach(u -> urlList.add(new Ga4ghURL(u)));
		this.urls = urlList;
		//this.urls = dataObject.getUrls();
		this.description = dataObject.getDescription();
		this.aliases = dataObject.getAliases();
	}
    
    
}