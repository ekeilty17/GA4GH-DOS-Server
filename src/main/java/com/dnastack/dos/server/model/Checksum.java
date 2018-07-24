package com.dnastack.dos.server.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Checksum {

	public void setType(Type type) {
		this.type = type;
	}

	public enum Type {
		md5("md5"), multipart_md5("multipart-md5"), S3("S3"), sha256("sha256"), sha512("sha512");

		private String val;

		private Type(String val) {
			this.val = val;
		}
	}

	@NotNull
	private String checksum;
	private Type type;

	// Custom Constructors

	public Checksum() {

	}

	public Checksum(String checksum) {
		super();
		this.checksum = checksum;
		this.type = Type.md5;
	}

	public Checksum(String checksum, Type type) {
		super();
		this.checksum = checksum;
		this.type = type;
	}

}
