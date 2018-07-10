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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"checksums"})
@EqualsAndHashCode(exclude = {"checksums"})
@EntityListeners(AuditingEntityListener.class)
public class Ga4ghDataBundle {
	
	// TODO look into "@JsonView" for dealing with different permissions per element in data
	// TODO or us a DTO layer...probably better
	
	@Id
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
	
	/*
	public Ga4ghDataBundle(Ga4ghDataBundle ga4ghDataBundle) {
		this(ga4ghDataBundle.getId(), ga4ghDataBundle.getData_object_ids(), ga4ghDataBundle.getCreated(), 
			 ga4ghDataBundle.getUpdated(), ga4ghDataBundle.getVersion(), ga4ghDataBundle.getChecksums(), ga4ghDataBundle.getDescription(), 
			 ga4ghDataBundle.getAliases(), ga4ghDataBundle.getSystem_metadata(), ga4ghDataBundle.getUser_metadata());
	}
	*/
	
	/*
	 * @Entity and @Id covert the class variables into columns in the SQL database
	 * @NotNull means it's a required field
	 * @Singular is part of lombok, works with the @Builder (part of @Data) and is needed to add data to the list in our class definition
	 * @ElementCollect is needed when adding data that's in a list to the SQL database
	 * @Embedded maps non-entities to columns in the SQL database. These are user-defined types such as Checksum and URL
	 */
}
