package com.dnastack.dos.server.request;

import com.dnastack.dos.server.model.Ga4ghDataObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataObject {
	
	private Ga4ghDataObject data_object;
	
}

/*
 * This class only exists to add the words "data_object" : { ... } at the beginning of the json object
 * In order to avoid complexity I didn't want to create a column in the data base that only points to another set of columns
 * This class is only used in the Ga4ghDataObjectController class, and simply handles the format of the input
 */