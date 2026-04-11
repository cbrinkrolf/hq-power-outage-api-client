package configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PropertiesManagerTest {

	@TempDir
	static Path sharedTempDir;

	PropertiesManager pm;

	@Test
	@Order(1)
	void test_create_External_Config_File() {

		Path configFile = sharedTempDir.resolve("config.properties");
		assertTrue(!Files.exists(configFile));

		PropertiesManager.setExternalConfigDirectory(sharedTempDir);
		pm = PropertiesManager.getInstance();

		pm.getProperties();
		assertTrue(Files.exists(configFile));

	}

	@Test
	@Order(2)
	void test_mandatory_properties_Exist() {

		testPropertyNotNullNotEmpty(PropertiesManager.LOGS_FOLDER);
		testPropertyNotNullNotEmpty(PropertiesManager.STATISTICS_FOLDER);
		testPropertyNotNullNotEmpty(PropertiesManager.HQAPI_BASE_URL);
		testPropertyNotNullNotEmpty(PropertiesManager.HQAPI_VERSION_SUFFIX);
		testPropertyNotNullNotEmpty(PropertiesManager.HQAPI_QUERY_PREFIX);
		testPropertyNotNullNotEmpty(PropertiesManager.HQAPI_QUERY_SUFFIX);
		testPropertyNotNullNotEmpty(PropertiesManager.DAEMON_REFRESH_SECONDS);

	}

	@Test
	@Order(3)
	void test_Not_Existing_Property_Is_Null() {
		Set<Object> keys = pm.getProperties().keySet();
		String testString = String.valueOf(System.currentTimeMillis());
		while (keys.contains(testString)) {
			testString = String.valueOf(System.currentTimeMillis());
		}
		assertNull(pm.getPropertyValue(testString));
	}

	private void testPropertyNotNullNotEmpty(String property) {
		String value = pm.getPropertyValue(property);
		assertNotNull(value);
		assertTrue(!value.strip().isEmpty());
	}

}
