package com.movietone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class Response {

	private String date;
	private Map<String, String> params;
	private String response;
	private String responseCode;

	public static class ResponseBuilder {

		private String date;
		private Map<String, String> params;
		private String responseCode;
		private String response;

		public ResponseBuilder(Map<String, String> params, String response) {
			this.params = params;
			this.response = response;
			date = getCurrentDate();
			responseCode = "OK";
			if (params == null) {
				responseCode = "ERROR";
			}
		}

		public Response build() {
			Response resp = new Response();
			resp.date = date;
			resp.params = params;
			resp.response = response;
			resp.responseCode = responseCode;
			return resp;
		}

		private String getCurrentDate() {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			sdf.setTimeZone(TimeZone.getTimeZone("CET"));
			return sdf.format(date);
		}

	}
}