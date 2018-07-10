package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import org.springframework.data.domain.Page;

@Data
@Builder
public class ListDataObjectsResponse {
	
	private List<Ga4ghDataObject> data_objects;
	private String next_page_token;
	
	public ListDataObjectsResponse() {
		
	}
	
	public ListDataObjectsResponse(Page<Ga4ghDataObject> page) {
		super();
		this.data_objects = page.getContent();
		this.next_page_token = null;
	}
	
	public ListDataObjectsResponse(Page<Ga4ghDataObject> page, String next_page_token) {
		super();
		this.data_objects = page.getContent();
		this.next_page_token = next_page_token;
	}
	
	public ListDataObjectsResponse(List<Ga4ghDataObject> data_objects) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = null;
	}
	
	public ListDataObjectsResponse(List<Ga4ghDataObject> data_objects, String next_page_token) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = next_page_token;
	}
	
}
