package main;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import Utility.MyBaseLogger;
import client.HQClient;
import configuration.PropertiesManager;
import daemon.SimpleDaemon;
import export.JSONFileWriter;

public class Starter extends MyBaseLogger {

	private static Path statsPath = Path.of("HQstats");

	public Starter(Logger logger) {

		HQClient client = new HQClient(logger);
		client.setLogging(true);

		JSONFileWriter writer = new JSONFileWriter(client, statsPath, logger);
		writer.setLogging(true);

		String secondsString = PropertiesManager.getInstance()
				.getPropertyValue(PropertiesManager.DAEMON_REFRESH_SECONDS);
		int seconds = 60;

		if (secondsString == null) {
			logMessageIfLogging(Level.SEVERE, "seconds for daemon is null");
		} else {
			try {
				seconds = Integer.parseInt(secondsString);
			} catch (NumberFormatException e) {
				logMessageIfLogging(Level.SEVERE, "seconds for daemon is not an integer");
			}
		}

		SimpleDaemon d = new SimpleDaemon(seconds, logger);
		d.setLogging(true);

		d.addDataSender(writer);
		d.start();

		System.out.println("durch");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("cleanup");
				LogManager.getLogManager().reset();
			}
		}));

	}
}
