package com.dnastack.dos.server.controller;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataObject;
import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.request.CreateDataObjectRequest;
import com.dnastack.dos.server.request.UpdateDataObjectRequest;
import com.dnastack.dos.server.response.CreateDataObjectResponse;
import com.dnastack.dos.server.response.DeleteDataObjectResponse;
import com.dnastack.dos.server.response.GetDataObjectResponse;
import com.dnastack.dos.server.response.ListDataObjectsResponse;
import com.dnastack.dos.server.response.UpdateDataObjectResponse;
import com.dnastack.dos.server.service.Ga4ghDataObjectService;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
public class Ga4ghDataObjectController {

	@Autowired
	private Ga4ghDataObjectService ga4ghDataObjectService;

	// GET Request - gets all data objects
	@RequestMapping("/dataobjects")
	public ListDataObjectsResponse getDataObjectsList(@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "checksum", required = false) String checksum,
			@RequestParam(value = "checksum_type", required = false) String checksum_type,
			@RequestParam(value = "page_size", required = false) Integer page_size,
			@RequestParam(value = "page_token", required = false) String page_token,
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws Exception {

		// Creating correct pageable variable
		if (page_token != null) {
			try {
				Integer.valueOf(page_token);
			} catch (Exception e) {
				throw new InvalidParameterException("Invalid page token");
			}
			pageable = new PageRequest(Integer.valueOf(page_token), pageable.getPageSize());
		}
		if (page_size != null) {
			pageable = new PageRequest(pageable.getPageNumber(), page_size);
		}

		if (alias != null) {
			try {
				ga4ghDataObjectService.getObjectsByAliasWithHighestVersion(alias, pageable.next());
				return new ListDataObjectsResponse(
						ga4ghDataObjectService.getObjectsByAliasWithHighestVersion(alias, pageable),
						String.valueOf(pageable.next().getPageNumber()));
			} catch (Exception e) {
				return new ListDataObjectsResponse(
						ga4ghDataObjectService.getObjectsByAliasWithHighestVersion(alias, pageable), "0");
			}
		}

		try {
			ga4ghDataObjectService.getAllObjectsWithHighestVersions(pageable.next());
			return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjectsWithHighestVersions(pageable),
					String.valueOf(pageable.next().getPageNumber()));
		} catch (Exception e) {
			return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjectsWithHighestVersions(pageable), "0");
		}
	}

	// GET Request - returns specific data object by id
	@RequestMapping("/dataobjects/{data_object_id}")
	public GetDataObjectResponse getDataObjectById(@PathVariable String data_object_id,
			@RequestParam(value = "version", required = false) String version) throws EntityNotFoundException {
		if (version != null) {
			return new GetDataObjectResponse(
					new DataObject(ga4ghDataObjectService.getObjectByIdAndVersion(data_object_id, version)));
		} else {
			return new GetDataObjectResponse(
					new DataObject(ga4ghDataObjectService.getObjectByIdWithHighestVersion(data_object_id)));
		}
	}

	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/dataobject/{data_object_id}/versions")
	public ListDataObjectsResponse getDataObjectVersions(@PathVariable String data_object_id,
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws EntityNotFoundException, Exception {
		return new ListDataObjectsResponse(
				ga4ghDataObjectService.getObjectByIdAndAllVersions(data_object_id, pageable));
	}

	// POST Request - add data object
	@RequestMapping(value = "/dataobjects", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CreateDataObjectResponse createDataObject(@RequestBody @Valid CreateDataObjectRequest object)
			throws Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_object().getCreated());
		DateTime D2 = new DateTime(object.getData_object().getUpdated());

		ga4ghDataObjectService.addObject(new Ga4ghDataObject(object.getData_object()));
		return new CreateDataObjectResponse(object.getData_object().getId());
	}

	// PUT Request - updates a data object by data_object_id
	@RequestMapping(value = "/dataobjects/{data_object_id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataObjectResponse updateDataObjectById(@RequestBody @Valid UpdateDataObjectRequest object,
			@PathVariable String data_object_id) throws EntityNotFoundException, Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_object().getCreated());
		DateTime D2 = new DateTime(object.getData_object().getUpdated());
		if (!data_object_id.equals(object.getData_object_id())) {
			throw new Exception("Conflicting data_object_id's in url and request body.");
		}

		ga4ghDataObjectService.updateObject(data_object_id, new Ga4ghDataObject(object.getData_object()));
		return new UpdateDataObjectResponse(object.getData_object().getId());
	}

	// DELETE Request - deletes data object by data_object_id
	@RequestMapping(value = "/dataobjects/{data_object_id}", method = RequestMethod.DELETE)
	public DeleteDataObjectResponse deleteDataObjectById(@PathVariable String data_object_id)
			throws EntityNotFoundException {
		ga4ghDataObjectService.deleteObject(data_object_id);
		return new DeleteDataObjectResponse(data_object_id);
	}

	// TEMPORARY METHODS - for debugging purposes

	// GET Request - temporary
	@RequestMapping("/dataobjects/auth")
	public String Test() {
		return "If you are seeing this, then you have been authenticated.";
	}

	// GET Request - temporary - gets all data objects and all their versions
	@RequestMapping("/dataobjects/allVersions")
	public ListDataObjectsResponse getAllDataBundlesAndAllVersions(
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws Exception {
		return new ListDataObjectsResponse(ga4ghDataObjectService.getAllObjectsAndAllVersions(pageable));
	}

	// GET Request - temporary - gets all data objects and all their versions as
	// they are represented in the database
	@RequestMapping("/dataobjects/allVersions/raw")
	public List<Ga4ghDataObject> getAllDataBundlesAndAllVersionsRaw() {
		return ga4ghDataObjectService.getAllObjectsAndAllVersionsRaw();
	}

	// DELETE Request - temporary - deletes all data objects
	@RequestMapping(value = "/dataobjects/all", method = RequestMethod.DELETE)
	public String deleteDataObjectById() {
		ga4ghDataObjectService.deleteAllObjects();
		return "All Data Objects deleted.";
	}
}
