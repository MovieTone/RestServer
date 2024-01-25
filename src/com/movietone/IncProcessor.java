package com.movietone;

import java.util.Map;

public class IncProcessor extends Processor {

	public IncProcessor(Map<String, String> params) {
		super(params);
	}

	public Response process() {
		// create response using Builder pattern
		String result = Integer.toString(Integer.parseInt(params.get("a")) + 1);
		return new Response.ResponseBuilder(params, result).build();
	}

}