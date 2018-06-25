package com.dnastack.dos.server.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBundle {
	
	@Valid
	@NotNull
	Ga4ghDataBundle data_bundle;
	
}
