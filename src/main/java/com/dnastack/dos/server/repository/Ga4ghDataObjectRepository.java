package com.dnastack.dos.server.repository;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Ga4ghDataObjectRepository extends JpaRepository<Ga4ghDataObject, String> {
	
}