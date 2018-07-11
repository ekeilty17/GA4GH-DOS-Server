package com.dnastack.dos.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericController {

	// Handling requests to pages that DNE
	@RequestMapping("/**")
	public String getEndpointDNE() {
		return "This page does not exist.";
	}

	@RequestMapping(value = "/**", method = RequestMethod.POST)
	public String postEndpointDNE() {
		return "This page does not exist.";
	}

	@RequestMapping(value = "/**", method = RequestMethod.PUT)
	public String putEndpointDNE() {
		return "This page does not exist.";
	}

	@RequestMapping(value = "/**", method = RequestMethod.DELETE)
	public String deleteEndpointDNE() {
		return "This page does not exist.";
	}

}
