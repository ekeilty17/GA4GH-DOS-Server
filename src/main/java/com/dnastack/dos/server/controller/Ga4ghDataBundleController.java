package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.request.DataBundle;
import com.dnastack.dos.server.request.DataBundles;
import com.dnastack.dos.server.request.ListRequest;
import com.dnastack.dos.server.service.Ga4ghDataBundleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


@RestController
public class Ga4ghDataBundleController {
	
	@Autowired
	private Ga4ghDataBundleService ga4ghDataBundleService;

	// POST Request - add a data bundle
	@RequestMapping(
			value = "/databundles",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addObject(@RequestBody @Valid DataBundle object) {
		ga4ghDataBundleService.addObject(object.getData_bundle());
		return "{\"data_bundle_id\":\"" + object.getData_bundle().getId() + "\"}";
	}
	
	// POST Request - returns a list of databundles based on search criteria specified in the payload?
	// or does it just list all the data bundles?
	@RequestMapping(
			value = "/databundles/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public DataBundles getObjectList(@RequestBody @Valid ListRequest object) {
		// Need to add search based on variables in object
		return new DataBundles(ga4ghDataBundleService.getAllObjects());
	}
	

	// GET Request - returns data bundle by data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}")
	public DataBundle getPostedObject(@PathVariable String data_bundle_id) throws EntityNotFoundException {
		return new DataBundle(ga4ghDataBundleService.getObject(data_bundle_id));
	}
	
	// PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateObject(@RequestBody DataBundle object, @PathVariable String data_bundle_id) {
		ga4ghDataBundleService.updateObject(object.getData_bundle());
		return "{\"data_bundle_id\":\"" + object.getData_bundle().getId() + "\"}";
	}
	
	// DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.DELETE)
	public String updateObject(@PathVariable String data_bundle_id) {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return "{\"data_bundle_id\":\"" + data_bundle_id + "\"}";
	}
	
	

	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public void getObjectVersions(@PathVariable String data_bundle_id) {
		//How would I implement this?
		//If I were to POST a version 2 of a databundle, what should I have done with the version 1
		//would version 1 and version 2 share the same data_bundle_ids?
	}
}
