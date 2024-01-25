package com.movietone;

import java.util.Map;

public abstract class Processor {

	protected Map<String, String> params;

	public Processor(Map<String, String> params) {
		this.params = params;
	}

	public Processor() {

	}

	public abstract Response process();

}