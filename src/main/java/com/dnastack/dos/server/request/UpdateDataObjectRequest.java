package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDataObjectRequest {
	
	@NotNull
	private String data_object_id;
	@NotNull
	@Valid
	private Ga4ghDataObject ga4ghDataObject;
	
}
