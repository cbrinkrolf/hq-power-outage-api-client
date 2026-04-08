package export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JSONFileWriterTest {

	@TempDir
	static Path sharedTempDir;

	@Test
	@Order(1)
	void test_createFolder() {
		Path folder = sharedTempDir.resolve("statistics/");

		JSONFileWriter writer = new JSONFileWriter(null, folder);
		writer.sendData();

		assertTrue(Files.exists(folder));
		assertTrue(Files.isDirectory(folder));
		// assertAll(() -> assertTrue(Files.exists(numbers)), () -> assertLinesMatch(lines,
		// Files.readAllLines(numbers)));
	}

	@Test
	@Order(2)
	void test_Folderexists() {
		Path folder = sharedTempDir.resolve("statistics/");

		assertTrue(Files.exists(folder));
		assertTrue(Files.isDirectory(folder));
		File f = folder.toFile();
		long modifiedOld = f.lastModified();

		JSONFileWriter writer = new JSONFileWriter(null, folder);
		writer.sendData();

		assertTrue(Files.exists(folder));
		assertTrue(Files.isDirectory(folder));
		assertEquals(modifiedOld, f.lastModified());
	}

}
