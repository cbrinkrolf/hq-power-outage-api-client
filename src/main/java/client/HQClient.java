package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HQClient implements Client {

	private static final String BASE_URL = "https://pannes.hydroquebec.com/pannes/donnees/v3_0/";
	// https://pannes.hydroquebec.com/pannes/donnees/v3_0/bisversion.json
	private static final String API_VERSION_SUFFIX = "bisversion.json";

	private static final String API_QUERY_PREFIX = "bismarkers";
	private static final String API_QUERY_POSTFIX = ".json";
	// 20260323103020.json

	private Logger logger;

	public HQClient(Logger logger) {
		this.logger = logger;
	}

	public long getLatestBisVersion() {

		HttpURLConnection connection;
		try {
			connection = getConnection(BASE_URL + API_VERSION_SUFFIX);
			String response = getResponse(connection);
			if (response.isEmpty()) {
				return -1;
			}
			return parseStringResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String getRecord(long version) {
		HttpURLConnection connection;
		try {
			connection = getConnection(BASE_URL + API_QUERY_PREFIX + String.valueOf(version) + API_QUERY_POSTFIX);
			String response = getResponse(connection);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private HttpURLConnection getConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setInstanceFollowRedirects(false);

		return con;
	}

	private String getResponse(HttpURLConnection connection) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			// System.out.println(content.toString());
			connection.disconnect();
			return content.toString();
		} catch (IOException e) {
			connection.disconnect();
			e.printStackTrace();
		}
		return "";
	}

	private long parseStringResponse(String response) {

		String regex = "\\d+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(response);
		if (!matcher.find()) {
			return -1;
		}
		String match = matcher.group();
		// System.out.println("match: " + match);
		try {
			long number = Long.parseLong(match);
			return number;

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
