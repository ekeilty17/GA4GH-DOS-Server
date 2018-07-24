package com.dnastack.dos.server.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceInfoResponse {
	
	private String version;
	private String name;
	private String description;
	
}
