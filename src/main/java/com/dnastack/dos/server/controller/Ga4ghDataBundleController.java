package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.request.CreateDataBundleRequest;
import com.dnastack.dos.server.request.ListRequest;
import com.dnastack.dos.server.request.UpdateDataBundleRequest;
import com.dnastack.dos.server.response.CreateDataBundleResponse;
import com.dnastack.dos.server.response.DeleteDataBundleResponse;
import com.dnastack.dos.server.response.GetDataBundleResponse;
import com.dnastack.dos.server.response.ListDataBundlesResponse;
import com.dnastack.dos.server.response.UpdateDataBundleResponse;
import com.dnastack.dos.server.service.Ga4ghDataBundleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class Ga4ghDataBundleController {
	
	@Autowired
	private Ga4ghDataBundleService ga4ghDataBundleService;
	
	// GET Request - temporary
	@RequestMapping("/databundles/test")
	public String Test() {
		return "If you are seeing this, then you have been authenticated.";
	}
	
	// POST Request - add a data bundle
	@RequestMapping(
			value = "/databundles",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public CreateDataBundleResponse createDataBundle(@RequestBody @Valid CreateDataBundleRequest object) {
		ga4ghDataBundleService.addObject(object.getData_bundle());
		return new CreateDataBundleResponse(object.getData_bundle().getId());
	}
	
	// POST Request - FIXME returns a list of databundles based on search criteria specified in the payload?
	// or does it just list all the data bundles?
	@RequestMapping(
			value = "/databundles/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ListDataBundlesResponse getDataBundlesList(@RequestBody @Valid ListRequest object) {
		// TODO Need to add search based on variables in object
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjects());
	}
	

	// GET Request - returns data bundle by data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}")
	public GetDataBundleResponse getDataBundleById(@PathVariable String data_bundle_id) throws EntityNotFoundException {
		return new GetDataBundleResponse(ga4ghDataBundleService.getObject(data_bundle_id));
	}
	
	// PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataBundleResponse updateDataBundleById(@RequestBody UpdateDataBundleRequest object, @PathVariable String data_bundle_id) {
		// TODO need to check that data_bundle_id == object.getData_bundle_id()
		// FIXME what do I do with object.getData_bundle_id != object.getGa4ghDataBundle().getId() ?
		ga4ghDataBundleService.updateObject(object.getGa4ghDataBundle());
		return new UpdateDataBundleResponse(object.getData_bundle_id());
	}
	
	// DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.DELETE)
	public DeleteDataBundleResponse deleteDataBundleById(@PathVariable String data_bundle_id) {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return new DeleteDataBundleResponse(data_bundle_id);
	}
	
	

	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public void getDataBundleVersions(@PathVariable String data_bundle_id) {
		// FIXME How would I implement this?
		// If I were to POST a version 2 of a databundle, what should I have done with the version 1
		// would version 1 and version 2 share the same data_bundle_ids?
	}
}
