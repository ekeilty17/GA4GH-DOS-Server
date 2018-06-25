package com.dnastack.dos.server.request;

import javax.validation.constraints.Min;

import com.dnastack.dos.server.model.Checksum;

import lombok.Data;

@Data
public class ListRequest {
	private String alias;
	private Checksum checksum;	// check if this should be checksum or checksums
	@Min(1)
	private int page_size;
	private String page_token;
}
