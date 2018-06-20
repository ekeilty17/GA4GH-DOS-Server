package com.dnastack.dos.server.model;

import javax.persistence.Embeddable;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class URL {
	@NonNull
    private String url = null;
    //private SystemMetadata systemMetadata = null;
    //private UserMetadata userMetadata = null;

}
