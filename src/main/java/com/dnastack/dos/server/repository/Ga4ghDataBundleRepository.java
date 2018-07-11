package com.dnastack.dos.server.repository;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Ga4ghDataBundleRepository extends JpaRepository<Ga4ghDataBundle, String> {
	
	public Ga4ghDataBundle findByIdAndVersion(String id, String version);
	public Ga4ghDataBundle findByIdAndMostRecent(String id, boolean mostRecent);
	public List<Ga4ghDataBundle> findByIdEquals(String id);
	
}
