package export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utility.MyBaseLogger;
import client.Client;

public class JSONFileWriter extends MyBaseLogger implements DataSender {

	private String statsFolder = "HQstats";

	private Client client;

	public JSONFileWriter(Client client) {
		this(client, "HQstats", null);
	}

	public JSONFileWriter(Client client, String statsFolder) {
		this(client, statsFolder, null);
	}

	public JSONFileWriter(Client client, String statsFolder, Logger logger) {
		this.client = client;
		this.statsFolder = statsFolder;
		this.setLogger(logger);
	}

	@Override
	public void sendData() {

		checkAndCreateFolder();
		collectRecord();

	}

	private void checkAndCreateFolder() {
		File folder = new File(statsFolder);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}
	}

	private void collectRecord() {

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

		String fileName = statsFolder + File.separator + version + ".json";
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
