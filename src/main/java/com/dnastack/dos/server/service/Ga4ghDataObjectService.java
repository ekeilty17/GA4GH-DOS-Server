package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.repository.Ga4ghDataObjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Ga4ghDataObjectService {
	
	@Autowired
	private Ga4ghDataObjectRepository ga4ghDataObjectRepository;
	
	public List<Ga4ghDataObject> getAllObjects() {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(objects::add);
		return objects;
	}
	
	public Ga4ghDataObject getObjectNoException(String id) throws EntityNotFoundException {
		return ga4ghDataObjectRepository.findOne(id);
	}
	
	public Ga4ghDataObject getObject(String id) throws EntityNotFoundException {
		Ga4ghDataObject ga4gh = ga4ghDataObjectRepository.findOne(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataObject.class, "id", id);
		}
		return ga4gh;
	}
	
	public void addObject(Ga4ghDataObject object) {
		ga4ghDataObjectRepository.save(object);
	}
	
	public void updateObject(Ga4ghDataObject object) {
		ga4ghDataObjectRepository.save(object);
	}
	
	public void deleteObject(String id) {
		ga4ghDataObjectRepository.delete(id);
	}
	
}