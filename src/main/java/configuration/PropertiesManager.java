package configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesManager {

	private static final String internalConfigFile = "config.properties";
	private static final String externalConfigFile = "config.properties";

	public boolean existsInternalConfigurationFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		return loader.getResource(internalConfigFile) != null;
	}

	public boolean existsExternalConfigurationFile() {
		return Files.exists(Path.of(externalConfigFile));
	}

	public boolean createExternalConfigurationFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader.getResource(internalConfigFile) == null) {
			return false;
		}

		Properties properties = PropertiesReader.getConfiguration(loader.getResourceAsStream(internalConfigFile));
		Path p = Path.of(externalConfigFile);

		return PropertiesWriter.writeConfiguration(properties, p);
	}

	public boolean createExternalConfigurationFileIfNotExist() {
		if (existsExternalConfigurationFile()) {
			return false;
		}

		if (existsInternalConfigurationFile()) {
			return false;
		}

		return createExternalConfigurationFile();
	}

	public Properties getProperties() throws IOException {
		Properties defaultProps = PropertiesReader.getConfiguration(Files.newInputStream(Path.of(internalConfigFile)));

		return PropertiesReader.getConfiguration(Files.newInputStream(Path.of(externalConfigFile)), defaultProps);
	}

}
