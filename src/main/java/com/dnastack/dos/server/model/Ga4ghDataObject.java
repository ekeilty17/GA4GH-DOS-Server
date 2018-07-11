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

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"checksums", "urls"})
@EqualsAndHashCode(exclude = {"checksums", "urls"})
//(name = "ga4gh_data_object")
public class Ga4ghDataObject {
    
	@Id
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
    
    private String version;	
    private String mimeType;
    
    @Singular
    @ElementCollection
    @Embedded
    @NotNull
    @Valid
    private List<Checksum> checksums;
    
    // TODO look into if FetchType should be changed to LAZY
    // @OneToMany(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    // TODO look into "join table for mapping collections if you want Cascade collection"
    // FIXME urls must have a unique id, which means can't have duplicate urls
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private List<URL> urls;
    
    private String description;
    
    @Singular
    @ElementCollection
    private List<String> aliases;
    
    /*
    public Ga4ghDataObject id(String id) {
        this.id = id;
        return this;
    }
    */
}