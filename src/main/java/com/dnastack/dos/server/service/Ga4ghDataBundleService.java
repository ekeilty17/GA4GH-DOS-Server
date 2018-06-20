package com.dnastack.dos.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.repository.Ga4ghDataBundleRepository;

@Service
public class Ga4ghDataBundleService {

	@Autowired
	private Ga4ghDataBundleRepository ga4ghDataBundleRepository;
	
	public List<Ga4ghDataBundle> getAllObjects() {
		List<Ga4ghDataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(objects::add);
		return objects;
	}
	
	public Optional<Ga4ghDataBundle> getObject(String id) {
		return ga4ghDataBundleRepository.findById(id);
	}
	
	public void addObject(Ga4ghDataBundle object) {
		ga4ghDataBundleRepository.save(object);
	}
	
	public void updateObject(Ga4ghDataBundle object) {
		ga4ghDataBundleRepository.save(object);
	}
	
	public void deleteObject(String id) {
		ga4ghDataBundleRepository.deleteById(id);
	}
}
