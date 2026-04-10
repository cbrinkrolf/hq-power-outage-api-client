package configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesManager {

	public static final String STATISTICS_FOLDER = "statistics.folder";
	public static final String LOGS_FOLDER = "logs.folder";
	public static final String HQAPI_BASE_URL = "hqapi.base.url";
	public static final String HQAPI_VERSION_SUFFIX = "hqapi.version.suffix";
	public static final String HQAPI_QUERY_PREFIX = "hqapi.query.prefix";
	public static final String HQAPI_QUERY_SUFFIX = "hqapi.query.suffix";

	public static final String POST_EXPORTER_ENDPOINT = "postexporter.endpoint";
	public static final String DAEMON_REFRESH_SECONDS = "daemon.refresh.seconds";

	private static final String internalConfigFile = "config.properties";
	private static final String externalConfigFile = "config.properties";

	private static PropertiesManager instance = null;

	private Properties properties;

	private PropertiesManager() {
		createExternalConfigurationFileIfNotExist();
	}

	public static PropertiesManager getInstance() {
		if (instance == null) {
			instance = new PropertiesManager();
		}
		return instance;
	}

	private boolean existsInternalConfigurationFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResource(internalConfigFile) != null;
	}

	private boolean existsExternalConfigurationFile() {
		return Files.exists(Path.of(externalConfigFile));
	}

	private boolean createExternalConfigurationFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader.getResource(internalConfigFile) == null) {
			return false;
		}
		Properties properties = PropertiesReader.getConfiguration(loader.getResourceAsStream(internalConfigFile));
		Path p = Path.of(externalConfigFile);

		return PropertiesWriter.writeConfiguration(properties, p);
	}

	private boolean createExternalConfigurationFileIfNotExist() {
		if (existsExternalConfigurationFile()) {
			return false;
		}
		if (!existsInternalConfigurationFile()) {
			return false;
		}
		return createExternalConfigurationFile();
	}

	private void loadProperties() {
		try {
			Properties defaultProps = PropertiesReader
					.getConfiguration(Files.newInputStream(Path.of(internalConfigFile)));
			this.properties = defaultProps;
			this.properties = PropertiesReader.getConfiguration(Files.newInputStream(Path.of(externalConfigFile)),
					defaultProps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPropertyValue(String property) {
		if (this.properties == null) {
			loadProperties();
		}
		return properties.getProperty(property);
	}

	public Properties getProperties() {
		if (this.properties == null) {
			loadProperties();
		}
		return properties;
	}

}
