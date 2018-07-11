package com.dnastack.dos.server.repository;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Ga4ghDataObjectRepository extends JpaRepository<Ga4ghDataObject, String> {
	
	public Ga4ghDataObject findByIdAndVersion(String id, String version);
	public Ga4ghDataObject findByIdAndMostRecent(String id, boolean mostRecent);
	public List<Ga4ghDataObject> findByIdEquals(String id);
	
}