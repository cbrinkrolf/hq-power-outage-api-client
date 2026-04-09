package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesReader {

	public static Properties getConfiguration(InputStream resourceStream) {
		return PropertiesReader.getConfiguration(resourceStream, null);
	}

	public static Properties getConfiguration(InputStream resourceStream, Properties defaultProperties) {

		if (defaultProperties == null) {
			defaultProperties = new Properties();
		}

		try {
			defaultProperties.load(resourceStream);
			resourceStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defaultProperties;
	}

}
