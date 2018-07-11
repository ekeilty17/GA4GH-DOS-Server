package com.dnastack.dos.server.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
//@Builder
@Entity
@ToString(exclude = {"checksums"})
@EqualsAndHashCode(exclude = {"checksums"})
public class Ga4ghDataBundle {
	
	// TODO look into "@JsonView" for dealing with different permissions per element in data
	// TODO or us a DTO layer...probably better
	
	@Id
	@NotNull
	private String versionId;
	
	private boolean mostRecent;
	
	@NotNull
	private String id;
	
	@Singular
	@ElementCollection
	@NotNull
	private List<String> data_object_ids;
	// TODO possibly change all lists to sets...not sure yet
    
	@NotNull
	private String created;
	@NotNull
	private String updated;
	
	// FIXME "always use OptimisticLock for avoid concurrent data changing"
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
    //@CollectionTable(name="example_attributes", joinColumns=@JoinColumn(name="example_id"))
	@NotNull
    private Map<String, String> system_metadata;
	
	@ElementCollection
    @MapKeyColumn(name="user_metadata_key")
    @Column(name="user_metadata_value")
	@NotNull
    private Map<String, String> user_metadata;

	public Ga4ghDataBundle() {
		
	}
	
	public Ga4ghDataBundle(String id, List<String> data_object_ids, String created, String updated,
			String version, List<Checksum> checksums, String description, List<String> aliases,
			Map<String, String> system_metadata, Map<String, String> user_metadata) {
		super();
		this.versionId = id + 'v' + version;
		this.mostRecent = true;
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
		this.versionId =  dataBundle.getId() + 'v' + dataBundle.getVersion();
		this.mostRecent = true;
		this.id = dataBundle.getId();
		this.data_object_ids = dataBundle.getData_object_ids();
		this.created = dataBundle.getCreated(); 
		this.updated = dataBundle.getUpdated();
		this.version = dataBundle.getVersion();
		this.checksums = dataBundle.getChecksums();
		this.description = dataBundle.getDescription();
		this.aliases = dataBundle.getAliases();
		this.system_metadata = dataBundle.getSystem_metadata();
		this.user_metadata = dataBundle.getUser_metadata();
	}
	
}
