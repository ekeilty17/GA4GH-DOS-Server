package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.DataObject;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListDataObjectsResponse {

	private List<DataObject> data_objects;
	private String next_page_token;

	public ListDataObjectsResponse() {

	}

	public ListDataObjectsResponse(Page<DataObject> page) {
		super();
		this.data_objects = page.getContent();
		this.next_page_token = null;
	}

	public ListDataObjectsResponse(Page<DataObject> page, String next_page_token) {
		super();
		this.data_objects = page.getContent();
		this.next_page_token = next_page_token;
	}

	public ListDataObjectsResponse(List<DataObject> data_objects) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = null;
	}

	public ListDataObjectsResponse(List<DataObject> data_objects, String next_page_token) {
		super();
		this.data_objects = data_objects;
		this.next_page_token = next_page_token;
	}

}
