package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.Ga4ghDataBundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBundles {
	
	private List<Ga4ghDataBundle> data_bundles;
	
}
