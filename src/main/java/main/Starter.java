package main;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import Utility.MyBaseLogger;
import client.HQClient;
import daemon.SimpleDaemon;
import export.JSONFileWriter;

public class Starter extends MyBaseLogger {

	private static String statsFolder = "HQstats";

	public Starter(Logger logger) {

		HQClient client = new HQClient(logger);
		client.setLogging(true);

		JSONFileWriter writer = new JSONFileWriter(client, statsFolder, logger);
		writer.setLogging(true);

		SimpleDaemon d = new SimpleDaemon(60, logger);
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
