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
public class Ga4ghDataBundleController {

	@Autowired
	private Ga4ghDataBundleService ga4ghDataBundleService;

	// GET Request - gets all data bundles
	@RequestMapping("/databundles")
	public ListDataBundlesResponse getDataBundlesList(@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "checksum", required = false) String checksum, // TODO unknown what this should do
			@RequestParam(value = "checksum_type", required = false) String checksum_type, // TODO unknown what this
																							// should do
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
				ga4ghDataBundleService.getObjectsByAliasWithHighestVersion(alias, pageable.next());
				return new ListDataBundlesResponse(
						ga4ghDataBundleService.getObjectsByAliasWithHighestVersion(alias, pageable),
						String.valueOf(pageable.next().getPageNumber()));
			} catch (Exception e) {
				return new ListDataBundlesResponse(
						ga4ghDataBundleService.getObjectsByAliasWithHighestVersion(alias, pageable), "0");
			}
		}

		try {
			ga4ghDataBundleService.getAllObjectsWithHighestVersions(pageable.next());
			return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsWithHighestVersions(pageable),
					String.valueOf(pageable.next().getPageNumber()));
		} catch (Exception e) {
			return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsWithHighestVersions(pageable), "0");
		}

	}

	// GET Request - returns data bundle by data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}")
	public GetDataBundleResponse getDataBundleById(@PathVariable String data_bundle_id,
			@RequestParam(value = "version", required = false) String version) throws EntityNotFoundException {
		if (version != null) {
			return new GetDataBundleResponse(
					new DataBundle(ga4ghDataBundleService.getObjectByIdAndVersion(data_bundle_id, version)));
		} else {
			return new GetDataBundleResponse(
					new DataBundle(ga4ghDataBundleService.getObjectByIdWithHighestVersion(data_bundle_id)));
		}
	}

	// GET Request - gets all versions of a data bundle by a data_bundle_id
	@RequestMapping("/databundles/{data_bundle_id}/versions")
	public ListDataBundlesResponse getDataBundleVersions(@PathVariable String data_bundle_id,
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws EntityNotFoundException, Exception {
		return new ListDataBundlesResponse(
				ga4ghDataBundleService.getObjectByIdAndAllVersions(data_bundle_id, pageable));
	}

	// POST Request - add a data bundle
	@RequestMapping(value = "/databundles", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CreateDataBundleResponse createDataBundle(@RequestBody @Valid CreateDataBundleRequest object)
			throws Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_bundle().getCreated());
		DateTime D2 = new DateTime(object.getData_bundle().getUpdated());

		ga4ghDataBundleService.addObject(new Ga4ghDataBundle(object.getData_bundle()));
		return new CreateDataBundleResponse(object.getData_bundle().getId());
	}

	// PUT Request - updates data bundle by data_bundle_id
	@RequestMapping(value = "/databundles/{data_bundle_id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UpdateDataBundleResponse updateDataBundleById(@RequestBody @Valid UpdateDataBundleRequest object,
			@PathVariable String data_bundle_id) throws EntityNotFoundException, Exception {
		// Handling DateTime Exception
		DateTime D1 = new DateTime(object.getData_bundle().getCreated());
		DateTime D2 = new DateTime(object.getData_bundle().getUpdated());

		if (!data_bundle_id.equals(object.getData_bundle_id())) {
			throw new Exception("Conflicting data_bundle_id's in url and request body.");
		}
		ga4ghDataBundleService.updateObject(data_bundle_id, new Ga4ghDataBundle(object.getData_bundle()));
		return new UpdateDataBundleResponse(object.getData_bundle().getId());
	}

	// DELETE Request - deletes a data bundle by data_bundle_id
	@RequestMapping(value = "/databundles/{data_bundle_id}", method = RequestMethod.DELETE)
	public DeleteDataBundleResponse deleteDataBundleById(@PathVariable String data_bundle_id)
			throws EntityNotFoundException {
		ga4ghDataBundleService.deleteObject(data_bundle_id);
		return new DeleteDataBundleResponse(data_bundle_id);
	}

	// TEMPORARY METHODS - for debugging purposes

	// GET Request - temporary
	@RequestMapping("/databundles/auth")
	public String Test() {
		return "If you are seeing this, then you have been authenticated.";
	}

	// GET Request - temporary - gets all data objects and all their versions
	@RequestMapping("/databundles/allVersions")
	public ListDataBundlesResponse getAllDataBundlesAndAllVersions(
			@PageableDefault(value = 10, page = 0) Pageable pageable) throws Exception {
		return new ListDataBundlesResponse(ga4ghDataBundleService.getAllObjectsAndAllVersions(pageable));
	}

	// GET Request - temporary - gets all data objects and all their versions as
	// they are represented in the database
	@RequestMapping("/databundles/allVersions/raw")
	public List<Ga4ghDataBundle> getAllDataBundlesAndAllVersionsRaw() {
		return ga4ghDataBundleService.getAllObjectsAndAllVersionsRaw();
	}

	// DELETE Request - temporary - deletes all data bundles in the database
	@RequestMapping(value = "/databundles/all", method = RequestMethod.DELETE)
	public String deleteAllDataBundles() {
		ga4ghDataBundleService.deleteAllObjects();
		return "All Data Bundles deleted.";
	}

}
