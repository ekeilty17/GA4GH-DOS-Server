package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.request.CreateDataObjectRequest;
import com.dnastack.dos.server.request.ListRequest;
import com.dnastack.dos.server.request.UpdateDataObjectRequest;
import com.dnastack.dos.server.response.CreateDataObjectResponse;
import com.dnastack.dos.server.response.DeleteDataObjectResponse;
import com.dnastack.dos.server.response.GetDataObjectResponse;
import com.dnastack.dos.server.response.ListDataObjectsResponse;
import com.dnastack.dos.server.response.UpdateDataObjectResponse;
import com.dnastack.dos.server.service.Ga4ghDataObjectService;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class Ga4ghDataObjectController {
	
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
	public CreateDataObjectResponse createDataObject(@RequestBody @Valid CreateDataObjectRequest object) {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_object().getCreated());
		DateTime D2 = new DateTime(object.getData_object().getUpdated());
		
		ga4ghDataObjectService.addObject(object.getData_object());
		return new CreateDataObjectResponse(object.getData_object().getId());
	}

	// POST Request
	@RequestMapping(
			value = "/dataobjects/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ListDataObjectsResponse getDataObjectsList(@RequestBody @Valid ListRequest object) {
		return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjects());
	}
	
	
	// GET Request - returns specific data object by id
	@RequestMapping("/dataobjects/{data_object_id}")
	public GetDataObjectResponse getDataObjectById(@PathVariable String data_object_id) throws EntityNotFoundException {
		return new GetDataObjectResponse(ga4ghDataObjectService.getObject(data_object_id));
	}
	
	// PUT Request - updates a data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataObjectResponse updateDataObjectById(@RequestBody UpdateDataObjectRequest object, @PathVariable String data_object_id) {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getGa4ghDataObject().getCreated());
		DateTime D2 = new DateTime(object.getGa4ghDataObject().getUpdated());
		
		ga4ghDataObjectService.updateObject(object.getGa4ghDataObject());
		return new UpdateDataObjectResponse(object.getData_object_id());
	}
	
	// DELETE Request - deletes data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.DELETE)
	public DeleteDataObjectResponse deleteDataObjectById(@PathVariable String data_object_id) {
		ga4ghDataObjectService.deleteObject(data_object_id);
		return new DeleteDataObjectResponse(data_object_id);
	}
	
	
	// GET Request - gets all versions of a data object by a data_object_id
	@RequestMapping("/dataobjects/{data_object_id}/versions")
	public void getDataObjectVersions(@PathVariable String data_object_id) {
	}
	
}
