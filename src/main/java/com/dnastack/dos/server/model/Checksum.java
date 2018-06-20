package com.dnastack.dos.server.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Checksum {
	
	@NotNull
	private String checksum = null;
	private String type = null;

	public Checksum checksum(String checksum) {
		this.checksum = checksum;
		return this;
	}
}
