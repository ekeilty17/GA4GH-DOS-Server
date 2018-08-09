package com.dnastack.dos.server.repository;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Ga4ghDataObjectRepository extends JpaRepository<Ga4ghDataObject, String> {

	public Ga4ghDataObject findByIdAndVersion(String id, String version);
	public Ga4ghDataObject findByIdAndHighest(String id, boolean highest);
	public List<Ga4ghDataObject> findByIdEquals(String id);

}