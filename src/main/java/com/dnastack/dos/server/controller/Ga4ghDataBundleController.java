package com.dnastack.dos.server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.service.Ga4ghDataBundleService;

@RestController
public class Ga4ghDataBundleController {
	
	@Autowired
	private Ga4ghDataBundleService ga4ghDataBundleService;

	//POST Request - add a data bundle
	@RequestMapping(
			value = "/databundles",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)	//check that it only consumes a json object
	public String addObject(@RequestBody Ga4ghDataBundle object) {
		ga4ghDataBundleService.addObject(object);
		return "DataBundle Posted";
	}
	
	
	//POST Request - returns a list of databundles based on search criteria specified in the payload?
	// or does it just list all the data bundles?
	@RequestMapping(
			value = "/databundles/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Ga4ghDataBundle> getObjectList(@RequestBody Ga4ghDataBundle object) {
		//How do I return a json object?
		//can i just return a Ga4ghDataBundle object?
		return ga4ghDataBundleService.getAllObjects();
	}
	

	//GET Request - returns data bundle by data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}")
	public Optional<Ga4ghDataBundle> getPostedObject(@PathVariable String data_bundle_id) {
		return ga4ghDataBundleService.getObject(data_bundle_id);
	}
	
	//PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateObject(@RequestBody Ga4ghDataBundle object, @PathVariable String data_bundle_id) {
		ga4ghDataBundleService.updateObject(object);
		return "DataBundle Updated";
	}
	
	//DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.DELETE)
	public String updateObject(@PathVariable String data_bundle_id) {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return "DataBundle Deleted";
	}
	
	

	//GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public void getObjectVersions(@PathVariable String data_bundle_id) {
		//How would I implement this?
		//If I were to POST a version 2 of a databundle, what should I have done with the version 1
		//would version 1 and version 2 share the same data_bundle_ids?
	}
}
