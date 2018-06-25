package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.request.DataObject;
import com.dnastack.dos.server.request.DataObjects;
import com.dnastack.dos.server.request.ListRequest;
import com.dnastack.dos.server.service.Ga4ghDataObjectService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;



@RestController
public class Ga4ghDataObjectController {
	
	@Autowired
	private Ga4ghDataObjectService ga4ghDataObjectService;
	
	
	// POST Request - add data object
	@RequestMapping(
			value = "/dataobjects",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addObject(@RequestBody @Valid DataObject object) {
		// Check update/create format
		ga4ghDataObjectService.addObject(object.getData_object());
		return "The Data Object was successfully created.\n{\n\t\"data_object_id\":\"" + object.getData_object().getId() + "\"\n}";
		// not sure if there is anything wrong with doing things this way
		// I could make another class with a member variable "data_object_id" and pass object.getData_object().getId() into the setter method
		// but like that seems like so much work for something so simple
		// Maybe ask if there is a better way to do this...hopefully I don't have to use gson
	}

	// POST Request
	@RequestMapping(
			value = "/dataobjects/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public DataObjects getObjectList(@RequestBody @Valid ListRequest object) {
		return new DataObjects(ga4ghDataObjectService.getAllObjects());
	}
	
	
	// GET Request - returns specific data object by id
	@RequestMapping("/dataobjects/{data_object_id}")
	public DataObject getPostedObject(@PathVariable String data_object_id) throws EntityNotFoundException {
		return new DataObject(ga4ghDataObjectService.getObject(data_object_id));
	}
	
	// PUT Request - updates a data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateObject(@RequestBody DataObject object, @PathVariable String data_object_id) {
		ga4ghDataObjectService.updateObject(object.getData_object());
		return "{\"data_object_id\":\"" + object.getData_object().getId() + "\"}";
	}
	
	// DELETE Request - deletes data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.DELETE)
	public String updateObject(@PathVariable String data_object_id) {
		ga4ghDataObjectService.deleteObject(data_object_id);
		return "{\"data_object_id\":\"" + data_object_id + "\"}";
	}
	
	
	// GET Request - gets all versions of a data object by a data_object_id
	@RequestMapping("/dataobjects/{data_object_id}/versions")
	public void getObjectVersions(@PathVariable String data_bundle_id) {
		//How would I implement this?
		//If I were to POST a version 2 of a databundle, what should I have done with the version 1
		//would version 1 and version 2 share the same data_bundle_ids?
	}
	
}
