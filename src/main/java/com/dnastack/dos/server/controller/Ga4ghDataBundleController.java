package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataBundle;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
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

import java.util.ArrayList;
import java.util.List;

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
	
	
	// GET Request - gets all data bundles
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
			return new ListDataBundlesResponse(ga4ghDataBundleService.getObjectsByAliasWithMostRecentVersion(alias, pageable));
		}
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsWithMostRecentVersions(pageable));
	}

	// GET Request - returns data bundle by data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}")
	public GetDataBundleResponse getDataBundleById(
			@PathVariable String data_bundle_id,
			@RequestParam(value = "version", required = false) String version) throws EntityNotFoundException {
		if (version != null) {
			return new GetDataBundleResponse(new DataBundle(ga4ghDataBundleService.getObjectByIdAndVersion(data_bundle_id, version)));
		} else {
			return new GetDataBundleResponse(new DataBundle(ga4ghDataBundleService.getObjectByIdWithMostRecentVersion(data_bundle_id)));
		}
	}
	
	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public ListDataBundlesResponse getDataBundleVersions(
			@PathVariable String data_bundle_id, 
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws EntityNotFoundException {
		return new ListDataBundlesResponse(ga4ghDataBundleService.getObjectByIdAndAllVersions(data_bundle_id, pageable));
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
		
		ga4ghDataBundleService.addObject(new Ga4ghDataBundle(object.getData_bundle()));
		return new CreateDataBundleResponse(object.getData_bundle().getId());
	}
	
	
	// PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataBundleResponse updateDataBundleById(
			@RequestBody @Valid UpdateDataBundleRequest object, 
			@PathVariable String data_bundle_id) throws EntityNotFoundException {
		// TODO need to check that data_bundle_id == object.getData_bundle_id()
		// FIXME what do I do with object.getData_bundle_id != object.getGa4ghDataBundle().getId() ?
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_bundle().getCreated());
		DateTime D2 = new DateTime(object.getData_bundle().getUpdated());
		
		ga4ghDataBundleService.updateObject(new Ga4ghDataBundle(object.getData_bundle()));
		return new UpdateDataBundleResponse(object.getData_bundle_id());
	}
	
	
	// DELETE Request - temporary - deletes all data bundles in the database
	@RequestMapping(
			value = "/databundles/all",
			method = RequestMethod.DELETE)
	public String deleteAllDataBundles() {
		ga4ghDataBundleService.deleteAllObjects();
		return "All Data Bundles deleted.";
	}
	
	// DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(
			value = "/databundles/{data_bundle_id}",
			method = RequestMethod.DELETE)
	public DeleteDataBundleResponse deleteDataBundleById(@PathVariable String data_bundle_id) throws EntityNotFoundException {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return new DeleteDataBundleResponse(data_bundle_id);
	}
	
	
	
	// TEMPORARY METHODS - for debugging purposes
	
	// GET Request - temporary
	@RequestMapping("/databundles/test")
	public String Test() {
		return "If you are seeing this, then you have been authenticated.";
	}
	
	// GET Request - temporary - gets all data objects and all their versions
	@RequestMapping("/databundles/allVersions")
	public ListDataBundlesResponse getAllDataBundlesAndAllVersions(
			@PageableDefault(value = 10, page = 0) Pageable pageable) {
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsAndAllVersions(pageable));
	}
	
	// GET Request - temporary - gets all data objects and all their versions as they are represented in the database
	@RequestMapping("/databundles/allVersions/raw")
	public List<Ga4ghDataBundle> getAllDataBundlesAndAllVersionsRaw() {
		return ga4ghDataBundleService.getAllObjectsAndAllVersionsRaw();
	}
	
	
}
