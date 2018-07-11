package com.dnastack.dos.server.response;

import lombok.Data;

@Data
public class ErrorResponse {

	private String msg;
	private int status;

	public ErrorResponse(int status) {
		this();
		this.status = status;
	}

	public ErrorResponse(int status, Throwable ex) {
		this();
		this.status = status;
		this.msg = "An unexpected error occurred.";
	}

	public ErrorResponse(String msg, int status, Throwable ex) {
		this();
		this.status = status;
		this.msg = msg;
	}

}