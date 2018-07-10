package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.repository.Ga4ghDataObjectRepository;

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
	
	public Ga4ghDataObject getObject(String id) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findOne(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		return ga4gh;
	}
	
	public Page<Ga4ghDataObject> getAllObjects(Pageable pageable) {
		return ga4ghDataObjectRepository.findAll(pageable);
	}
	
	public List<Ga4ghDataObject> getAllObjectsAndAllVersions() {
		return null;
	}
	
	public List<Ga4ghDataObject> getAllVersionsById(String id) {
		return null;
	}
	
	public Page<Ga4ghDataObject> getObjectsByAlias(String alias, Pageable pageable) {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(o -> {
														if(o.getAliases().contains(alias)){
															objects.add(o);
														}
													});
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > objects.size() ? objects.size() : (start + pageable.getPageSize());
		return new PageImpl<Ga4ghDataObject>(	objects.subList(start, end), 
												new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), 
												objects.size()
											);
	}
	
	public void addObject(Ga4ghDataObject object) throws Exception {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findOne(object.getId());
		if (ga4gh != null) {
			throw new Exception("Data Object with that id already exists in the database. Use a PUT Request in order to update.");
		}
		ga4ghDataObjectRepository.save(object);
	}
	
	public void updateObject(Ga4ghDataObject object) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findOne(object.getId());
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", object.getId());
		}
		ga4ghDataObjectRepository.save(object);
	}
	
	public void deleteAllObjects() {
		ga4ghDataObjectRepository.deleteAll();
	}
	
	public void deleteObject(String id) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findOne(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		ga4ghDataObjectRepository.delete(id);
	}
	
}