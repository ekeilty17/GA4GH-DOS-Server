package com.dnastack.dos.server.repository;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Ga4ghDataBundleRepository extends JpaRepository<Ga4ghDataBundle, String> {

	public Ga4ghDataBundle findByIdAndVersion(String id, String version);

	public Ga4ghDataBundle findByIdAndHighest(String id, boolean highest);

	public List<Ga4ghDataBundle> findByIdEquals(String id);

}
