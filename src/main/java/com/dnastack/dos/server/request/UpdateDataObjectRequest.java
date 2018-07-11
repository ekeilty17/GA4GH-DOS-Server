package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.DataObject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDataObjectRequest {
	
	@NotNull
	private String data_object_id;
	@Valid
	@NotNull
	private DataObject data_object;
	
}
