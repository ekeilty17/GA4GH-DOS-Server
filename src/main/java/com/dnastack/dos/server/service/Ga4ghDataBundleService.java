package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataBundle;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.repository.Ga4ghDataBundleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Ga4ghDataBundleService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Ga4ghDataBundleRepository ga4ghDataBundleRepository;

	// Helper - converts list of DataBundle objects to a paginated list
	public Page<DataBundle> paginateList(List<DataBundle> objectList, Pageable pageable) throws InvalidParameterException {
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > objectList.size() ? objectList.size()
				: (start + pageable.getPageSize());
		
		if (start >= end) {
			throw new InvalidParameterException("Page does not exist, page size is too large.");
		}
		
		return new PageImpl<DataBundle>(objectList.subList(start, end),
				new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
				objectList.size());
	}

	// GET Specific Object
	public Ga4ghDataBundle getObjectByIdAndVersion(String id, String version) throws EntityNotFoundException {
		Ga4ghDataBundle ga4gh = ga4ghDataBundleRepository.findByIdAndVersion(id, version);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "version", version);
		}
		return ga4gh;
	}

	public Ga4ghDataBundle getObjectByIdWithMostRecentVersion(String id) throws EntityNotFoundException {
		List<Ga4ghDataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findByIdEquals(id).forEach(o -> {
			if (o.isMostRecent() == true) {
				objects.add(o);
			}
		});
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		return objects.get(0);
	}

	// GET List of Objects by some criteria
	public Page<DataBundle> getObjectByIdAndAllVersions(String id, Pageable pageable) throws EntityNotFoundException, Exception {
		List<DataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findByIdEquals(id).forEach(o -> objects.add(new DataBundle(o)));
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		return paginateList(objects, pageable);
	}

	public Page<DataBundle> getAllObjectsWithMostRecentVersions(Pageable pageable) throws Exception {
		List<DataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> {
			if (o.isMostRecent() == true) {
				objects.add(new DataBundle(o));
			}
		});
		return paginateList(objects, pageable);
	}

	public Page<DataBundle> getAllObjectsAndAllVersions(Pageable pageable) throws Exception {
		List<DataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> objects.add(new DataBundle(o)));
		return paginateList(objects, pageable);
	}

	public List<Ga4ghDataBundle> getAllObjectsAndAllVersionsRaw() {
		List<Ga4ghDataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> objects.add(o));
		return objects;
	}

	public Page<DataBundle> getObjectsByAlias(String alias, Pageable pageable) throws Exception {
		// TODO I don't really like how I did this for a few reasons, but it works
		// 1) The query of the database loads every object, which is bad
		// 2) I'm doing the pagination manually, which is also bad
		// Idea:
		// Use the Ga4ghDataBundleRepository to make a custom database query to just get
		// a list of Ga4ghDataBundles
		// Then do the manual pagination to that list
		// You can't set nativeQuery = True if you want the Repo to return
		// Page<Ga4ghDataBundle>
		// So this is the best solution I can think of
		List<DataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> {
			if (o.getAliases().contains(alias)) {
				objects.add(new DataBundle(o));
			}
		});
		return paginateList(objects, pageable);
	}

	public Page<DataBundle> getObjectsByAliasWithMostRecentVersion(String alias, Pageable pageable) throws Exception{
		List<DataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> {
			if (o.isMostRecent() && o.getAliases().contains(alias)) {
				objects.add(new DataBundle(o));
			}
		});
		return paginateList(objects, pageable);
	}

	// POST
	public void addObject(Ga4ghDataBundle object) throws Exception {
		List<Ga4ghDataBundle> ga4ghList = ga4ghDataBundleRepository.findByIdEquals(object.getId());
		if (!ga4ghList.isEmpty()) {
			throw new Exception(
					"Data Bundle with that id already exists in the database. Use a PUT Request in order to update.");
		}
		ga4ghDataBundleRepository.save(object);
	}

	// PUT
	public void updateObject(String data_bundle_id, Ga4ghDataBundle object) throws EntityNotFoundException, Exception {
		// TODO can turn findByIdEquals into a boolean
		List<Ga4ghDataBundle> objects_DataBundleId = ga4ghDataBundleRepository.findByIdEquals(data_bundle_id);
		if (objects_DataBundleId.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "data_bundle_id", data_bundle_id);
		}

		if (!data_bundle_id.equals(object.getId())) {
			List<Ga4ghDataBundle> objects_Id = ga4ghDataBundleRepository.findByIdEquals(object.getId());
			if (!objects_Id.isEmpty()) {
				throw new Exception(
						"Data Bundle with that id already exists in the database. Overriding this Data Bundle's id would conflict with the id of another Data Bundle.");
			}

			// Updating id of all versions of the data bundle
			objects_DataBundleId.forEach(o -> ga4ghDataBundleRepository.save(new Ga4ghDataBundle(false, object.getId(),
					new ArrayList<>(o.getData_object_ids()), o.getCreated(), o.getUpdated(), o.getVersion(),
					new ArrayList<>(o.getChecksums()), o.getDescription(), new ArrayList<>(o.getAliases()),
					new HashMap<>(o.getSystem_metadata()), new HashMap<>(o.getUser_metadata()))));
			// Deleting objects with old id
			deleteObject(data_bundle_id);
		} else {
			// Updating mostRecent variable
			Ga4ghDataBundle ga4ghMostRecent = ga4ghDataBundleRepository.findByIdAndMostRecent(object.getId(), true);
			ga4ghMostRecent.setMostRecent(false);
			ga4ghDataBundleRepository.save(ga4ghMostRecent);
		}
		
		// Saving new objects
		ga4ghDataBundleRepository.save(object);
		
	}

	// DELETE
	public void deleteAllObjects() {
		ga4ghDataBundleRepository.deleteAll();
	}

	public void deleteObject(String id) throws EntityNotFoundException {
		List<Ga4ghDataBundle> objects = ga4ghDataBundleRepository.findByIdEquals(id);
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		objects.forEach(o -> ga4ghDataBundleRepository.delete(o.getId() + 'v' + o.getVersion()));
	}
	
}
