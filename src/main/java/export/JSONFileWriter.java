package export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;

import Utility.MyBaseLogger;
import client.Client;

public class JSONFileWriter extends MyBaseLogger implements DataSender {

	private Path statsPath = Path.of("HQstats");

	private Client client;

	public JSONFileWriter(Client client) {
		this(client, Path.of("HQstats"), null);
	}

	public JSONFileWriter(Client client, Path statsPath) {
		this(client, statsPath, null);
	}

	public JSONFileWriter(Client client, Path statsPath, Logger logger) {
		this.client = client;
		this.statsPath = statsPath;
		this.setLogger(logger);
	}

	@Override
	public void sendData() {
		try {
			checkAndCreateFolder();
			collectRecord();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setDirectory(Path directory) {

		this.statsPath = directory;
	}

	private void checkAndCreateFolder() throws IOException {
		if (!Files.exists(statsPath) || !Files.isDirectory(statsPath)) {
			Files.createDirectory(statsPath);
		}
	}

	private void collectRecord() {
		if (client == null) {
			return;
		}

		long version = client.getLatestBisVersion();
		if (isLogging()) {
			getLogger().log(Level.INFO, "Version: {0}", version);
		}
		if (version <= 0) {
			if (isLogging()) {
				getLogger().log(Level.INFO, "Version: {0} is invalid.", version);
			}
			return;
		}

		String fileName = statsPath + File.separator + version + ".json";
		File f = new File(fileName);

		if (f.exists() && f.isFile()) {
			if (isLogging()) {
				getLogger().log(Level.INFO, "Version: {0} exists already.", version);
			}
			return;
		}

		String response = client.getRecord(version);
		if (!response.isEmpty()) {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
				writer.write(response);
				if (isLogging()) {
					getLogger().log(Level.INFO, "Version: {0} file written.", version);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
