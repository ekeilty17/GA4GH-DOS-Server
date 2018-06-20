package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.service.Ga4ghDataObjectService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

//import org.joda.time.DateTime;
import java.util.List;
import java.util.Optional;


@RestController
public class Ga4ghDataObjectController {
	
	@Autowired
	private Ga4ghDataObjectService ga4ghDataObjectService;
	
	/*
	//GET Request - returns all data objects
	@RequestMapping("/dataobjects")
	public List<Ga4ghDataObject> getAllPostedObjects() {
		return ga4ghDataObjectService.getAllObjects();
	}
	*/
	
	
	//POST Request - add data object
	@RequestMapping(
			value = "/dataobjects",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addObject(@RequestBody Ga4ghDataObject object) {
		ga4ghDataObjectService.addObject(object);
		return "DataObject Posted";
	}
	
	
	//POST Request
	@RequestMapping(
			value = "/dataobjects/list",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Ga4ghDataObject> getObjectList(@RequestBody Ga4ghDataObject object) {
		return ga4ghDataObjectService.getAllObjects();
	}
	
	
	//GET Request - returns specific data object by id
	@RequestMapping("/dataobjects/{data_object_id}")
	public Optional<Ga4ghDataObject> getPostedObject(@PathVariable String data_object_id) {
		return ga4ghDataObjectService.getObject(data_object_id);
	}
	
	//PUT Request - updates a data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateObject(@RequestBody Ga4ghDataObject object, @PathVariable String data_object_id) {
		ga4ghDataObjectService.updateObject(object);
		return "DataObject Updated";
	}
	
	//DELETE Request - deletes data object by data_object_id
	@RequestMapping(
			value = "/dataobjects/{data_object_id}",
			method = RequestMethod.DELETE)
	public String updateObject(@PathVariable String data_object_id) {
		ga4ghDataObjectService.deleteObject(data_object_id);
		return "DataObject Deleted";
	}
	
	
	//GET Request - gets all versions of a data object by a data_object_id
	@RequestMapping("/dataobjects/{data_object_id}/versions")
	public void getObjectVersions(@PathVariable String data_bundle_id) {
		//How would I implement this?
		//If I were to POST a version 2 of a databundle, what should I have done with the version 1
		//would version 1 and version 2 share the same data_bundle_ids?
	}
}
