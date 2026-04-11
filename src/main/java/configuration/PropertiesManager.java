package configuration;

import java.io.IOException;
import java.io.InputStream;
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

	public static final String INTERNAL_CONFIG_FILE = "config.properties";
	public static final String EXTERNAL_CONFIG_FILE = "config.properties";

	private static Path externalConfigPath = null;

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
		return getInternalConfigFileStream() != null;
	}

	private boolean existsExternalConfigurationFile() {
		return Files.exists(getExternalConfigFilePath());
	}

	private boolean createExternalConfigurationFile() {
		if (getInternalConfigFileStream() == null) {
			return false;
		}
		Properties properties = PropertiesReader.getConfiguration(getInternalConfigFileStream());
		return PropertiesWriter.writeConfiguration(properties, getExternalConfigFilePath());
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
			Properties defaultProps = PropertiesReader.getConfiguration(getInternalConfigFileStream());
			this.properties = defaultProps;
			this.properties = PropertiesReader.getConfiguration(Files.newInputStream(getExternalConfigFilePath()),
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

	private Path getExternalConfigFilePath() {
		if (externalConfigPath == null) {
			return Path.of(EXTERNAL_CONFIG_FILE);
		} else {
			return externalConfigPath.resolve(EXTERNAL_CONFIG_FILE);
		}
	}

	public Path getExternalConfigPath() {
		return externalConfigPath;
	}

	public static void setExternalConfigDirectory(Path externalConfigPath) {

		PropertiesManager.externalConfigPath = externalConfigPath;
	}

	private InputStream getInternalConfigFileStream() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResourceAsStream(INTERNAL_CONFIG_FILE);
	}

}
