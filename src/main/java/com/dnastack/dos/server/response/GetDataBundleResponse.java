package com.dnastack.dos.server.response;

import com.dnastack.dos.server.model.DataBundle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataBundleResponse {

	private DataBundle data_bundle;

}
