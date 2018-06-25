package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDataBundleRequest {
	
	@NotNull
	private String data_bundle_id;
	@NotNull
	@Valid
	private Ga4ghDataBundle ga4ghDataBundle;
	
}
