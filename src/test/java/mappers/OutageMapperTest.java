package mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.Assertions;

import entities.Outage;

class OutageMapperTest {

	private final Path resourceDirectory = Paths.get("src", "test", "resources");
	private final OutageMapper mapper = new OutageMapper(null);

	@Test
	public void testGetMatches() {

		// assertTrue(true);
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testRawJSON2Outages() {

		Path filePath = resourceDirectory.resolve("test.json");

		String content = "";

		try {
			content = Files.readString(filePath);
			// System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Outage> list = mapper.rawJSON2Outages(content);
		assertEquals(24, list.size());

	}

}
