package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.repository.Ga4ghDataBundleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Ga4ghDataBundleService {

	@Autowired
	private Ga4ghDataBundleRepository ga4ghDataBundleRepository;
	
	public List<Ga4ghDataBundle> getAllObjects() {
		List<Ga4ghDataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(objects::add);
		return objects;
	}
	
	public Ga4ghDataBundle getObject(String id) throws EntityNotFoundException {
		Optional<Ga4ghDataBundle> ga4gh = ga4ghDataBundleRepository.findById(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		return ga4gh.get();
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
