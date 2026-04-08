package export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import Utility.MyBaseLogger;

public class HTTPPostExporter extends MyBaseLogger implements DataSender {

	private final String apiEndpoint;

	private String payload = "";
	private boolean successfulSent = false;
	private int responseCode = -1;

	public HTTPPostExporter(String apiEndpoint) {
		this(apiEndpoint, null);
	}

	public HTTPPostExporter(String apiEndpoint, Logger logger) {
		this.apiEndpoint = apiEndpoint;
		this.setLogger(logger);
	}

	@Override
	public void sendData() {
		successfulSent = false;
		responseCode = -1;
		HttpURLConnection con = createConnection();
		if (con != null) {
			this.sendPayload(con);
		}
		this.payload = "";
	}

	public void setJSONPayload(String jsonString) {
		this.payload = jsonString;
	}

	private HttpURLConnection createConnection() {
		URL url;
		HttpURLConnection con = null;
		try {
			url = URI.create(this.apiEndpoint).toURL();
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return con;
	}

	private boolean sendPayload(HttpURLConnection con) {

		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8))) {
			bw.write(payload);
			bw.flush();
			this.responseCode = con.getResponseCode();
			successfulSent = true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isSuccessfulSent() {
		return successfulSent;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
