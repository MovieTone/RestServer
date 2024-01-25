package com.movietone;

public class ErrorProcessor extends Processor {

	public ErrorProcessor() {
		super();
	}

	public Response process() {
		// create response using Builder pattern
		return new Response.ResponseBuilder(null, null).build();
	}

}