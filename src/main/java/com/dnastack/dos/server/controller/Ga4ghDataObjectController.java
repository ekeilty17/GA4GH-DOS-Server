package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.request.CreateDataObjectRequest;
import com.dnastack.dos.server.request.UpdateDataObjectRequest;
import com.dnastack.dos.server.response.CreateDataObjectResponse;
import com.dnastack.dos.server.response.DeleteDataObjectResponse;
import com.dnastack.dos.server.response.GetDataObjectResponse;
import com.dnastack.dos.server.response.ListDataBundlesResponse;
import com.dnastack.dos.server.response.ListDataObjectsResponse;
import com.dnastack.dos.server.response.UpdateDataObjectResponse;
import com.dnastack.dos.server.service.Ga4ghDataObjectService;

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
public class Ga4ghDataObjectController {
	
	// temporary
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Ga4ghDataObjectService ga4ghDataObjectService;

	
	// GET Request - temporary
	@RequestMapping("/dataobjects/test")
	public String Test() {
		return "If you are seeing this, then you have been authenticated.";
	}
	
	// POST Request - add data object
	@RequestMapping(
			value = "/dataobjects",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public CreateDataObjectResponse createDataObject(@RequestBody @Valid CreateDataObjectRequest object) throws Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_object().getCreated());
		DateTime D2 = new DateTime(object.getData_object().getUpdated());
		
		ga4ghDataObjectService.addObject(object.getData_object());
		return new CreateDataObjectResponse(object.getData_object().getId());
	}

	// GET Request - gets all data objects
	@RequestMapping("/dataobjects")
	public ListDataObjectsResponse getDataObjectsList(
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "checksum", required = false) String checksum,
			@RequestParam(value = "checksum_type", required = false) String checksum_type,
			@RequestParam(value = "page_size", required = false) Integer page_size,
			@RequestParam(value = "page_token", required = false) String page_token,
			@PageableDefault(value = 5, page = 0) Pageable pageable) {
		if (page_size != null) {
			pageable = new PageRequest(pageable.getPageNumber(), page_size);
		}
		if (alias != null) {
			return new ListDataObjectsResponse(ga4ghDataObjectService.getObjectsByAlias(alias, pageable));
		}
		return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjects(pageable));
	}
	
	
	// GET Request - returns specific data object by id
	@RequestMapping("/dataobjects/{data_object_id}")
	public GetDataObjectResponse getDataObjectById(
			@PathVariable String data_object_id,
			@RequestParam(value = "version", required = false) String version) throws EntityNotFoundException {
		return new GetDataObjectResponse(ga4ghDataObjectService.getObject(data_object_id));
	}
	
	// PUT Request - updates a data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataObjectResponse updateDataObjectById(@RequestBody @Valid UpdateDataObjectRequest object, @PathVariable String data_object_id) throws EntityNotFoundException {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_object().getCreated());
		DateTime D2 = new DateTime(object.getData_object().getUpdated());
		
		ga4ghDataObjectService.updateObject(object.getData_object());
		return new UpdateDataObjectResponse(object.getData_object_id());
	}
	
	// DELETE Request - temporary - deletes all data objects
		@RequestMapping(
				value = "/dataobjects/all",
				method = RequestMethod.DELETE)
		public String deleteDataObjectById() {
			ga4ghDataObjectService.deleteAllObjects();
			return "All Data Objects deleted.";
		}
	
	// DELETE Request - deletes data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.DELETE)
	public DeleteDataObjectResponse deleteDataObjectById(@PathVariable String data_object_id) throws EntityNotFoundException {
		ga4ghDataObjectService.deleteObject(data_object_id);
		return new DeleteDataObjectResponse(data_object_id);
	}
	
	
	// GET Request - temporary - gets all data objects and all their versions
	@RequestMapping("/dataobject/allVersions")
	public ListDataObjectsResponse getAllDataBundlesAndAllVersions() {
		return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjectsAndAllVersions());
	}
	
	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/dataobject/{data_bundle_id}/versions")
	public ListDataObjectsResponse getDataBundleVersions(@PathVariable String data_object_id) {
		return new ListDataObjectsResponse(ga4ghDataObjectService.getAllVersionsById(data_object_id));
	}
	
}
