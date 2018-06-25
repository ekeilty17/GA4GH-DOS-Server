package com.dnastack.dos.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ga4ghDataObject {
    
	@Id
	@NotNull
	private String id;
    private String name;
    @NotNull
    private String size;
    
    //@NotNull
    //private DateTime created;
    //private DateTime updated;
    @NotNull
    private String created;
    private String updated;		// Is it a mistake that this isn't required...ask about this
    
    private String version;		// Same with
    private String mimeType;	// These
    
    @Singular
    @ElementCollection
    @Embedded
    @NotNull
    @Valid
    private List<Checksum> checksums;
    
    @Singular
    @ElementCollection
    @Embedded
    @Valid
    private List<URL> urls;
    private String description;
    
    @Singular
    @ElementCollection
    private List<String> aliases;

    public Ga4ghDataObject id(String id) {
        this.id = id;
        return this;
    }
}