package configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class PropertiesWriter {

	public static boolean writeConfiguration(Properties properties, Path path) {

		try (final OutputStream os = Files.newOutputStream(path)) {
			properties.store(os, null);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
