package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataObjects {
	
	private List<Ga4ghDataObject> data_objects;
	
}
