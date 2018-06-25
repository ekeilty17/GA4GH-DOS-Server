package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ListDataObjectsResponse {
	
	private List<Ga4ghDataObject> data_objects;
	private String next_page_token;
	
	public ListDataObjectsResponse() {
		
	}
	
	public ListDataObjectsResponse(List<Ga4ghDataObject> data_objects) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = "idk";
	}
	
	public ListDataObjectsResponse(List<Ga4ghDataObject> data_objects, String next_page_token) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = next_page_token;
	}
	
	
}
