package com.movietone;

import java.util.Map;

public class UserProcessor extends Processor {

	public UserProcessor(Map<String, String> params) {
		super(params);
	}

	public Response process() {
		// create response using Builder pattern
		String result = params.get("userid");
		return new Response.ResponseBuilder(params, result).build();
	}

}