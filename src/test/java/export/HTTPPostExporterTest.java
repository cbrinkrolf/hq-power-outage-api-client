package export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entities.Outage;
import mappers.OutageMapper;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okhttp3.HttpUrl;

class HTTPPostExporterTest {

	@Test
	void test() throws Exception {
		// Create a MockWebServer. These are lean enough that you can create a new
		// instance for every unit test.
		MockWebServer server = new MockWebServer();
		System.out.println("test");

		// Schedule some responses.
		server.enqueue(new MockResponse.Builder().body("hello, world!").build());
		server.enqueue(new MockResponse.Builder().body("sup, bra?").build());
		server.enqueue(new MockResponse.Builder().body("yo dog").build());
		System.out.println("test");
		// Start the server.
		server.start();

		// Ask the server for its URL. You'll need this to make HTTP requests.
		HttpUrl baseUrl = server.url("/v1/chat/");
		System.out.println(baseUrl);
		int port = baseUrl.port();

		// Exercise your application code, which should make those HTTP requests.
		// Responses are returned in the same order that they are enqueued.

		System.out.println("sent");

		URL url = new URL("http://localhost:" + port + "/v1/chat/");
		// URL url = new URL("http://example.com");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("param1", "val");

		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes("test test test");
		out.flush();
		out.close();
		int status = con.getResponseCode();

		// Optional: confirm that your app made the HTTP requests you were expecting.
		RecordedRequest request1 = server.takeRequest();
		System.out.println("Header: " + request1.getHeaders());
		System.out.println("Body: " + request1.getBody());
		System.out.println("Method: " + request1.getMethod());
		// assertEquals("/v1/chat/messages/", request1.getUrl().encodedPath());
		// assertNotNull(request1.getHeaders().get("Authorization"));

		// RecordedRequest request2 = server.takeRequest();

		// RecordedRequest request3 = server.takeRequest();
		// assertEquals("/v1/chat/messages/3", request3.getUrl().encodedPath());
		// System.out.println("status: " + status);
		// System.out.println("body: " + request3.getBody());
		// assertEquals("/v1/chat/messages/2", request2.getUrl().encodedPath());
		// System.out.println("request 2 sent");

		// System.out.println(request3.getHeaders());
		// Shut down the server. Instances cannot be reused.
		server.close();
	}

	@Test
	void test_ValidJsonList_sendData() throws IOException, InterruptedException {
		MockWebServer server = new MockWebServer();

		// Schedule some responses.
		server.enqueue(new MockResponse.Builder().body("hello, world!").code(200).build());
		// server.enqueue(new MockResponse.Builder().body("sup, bra?").build());
		// server.enqueue(new MockResponse.Builder().body("yo dog").build());
		// Start the server.
		server.start();

		// Ask the server for its URL. You'll need this to make HTTP requests.
		HttpUrl baseUrl = server.url("/test");

		// Exercise your application code, which should make those HTTP requests.
		// Responses are returned in the same order that they are enqueued.
		OutageMapper mapper = new OutageMapper(null);
		List<Outage> list = getOutageList();
		String json = mapper.parseOutages2JSON(list);

		HTTPPostExporter client = new HTTPPostExporter(baseUrl.toString());
		client.setJSONPayload(json);
		client.sendData();

		RecordedRequest request1 = server.takeRequest();
		// System.out.println("Header: " + request1.getHeaders());
		// System.out.println("Body: " + request1.getBody().utf8());
		// System.out.println("Method: " + request1.getMethod());
		String body = request1.getBody().utf8();

		assertEquals("application/json", request1.getHeaders().get("Content-Type"));
		assertEquals("POST", request1.getMethod());

		List<Outage> importedList = mapper.parseJSONOutages2Outages(body);
		assertEquals(list.size(), importedList.size());
		assertEquals(list.getFirst().customersAffected(), importedList.getFirst().customersAffected());
		assertEquals(200, client.getResponseCode());
		assertEquals(true, client.isSuccessfulSent());
		server.close();
	}

	@Test
	void test_invalidURL() {
		HTTPPostExporter client = new HTTPPostExporter("-1");
		client.sendData();
		assertEquals(false, client.isSuccessfulSent());
		assertTrue(client.getResponseCode() < 100);

	}

	private List<Outage> getOutageList() {
		LocalDateTime start = LocalDateTime.of(2026, 12, 30, 13, 14, 15);
		Outage o1 = new Outage(1, start, -71.5, 46.5);
		Outage o2 = new Outage(22, start, -71.6, 46.6);
		Outage o3 = new Outage(333, start, -71.7, 46.7);
		return Arrays.asList(o1, o2, o3);
	}

}
