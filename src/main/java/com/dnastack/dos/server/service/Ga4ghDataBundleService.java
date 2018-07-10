package com.dnastack.dos.server.service;

import com.dnastack.dos.server.exception.EntityNotFoundException;
import com.dnastack.dos.server.model.Ga4ghDataBundle;
import com.dnastack.dos.server.repository.Ga4ghDataBundleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Ga4ghDataBundleService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Ga4ghDataBundleRepository ga4ghDataBundleRepository;
	
	public Ga4ghDataBundle getObject(String id) throws EntityNotFoundException {
		Ga4ghDataBundle ga4gh = ga4ghDataBundleRepository.findOne(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		return ga4gh;
	}
	
	public Page<Ga4ghDataBundle> getAllObjects(Pageable pageable) {
		return ga4ghDataBundleRepository.findAll(pageable);
	}
	
	public List<Ga4ghDataBundle> getAllObjectsAndAllVersions() {
		return null;
	}
	
	public List<Ga4ghDataBundle> getAllVersionsById(String id) {
		return null;
	}
	
	public Page<Ga4ghDataBundle> getObjectsByAlias(String alias, Pageable pageable) {
		// TODO I don't really like how I did this for a few reasons, but it works
		// 		1) The query of the database loads every object, which is bad
		//		2) I'm doing the pagination manually, which is also bad
		// Idea:
		//		Use the Ga4ghDataBundleRepository to make a custom database query to just get a list of Ga4ghDataBundles
		//		Then do the manual pagination to that list
		//		You can't set nativeQuery = True if you want the Repo to return Page<Ga4ghDataBundle>
		//		So this is the best solution I can think of
		List<Ga4ghDataBundle> objects = new ArrayList<>();
		ga4ghDataBundleRepository.findAll().forEach(o -> {
														if(o.getAliases().contains(alias)){
															objects.add(o);
														}
													});
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > objects.size() ? objects.size() : (start + pageable.getPageSize());
		return new PageImpl<Ga4ghDataBundle>(	objects.subList(start, end), 
												new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), 
												objects.size()
											);
	}
	
	public void addObject(Ga4ghDataBundle object) throws Exception {
		Ga4ghDataBundle ga4gh = ga4ghDataBundleRepository.findOne(object.getId());
		if (ga4gh != null) {
			throw new Exception("Data Bundle with that id already exists in the database. Use a PUT Request in order to update.");
		}
		ga4ghDataBundleRepository.save(object);
	}
	
	public void updateObject(Ga4ghDataBundle object) throws EntityNotFoundException {
		Ga4ghDataBundle ga4gh = ga4ghDataBundleRepository.findOne(object.getId());
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", object.getId());
		}
		/*
		// FIXME replace Integer.parseInt and != with a better comparer class
		// TODO use >, <, and == to determine which table to add to
		if (Integer.parseInt(ga4gh.getVersion()) != Integer.parseInt(object.getVersion())) {
			ga4ghDataBundleVersioningRepository.save(new Ga4ghDataBundleVersioning(ga4gh));
			ga4ghDataBundleRepository.save(object);
		}
		*/
		ga4ghDataBundleRepository.save(object);
	}
	
	public void deleteAllObjects() {
		ga4ghDataBundleRepository.deleteAll();
	}
	
	public void deleteObject(String id) throws EntityNotFoundException {
		Ga4ghDataBundle ga4gh = ga4ghDataBundleRepository.findOne(id);
		if (ga4gh == null) {
			throw new EntityNotFoundException(Ga4ghDataBundle.class, "id", id);
		}
		ga4ghDataBundleRepository.delete(id);
	}
}
