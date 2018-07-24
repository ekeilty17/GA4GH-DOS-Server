package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataObject;
import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.model.Ga4ghURL;
import com.dnastack.dos.server.repository.Ga4ghDataObjectRepository;
import com.dnastack.dos.server.repository.Ga4ghURLRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Ga4ghDataObjectService {

	@Autowired
	private Ga4ghDataObjectRepository ga4ghDataObjectRepository;

	@Autowired
	private Ga4ghURLRepository ga4ghURLRepository;

	// Helper - converts list of DataBundle objects to a paginated list
	public Page<DataObject> paginateList(List<DataObject> objectList, Pageable pageable)
			throws InvalidParameterException {
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > objectList.size() ? objectList.size()
				: (start + pageable.getPageSize());

		if (start >= end) {
			throw new InvalidParameterException("Page does not exist, page size is too large.");
		}

		return new PageImpl<DataObject>(objectList.subList(start, end),
				new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
				objectList.size());
	}
	
	// Helper - compare version Strings of the form x.x.x
	public boolean isVersionCorrectForm(String v) {
		return v.matches("\\d+|\\d+(\\.\\d+)|\\d+(\\.\\d+(\\.\\d+))");
	}
	public boolean isFirstVersionGreaterOrEqual(String v1, String v2) throws InvalidParameterException {
		
		if (!isVersionCorrectForm(v1) || !isVersionCorrectForm(v2)) {
			throw new InvalidParameterException("Invalid version form. Version numbers must be of the form x.x.x");
		}
		
		String[] v1Parts = v1.split("\\.");
		String[] v2Parts = v2.split("\\.");
		 
		int length = Math.max(v1Parts.length, v2Parts.length);
		for(int i = 0; i < length; i++) {
		    int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
		    int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
		    
		    if (v1Part < v2Part)
		        return false;
		    if (v1Part > v2Part)
		        return true;
		}
		return true;
	}

	// GET Specific Object
	public Ga4ghDataObject getObjectByIdAndVersion(String id, String version) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findByIdAndVersion(id, version);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id and version", id + 'v' + version);
		}
		return ga4gh;
	}

	public Ga4ghDataObject getObjectByIdWithMostRecentVersion(String id) throws EntityNotFoundException {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findByIdEquals(id).forEach(o -> {
			if (o.isMostRecent() == true) {
				objects.add(o);
			}
		});
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		return objects.get(0);
	}

	// GET List of Objects by some criteria
	public Page<DataObject> getObjectByIdAndAllVersions(String id, Pageable pageable)
			throws EntityNotFoundException, Exception {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findByIdEquals(id).forEach(o -> objects.add(new DataObject(o)));
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		return paginateList(objects, pageable);
	}

	public Page<DataObject> getAllObjectsWithMostRecentVersions(Pageable pageable) throws Exception {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
			if (o.isMostRecent() == true) {
				objects.add(new DataObject(o));
			}
		});
		return paginateList(objects, pageable);
	}

	public Page<DataObject> getAllObjectsAndAllVersions(Pageable pageable) throws Exception {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> objects.add(new DataObject(o)));
		return paginateList(objects, pageable);
	}

	public List<Ga4ghDataObject> getAllObjectsAndAllVersionsRaw() {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> objects.add(o));
		return objects;
	}

	public Page<DataObject> getObjectsByAlias(String alias, Pageable pageable) throws Exception {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
			if (o.getAliases().contains(alias)) {
				objects.add(new DataObject(o));
			}
		});
		return paginateList(objects, pageable);
	}

	public Page<DataObject> getObjectsByAliasWithMostRecentVersion(String alias, Pageable pageable) throws Exception {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
			if (o.isMostRecent() && o.getAliases().contains(alias)) {
				objects.add(new DataObject(o));
			}
		});
		return paginateList(objects, pageable);
	}

	// POST
	public void addObject(Ga4ghDataObject object) throws Exception {
		List<Ga4ghDataObject> ga4ghList = ga4ghDataObjectRepository.findByIdEquals(object.getId());
		if (!ga4ghList.isEmpty()) {
			throw new Exception(
					"Data Object with that id already exists in the database. Use a PUT Request in order to update.");
		}
		if (!isVersionCorrectForm(object.getVersion())) {
			throw new InvalidParameterException("Invalid version form. Version numbers must be of the form x.x.x");
		}
		ga4ghDataObjectRepository.save(object);
	}

	// PUT
	public void updateObject(String data_object_id, Ga4ghDataObject object) throws EntityNotFoundException, Exception {
		List<Ga4ghDataObject> objects_DataObjectId = ga4ghDataObjectRepository.findByIdEquals(data_object_id);
		if (objects_DataObjectId.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "data_object_id", data_object_id);
		}

		if (!data_object_id.equals(object.getId())) {
			List<Ga4ghDataObject> objects_Id = ga4ghDataObjectRepository.findByIdEquals(object.getId());
			if (!objects_Id.isEmpty()) {
				throw new Exception(
						"Data Object with that id already exists in the database. Overriding this Data Object's id would conflict with the id of another Data Object.");
			}

			// Updating id of all versions of the data object
			// FIXME contents of url does not get copied
			objects_DataObjectId.forEach(o -> {
				// Manually copying URLs
				List<Ga4ghURL> urls = new ArrayList<>();
				o.getUrls().forEach(u -> urls.add(new Ga4ghURL(u.getUrl(), new HashMap<>(u.getSystem_metadata()),
						new HashMap<>(u.getUser_metadata()))));
				ga4ghDataObjectRepository.save(new Ga4ghDataObject(false, object.getId(), o.getName(), o.getSize(),
						o.getCreated(), o.getUpdated(), o.getVersion(), o.getMimeType(),
						new ArrayList<>(o.getChecksums()), urls, o.getDescription(), new ArrayList<>(o.getAliases())));
			});

			// Deleting objects with old id
			deleteObject(data_object_id);
		} else {
			// Updating mostRecent variable
			Ga4ghDataObject ga4ghMostRecent = ga4ghDataObjectRepository.findByIdAndMostRecent(object.getId(), true);
			
			// Comparing version numbers and setting correct object to highest version
			if (isFirstVersionGreaterOrEqual(object.getVersion(), ga4ghMostRecent.getVersion())) {
				ga4ghMostRecent.setMostRecent(false);
			} else {
				object.setMostRecent(false);
			}
			
			ga4ghDataObjectRepository.save(ga4ghMostRecent);
		}

		// Saving new objects
		ga4ghDataObjectRepository.save(object);
	}

	// DELETE
	public void deleteAllObjects() {
		ga4ghDataObjectRepository.deleteAll();
		ga4ghURLRepository.deleteAll();
	}

	public void deleteObject(String id) throws EntityNotFoundException {
		List<Ga4ghDataObject> objects = ga4ghDataObjectRepository.findByIdEquals(id);
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		objects.forEach(o -> o.getUrls().forEach(u -> ga4ghURLRepository.delete(u.getId())));
		objects.forEach(o -> ga4ghDataObjectRepository.delete(o.getId() + 'v' + o.getVersion()));
	}

}