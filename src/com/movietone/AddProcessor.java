package com.movietone;

import java.util.Map;

public class AddProcessor extends Processor {

	public AddProcessor(Map<String, String> params) {
		super(params);
	}

	public Response process() {
		// create response using Builder pattern
		String result = Integer.toString(Integer.parseInt(params.get("a")) + Integer.parseInt(params.get("b")));
		return new Response.ResponseBuilder(params, result).build();
	}

}