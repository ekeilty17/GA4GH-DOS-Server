package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.DataObject;
import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.repository.Ga4ghDataObjectRepository;
import com.dnastack.dos.server.repository.Ga4ghURLRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Ga4ghDataObjectService {
	
	@Autowired
	private Ga4ghDataObjectRepository ga4ghDataObjectRepository;
	
	@Autowired
	private Ga4ghURLRepository ga4ghURLRepository;
	
	// Helper - converts list of DataBundle objects to a paginated list
	public Page<DataObject> paginateList(List<DataObject> objectList, Pageable pageable) {
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > objectList.size() ? objectList.size() : (start + pageable.getPageSize());
		return new PageImpl<DataObject>(	objectList.subList(start, end), 
											new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), 
											objectList.size()
										);
	}
	
		
	// GET Specific Object
	public Ga4ghDataObject getObjectByIdAndVersion(String id, String version) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findByIdAndVersion(id, version);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
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
	public Page<DataObject> getObjectByIdAndAllVersions(String id, Pageable pageable) throws EntityNotFoundException {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findByIdEquals(id).forEach(o -> objects.add(new DataObject(o)));
		if (objects.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		return paginateList(objects, pageable);
	}
	
	public Page<DataObject> getAllObjectsWithMostRecentVersions(Pageable pageable) {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
														if (o.isMostRecent() == true) {
															objects.add(new DataObject(o));
														}
													});
		return paginateList(objects, pageable);
	}
	
	public Page<DataObject> getAllObjectsAndAllVersions(Pageable pageable) {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> objects.add(new DataObject(o)));
		return paginateList(objects, pageable);
	}
	
	public List<Ga4ghDataObject> getAllObjectsAndAllVersionsRaw() {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> objects.add(o));
		return objects;
	}
	
	public Page<DataObject> getObjectsByAlias(String alias, Pageable pageable) {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
														if(o.getAliases().contains(alias)){
															objects.add(new DataObject(o));
														}
													});
		return paginateList(objects, pageable);
	}
	
	public Page<DataObject> getObjectsByAliasWithMostRecentVersion(String alias, Pageable pageable) {
		List<DataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
														if(o.isMostRecent() && o.getAliases().contains(alias)){
															objects.add(new DataObject(o));
														}
													});
		return paginateList(objects, pageable);
	}
	
	
	
	// POST
	public void addObject(Ga4ghDataObject object) throws Exception {
		List<Ga4ghDataObject> ga4ghList = ga4ghDataObjectRepository.findByIdEquals(object.getId());
		if (!ga4ghList.isEmpty()) {
			throw new Exception("Data Object with that id already exists in the database. Use a PUT Request in order to update.");
		}
		ga4ghDataObjectRepository.save(object);
	}
	
	
	// PUT
	public void updateObject(Ga4ghDataObject object) throws EntityNotFoundException {
		List<Ga4ghDataObject> ga4ghList = ga4ghDataObjectRepository.findByIdEquals(object.getId());
		if (ga4ghList.isEmpty()) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", object.getId());
		}
		
		// Updating mostRecent variable
		Ga4ghDataObject ga4ghMostRecent = ga4ghDataObjectRepository.findByIdAndMostRecent(object.getId(), true);
		ga4ghMostRecent.setMostRecent(false);
		ga4ghDataObjectRepository.save(ga4ghMostRecent);
		
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