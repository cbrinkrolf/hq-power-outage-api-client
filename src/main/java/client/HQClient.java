package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utility.MyBaseLogger;
import configuration.PropertiesManager;

public class HQClient extends MyBaseLogger implements Client {

	private String baseUrl = "";
	private String versionSuffix = "";
	private String queryPrefix = "";
	private String querySuffix = "";

	private int timeoutSeconds = 10;

	public HQClient(Logger logger) {
		this.setLogger(logger);
		initConstants();
	}

	private void initConstants() {
		PropertiesManager pm = PropertiesManager.getInstance();
		String url = pm.getPropertyValue(PropertiesManager.HQAPI_BASE_URL);
		if (url == null) {
			logMessageIfLogging(Level.SEVERE, "base url is null!");

		} else {
			if (!url.endsWith("/")) {
				url += "/";
			}
			this.baseUrl = url;
		}

		String suffix = pm.getPropertyValue(PropertiesManager.HQAPI_VERSION_SUFFIX);
		if (suffix == null) {
			logMessageIfLogging(Level.SEVERE, "version suffix is null!");
		} else {
			this.versionSuffix = suffix;
		}

		String qPrefix = pm.getPropertyValue(PropertiesManager.HQAPI_QUERY_PREFIX);
		if (qPrefix == null) {
			logMessageIfLogging(Level.SEVERE, "query prefix is null!");
		} else {
			this.queryPrefix = qPrefix;
		}

		String qSuffix = pm.getPropertyValue(PropertiesManager.HQAPI_QUERY_SUFFIX);
		if (qPrefix == null) {
			logMessageIfLogging(Level.SEVERE, "query suffix is null!");
		} else {
			this.querySuffix = qSuffix;
		}

	}

	public long getLatestBisVersion() {

		HttpURLConnection connection;
		try {
			connection = getConnection(baseUrl + versionSuffix);
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
			connection = getConnection(baseUrl + queryPrefix + String.valueOf(version) + querySuffix);
			String response = getResponse(connection);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private HttpURLConnection getConnection(String urlString) throws IOException {
		URL url = URI.create(urlString).toURL();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(timeoutSeconds * 1000);
		con.setReadTimeout(timeoutSeconds * 1000);
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
