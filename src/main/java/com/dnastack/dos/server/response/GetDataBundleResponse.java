package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataBundleResponse {
	
	private Ga4ghDataBundle data_bundle;
	
}
