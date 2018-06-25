package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ListDataBundlesResponse {
	
	private List<Ga4ghDataBundle> data_bundles;
	private String next_page_token;
	
	public ListDataBundlesResponse() {
		
	}
	
	public ListDataBundlesResponse(List<Ga4ghDataBundle> data_bundles) {
		super();
		this.data_bundles = data_bundles;
		this.next_page_token = "idk";
	}
	
	public ListDataBundlesResponse(List<Ga4ghDataBundle> data_bundles, String next_page_token) {
		super();
		this.data_bundles = data_bundles;
		this.next_page_token = next_page_token;
	}
	
	
	
}
