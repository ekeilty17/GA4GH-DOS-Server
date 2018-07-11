package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.DataObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataObjectResponse {
	
	private DataObject data_object;
	
}
