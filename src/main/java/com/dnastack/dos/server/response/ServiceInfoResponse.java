package com.dnastack.dos.server.response;

import java.util.Map;

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
	private Map<String, String> contact;
	private Map<String, String> license;

}
