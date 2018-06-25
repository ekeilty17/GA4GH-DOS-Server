package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataObjectResponse {
	
	private Ga4ghDataObject data_object;
	
}
