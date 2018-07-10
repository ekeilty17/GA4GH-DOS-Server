package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.request.CreateDataBundleRequest;
import com.dnastack.dos.server.request.UpdateDataBundleRequest;
import com.dnastack.dos.server.response.CreateDataBundleResponse;
import com.dnastack.dos.server.response.DeleteDataBundleResponse;
import com.dnastack.dos.server.response.GetDataBundleResponse;
import com.dnastack.dos.server.response.ListDataBundlesResponse;
import com.dnastack.dos.server.response.UpdateDataBundleResponse;
import com.dnastack.dos.server.service.Ga4ghDataBundleService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class Ga4ghDataBundleController {
	
	// temporary
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
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
	public CreateDataBundleResponse createDataBundle(@RequestBody @Valid CreateDataBundleRequest object) throws Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_bundle().getCreated());
		DateTime D2 = new DateTime(object.getData_bundle().getUpdated());
		
		ga4ghDataBundleService.addObject(object.getData_bundle());
		return new CreateDataBundleResponse(object.getData_bundle().getId());
	}
	
	// GET Request - gets all data bundles
	// TODO Need to add search based on variables in object
	@RequestMapping("/databundles")
	public ListDataBundlesResponse getDataBundlesList(
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "checksum", required = false) String checksum,			//TODO unknown what this should do
			@RequestParam(value = "checksum_type", required = false) String checksum_type,	//TODO unknown what this should do
			@RequestParam(value = "page_size", required = false) Integer page_size,
			@RequestParam(value = "page_token", required = false) String page_token,		//TODO unknown what this should do
			@PageableDefault(value = 5, page = 0) Pageable pageable) { 						//TODO ask what the default should be
		//TODO both page_size and size map to the same thing
		//	   right now page_size overrides size...not sure if this matters
		if (page_size != null) {
			pageable = new PageRequest(pageable.getPageNumber(), page_size);
		}
		if (alias != null) {
			return new ListDataBundlesResponse(ga4ghDataBundleService.getObjectsByAlias(alias, pageable));
		}
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjects(pageable));
	}

	// GET Request - returns data bundle by data_bundle_id
	// TODO add version functionality
	@RequestMapping("/databundles/{data_bundle_id}")
	public GetDataBundleResponse getDataBundleById(
			@PathVariable String data_bundle_id,
			@RequestParam(value = "version", required = false) String version) throws EntityNotFoundException {
		return new GetDataBundleResponse(ga4ghDataBundleService.getObject(data_bundle_id));
	}
	
	// PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataBundleResponse updateDataBundleById(@RequestBody @Valid UpdateDataBundleRequest object, @PathVariable String data_bundle_id) throws EntityNotFoundException {
		// TODO need to check that data_bundle_id == object.getData_bundle_id()
		// FIXME what do I do with object.getData_bundle_id != object.getGa4ghDataBundle().getId() ?
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_bundle().getCreated());
		DateTime D2 = new DateTime(object.getData_bundle().getUpdated());
				
		ga4ghDataBundleService.updateObject(object.getData_bundle());
		return new UpdateDataBundleResponse(object.getData_bundle_id());
	}
	
	// DELETE Request - temporary - deletes all data bundles in the database
	@RequestMapping(
			value = "/databundles/all",
			method = RequestMethod.DELETE)
	public String deleteAllDataBundles() {
		ga4ghDataBundleService.deleteAllObjects();
		return "All Databundles Deleted.";
	}
	
	// DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.DELETE)
	public DeleteDataBundleResponse deleteDataBundleById(@PathVariable String data_bundle_id) throws EntityNotFoundException {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return new DeleteDataBundleResponse(data_bundle_id);
	}
	
	
	// GET Request - temporary - gets all data objects and all their versions
	@RequestMapping("/databundles/allVersions")
	public ListDataBundlesResponse getAllDataBundlesAndAllVersions() {
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsAndAllVersions());
	}
	
	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public ListDataBundlesResponse getDataBundleVersions(@PathVariable String data_bundle_id) {
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllVersionsById(data_bundle_id));
	}
	
}
