package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.DataBundle;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListDataBundlesResponse {

	private List<DataBundle> data_bundles;
	private String next_page_token;

	public ListDataBundlesResponse() {

	}

	public ListDataBundlesResponse(Page<DataBundle> page) {
		super();
		this.data_bundles = page.getContent();
		this.next_page_token = null;
	}

	public ListDataBundlesResponse(Page<DataBundle> page, String next_page_token) {
		super();
		this.data_bundles = page.getContent();
		this.next_page_token = next_page_token;
	}

	public ListDataBundlesResponse(List<DataBundle> data_bundles) {
		super();
		this.data_bundles = data_bundles;
		this.next_page_token = null;
	}

	public ListDataBundlesResponse(List<DataBundle> data_bundles, String next_page_token) {
		super();
		this.data_bundles = data_bundles;
		this.next_page_token = next_page_token;
	}

}
