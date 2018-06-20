package com.dnastack.dos.server.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class URL {
	@NotNull
    private String url = null;
    //private SystemMetadata systemMetadata = null;
    //private UserMetadata userMetadata = null;

}
