package com.dnastack.dos.server.service;

import com.dnastack.dos.server.model.Ga4ghDataObject;
import com.dnastack.dos.server.repository.Ga4ghDataObjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class Ga4ghDataObjectService {
	
	@Autowired
	private Ga4ghDataObjectRepository ga4ghDataObjectRepository;
	
	public List<Ga4ghDataObject> getAllObjects() {
		List<Ga4ghDataObject> objects = new ArrayList<>();
		ga4ghDataObjectRepository.findAll().forEach(objects::add);
		return objects;
	}
	
	public Optional<Ga4ghDataObject> getObject(String id) {
		return ga4ghDataObjectRepository.findById(id);
	}
	
	public void addObject(Ga4ghDataObject object) {
		ga4ghDataObjectRepository.save(object);
	}
	
	public void updateObject(Ga4ghDataObject object) {
		ga4ghDataObjectRepository.save(object);
	}
	
	public void deleteObject(String id) {
		ga4ghDataObjectRepository.deleteById(id);
	}
	
}