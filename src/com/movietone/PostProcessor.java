package com.movietone;

import java.util.Map;

public class PostProcessor extends Processor {

	public PostProcessor(Map<String, String> params) {
		super(params);
	}

	public Response process() {
		// create response using Builder pattern
		String result = params.get("postid");
		return new Response.ResponseBuilder(params, result).build();
	}

}