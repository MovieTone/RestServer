package com.movietone;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class Server {

	private static Server instance;

	private Server() {

	}

	// Singleton
	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public void run() {
		ServerSocket ding;
		Socket dong = null;
		String response = "";
		BufferedOutputStream out = null;
		PrintWriter writer = null;
		try {
			ding = new ServerSocket(1299);
			System.out.println("Opened socket " + 1299);
			while (true) {
				// keeps listening for new clients, one at a time
				try {
					dong = ding.accept(); // waits for client here
				} catch (IOException e) {
					System.out.println("Error opening socket");
					System.exit(1);
				}
				InputStream stream = dong.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(stream));
				try {
					// read the first line to get the request method, URI and HTTP version
					String line = in.readLine();
					System.out.println("----------REQUEST START---------");
					System.out.println(line);

					// read only headers
					line = in.readLine();
					while (line != null && line.trim().length() > 0) {
						int index = line.indexOf(": ");
						if (index > 0) {
							System.out.println(line);
						} else {
							break;
						}

						if (line.split(":")[0].equalsIgnoreCase("Referer")) {
							response = process(line);
						}

						line = in.readLine();
					}
					System.out.println("----------REQUEST END---------\n\n");
				} catch (IOException e) {
					System.out.println("Error reading");
					System.exit(1);
				}
				
				out = new BufferedOutputStream(dong.getOutputStream());
				
				// char output to the client
				writer = new PrintWriter(out, true);
				
				// each response always has the status line, date, and server name
				writer.println("HTTP/1.1 200 OK");
				writer.println("main.java.com.movietone.com.movietone.Server: TEST");
				writer.println("Connection: close");
				writer.println("Content-type: text/html");
				writer.println("");
				
				// body of the response
				writer.println(response);
				writer.flush();
				writer.close();
				out.close();
				dong.close();
			}
		} catch (IOException e) {
			System.out.println("Error opening socket");
			System.exit(1);
		} finally {
			writer.close();
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String process(String line) {
		Gson gson = new Gson();
		Response response = null;
		try {
			String[] tokens = line.split(": ");
			String request = tokens[1].split("http://localhost:1299/")[1];
			String left = request.split("\\?")[0];
			String right = request.split("\\?")[1];
			System.out.println(left);

			switch (left) {
			case "math/add":
				if (right.contains("&")) {
					String[] rightTokens = right.split("[=&]");
					if (rightTokens[0].equalsIgnoreCase("a") && rightTokens[2].equalsIgnoreCase("b")) {
						String a = rightTokens[1];
						String b = rightTokens[3];
						Map<String, String> params = new HashMap<String, String>();
						params.put("a", a);
						params.put("b", b);
						response = getProcessor("add", params).process();
					}
				} else if (right.split("=")[0].equalsIgnoreCase("a")) {
					String a = right.split("=")[1];
					Map<String, String> params = new HashMap<String, String>();
					params.put("a", a);
					response = getProcessor("inc", params).process();
				} else {
					response = getProcessor("err", null).process();
				}
				break;
			case "posts":
				if (right.split("=")[0].equalsIgnoreCase("postid")) {
					String postId = right.split("=")[1];
					Map<String, String> params = new HashMap<String, String>();
					params.put("postid", postId);
					response = getProcessor("post", params).process();
				} else {
					response = getProcessor("err", null).process();
				}
				break;
			case "users":
				if (right.split("=")[0].equalsIgnoreCase("userid")) {
					String postId = right.split("=")[1];
					Map<String, String> params = new HashMap<String, String>();
					params.put("userid", postId);
					response = getProcessor("user", params).process();
				} else {
					response = getProcessor("err", null).process();
				}
				break;
			default:
				response = getProcessor("err", null).process();
				break;
			}
		} catch (Exception e) {
			response = getProcessor("err", null).process();
		}

		return gson.toJson(response);
	}

	public Processor getProcessor(String type, Map<String, String> params) {
		Processor processor = null;

		if ("post".equalsIgnoreCase(type)) {
			processor = new PostProcessor(params);
		} else if ("user".equalsIgnoreCase(type)) {
			processor = new UserProcessor(params);
		} else if ("add".equalsIgnoreCase(type)) {
			processor = new AddProcessor(params);
		} else if ("inc".equalsIgnoreCase(type)) {
			processor = new IncProcessor(params);
		} else if ("err".equalsIgnoreCase(type)) {
			processor = new ErrorProcessor();
		}

		return processor;
	}

	public static void main(String[] args) throws IOException {
		Server server = Server.getInstance();
		server.run();
	}

}
